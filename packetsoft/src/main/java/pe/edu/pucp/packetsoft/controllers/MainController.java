package pe.edu.pucp.packetsoft.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.Movimiento;
import pe.edu.pucp.packetsoft.models.PlanViaje;
import pe.edu.pucp.packetsoft.models.Vuelo;
import pe.edu.pucp.packetsoft.models.VueloRet;
import pe.edu.pucp.packetsoft.services.AeropuertoService;
import pe.edu.pucp.packetsoft.services.EnvioService;
import pe.edu.pucp.packetsoft.services.PlanViajeService;
import pe.edu.pucp.packetsoft.services.VueloService;
import pe.edu.pucp.packetsoft.utils.AstarNode;
import pe.edu.pucp.packetsoft.utils.AstarSearch;
import pe.edu.pucp.packetsoft.utils.PaquetesComp;

@RestController
@RequestMapping("/main")
@CrossOrigin
public class MainController {
    @Autowired
    private AeropuertoService aeropuertoService;
    @Autowired
    private EnvioService envioService;
    @Autowired
    private VueloService vueloService;
    @Autowired 
    private PlanViajeService planViajeService;

    @PostMapping(value = "/main")
    List<VueloRet> main(@RequestBody Prm param){
        List<VueloRet> result = null;
        try{
            // AEROPUERTOS
            List<Aeropuerto> listaAeropuertos = aeropuertoService.getAll();
            List<AstarNode> listaNodos = airportsToNodes(listaAeropuertos);

            //creamos una linked list que controle los paquetes que tienen que ser liberados
            Comparator<Movimiento> comPaquetes = new PaquetesComp();
            PriorityQueue<Movimiento> colaPaquetes = new PriorityQueue<Movimiento>(comPaquetes);
            
            // VUELOS / VERTICES 
            // creamos una lista de vertices a partir de la fecha que vamos a simular
            // List<Vuelo> listaVuelos = vueloService.getAll();
            List<Vuelo> listaVuelos = vueloService.readFileToLocal();
            // List<Vuelo> listaVuelos = vueloService.readFileToLocal();
            List<VueloRet> listaVuelosRetorno = processFlights(listaVuelos, listaNodos);
            
            // EL MAPEO ESTA TERMINADO ================================================================================
            //OBTENEMOS LOS ENVIOS ORDENADOS POR FECHA
            List<Envio> listaEnvios = envioService.listOrdenFecha();

            Calendar calInicio = Calendar.getInstance();
            Calendar calFin = Calendar.getInstance();
            setStartAndStopCalendars(calInicio,calFin,param);

            System.out.println("Inicio: "+calInicio.getTime()+", Final: "+calFin.getTime());
            
            Calendar curDate = Calendar.getInstance();
            curDate.setTime(calInicio.getTime());
            int j = 0, contRows = 1, contEnvios = 0;
            Envio envioActual = new Envio();

            StopWatch watch = new  StopWatch();
            watch.start();
            while(true){

                attendQueue(colaPaquetes, curDate);

                envioActual = listaEnvios.get(j);
                if(param.debug){
                    System.out.print("\n"+contRows+") "+curDate.getTime()+" "+envioActual.getId()+" "+envioActual.getFecha_hora() + " ");
                }
                if(curDate.getTime().after(calFin.getTime()))
                    break;
                if(curDate.getTime().before(calInicio.getTime())){ //
                    curDate.add(Calendar.MINUTE, 1);
                    continue;
                }
                if(calInicio.getTime().after(envioActual.getFecha_hora())){ //
                    j++;
                    contRows++;
                    continue;
                }

                

                if(sameDateTime(curDate.getTime() ,envioActual.getFecha_hora())){
                    if(esIntercontinental(envioActual))
                        envioActual.setIntercontinental(true);
                    else
                        envioActual.setIntercontinental(false);
                    // if(contEnvios == param.nEnvios)
                    //     break;

                    int origen  = indexNodoAeropuerto(listaNodos, envioActual.getAero_origen());
                    int destino = indexNodoAeropuerto(listaNodos, envioActual.getAero_destino());
                    if(param.debug){
                        System.out.print(">Ejecuta" + "["+contEnvios+"]");
                    }
                    AstarNode target = AstarSearch.aStar(listaNodos.get(origen), listaNodos.get(destino), envioActual);
                    if(AstarSearch.restaAlmacenamiento(target, envioActual, listaVuelosRetorno, colaPaquetes)){
                        System.out.println("COLAPSO: el paquete no ha llegado al aeropuerto a tiempo.");
                        System.out.println("ID envio fallido: " + envioActual.getId());
                        System.out.println("ID vuelo fallido: " + target.vuelo.getId());
                        System.out.println("Llegada vuelo fallido: " + target.vuelo.getHora_llegada());
                        System.out.println(target.vuelo.getAeropuerto_salida().getId());
                        System.out.println(target.vuelo.getAeropuerto_llegada().getId());
                        break;
                    }
                    savePlan(target,envioActual);
                    // AstarSearch.printPath(target);
                    AstarSearch.clearParents(listaNodos);
                    contEnvios++;
                    j++;
                }
                contRows++;
                if(contRows == 817){
                    // int k = 0;
                }
                if(!sameDateTime(curDate.getTime(), listaEnvios.get(j).getFecha_hora())){
                    curDate.add(Calendar.MINUTE, 1);
                }
            }
            watch.stop();
            System.out.print("Tiempo total para procesar "+contEnvios+" envios: "+watch.getTotalTimeMillis()+" milisegundos.");
            result = vuelosTomados(listaVuelosRetorno);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    // END OF MAIN ===================================================================================================================

    List<AstarNode> airportsToNodes(List<Aeropuerto> listaAeropuertos){
        List<AstarNode> result = null;
        try{
            List<AstarNode> listaNodos = Arrays.asList(new AstarNode[listaAeropuertos.size()]);
            int i = 0;
            for (Aeropuerto aeropuerto : listaAeropuertos) {
                // setear capacidad total dependiendo del continente.
                int cap = 0;
                if(aeropuerto.getContinente().getId() == 1)
                    cap = 850;
                else if(aeropuerto.getContinente().getId() == 2)
                    cap = 900;
                aeropuerto.setCapacidad_total(cap);
                aeropuerto.setCapacidad_utilizado(0);
                AstarNode nodo = new AstarNode(0);
                nodo.setAeropuerto(aeropuerto);
                listaNodos.set(i,nodo);
                i++;
            }
            result = listaNodos;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    List<VueloRet> processFlights(List<Vuelo> listaVuelos, List<AstarNode> listaNodos){
        List<VueloRet> result = null;
        try{
            List<VueloRet> listaVuelosRetorno = new ArrayList<VueloRet>();
            for (Vuelo vuelo : listaVuelos) {
                int iOrigen = -1, iDestino = -1, j = 0;
                for (AstarNode nodo : listaNodos) {
                    // ENCONTRAMOS EL NODO ORIGEN Y DESTINO EN LA LISTA
                    if(nodo.getAeropuerto().getId() == vuelo.getAeropuerto_salida().getId())
                        iOrigen = j;
                    if(nodo.getAeropuerto().getId() == vuelo.getAeropuerto_llegada().getId())
                        iDestino = j; 
                    j++;
                }
                int cap; 
                if(vuelo.getAeropuerto_salida().getContinente().getId() == vuelo.getAeropuerto_llegada().getContinente().getId()){
                    if(vuelo.getAeropuerto_salida().getContinente().getId() == 1)
                        cap = 250;
                    else
                        cap = 300;
                }else
                    cap = 350;
                vuelo.setCapacidad_total(cap);
                int costo = vuelo.getTiempo_vuelo_minutos();
                listaNodos.get(iOrigen).addBranch(costo, listaNodos.get(iDestino),vuelo);
                // Agregamos este vuelo a la lista de vuelos retornables
                VueloRet vueloRetorno = new VueloRet();
                vueloRetorno.setVuelo(vuelo);
                listaVuelosRetorno.add(vueloRetorno);
                // inicializar el inventario vacio
                List<Envio> inventario = new ArrayList<Envio>();
                vueloRetorno.setInventario(inventario);
            }
            result = listaVuelosRetorno;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    void setStartAndStopCalendars(Calendar calInicio, Calendar calFin, Prm param){
        try{
            calInicio.set(param.anio, param.mes-1, param.dia, param.hora, param.minuto, param.segundo);
            // if(param.diaSimul != 0){
            //     calFin.setTime(calInicio.getTime());
            //     calFin.add(Calendar.DATE, param.diaSimul);
            // }
            calFin.setTime(calInicio.getTime());
            if(param.horaSimul != 0){
                calFin.add(Calendar.HOUR_OF_DAY, param.horaSimul);
            }
            if(param.minSimul != 0){
                calFin.add(Calendar.MINUTE, param.minSimul);
            }
            if(param.horaSimul == 0 && param.minSimul == 0){
                calFin.set(param.anio+1, param.mes-1, param.dia, param.hora, param.minuto, param.segundo);
            }
            calFin.add(Calendar.MINUTE, -2);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    Boolean esIntercontinental(Envio envio){
        Boolean result = null;
        try{
            if(envio.getAero_destino().getContinente().getId() == envio.getAero_origen().getContinente().getId()){
                return false;
            }
            return true;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    int indexNodoAeropuerto(List<AstarNode> listaNodos,Aeropuerto aeropuerto){
        int i = 0;
        for (AstarNode astarNode : listaNodos) {
            if(astarNode.aeropuerto.getId() == aeropuerto.getId()){return i;}
            i++;
        }
        return -1;
    }

    List<VueloRet> vuelosTomados(List<VueloRet> listaVuelos){
        List<VueloRet> result = null;
        try{
            result = new ArrayList<VueloRet>();
            for (VueloRet vueloRet : listaVuelos) {
                if(!vueloRet.getInventario().isEmpty()){
                    result.add(vueloRet);
                }
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    void attendQueue(PriorityQueue<Movimiento> colaPaquetes, Calendar curDate){
        try{
            while(true){
                if(sameDateTime(curDate.getTime(), colaPaquetes.peek().getFecha()) ){
                    Movimiento curMov = colaPaquetes.poll();
                    Aeropuerto aeropuerto = curMov.getAeropuerto();
                    int curCap = aeropuerto.getCapacidad_utilizado();
                    aeropuerto.setCapacidad_utilizado(curCap + curMov.getEnvio().getCant_paquetes_total() * curMov.getOperacion());
                }else{
                    break;
                }
            }
            
        }catch(Exception ex){
            System.err.print(ex.getMessage());
        }
    }                           

    @SuppressWarnings({"deprecation"})
    Boolean sameDateTime(Date a,Date b){
        Boolean result = null;
        try{
            result =       a.getYear()      == b.getYear() 
                        && a.getMonth()     == b.getMonth()
                        && a.getDay()       == b.getDay()
                        && a.getHours()     == b.getHours()
                        && a.getMinutes()   == b.getMinutes()
                        ;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    void savePlan(AstarNode target, Envio envio){
        try{
            AstarNode n = target;
            if(n==null)
                return;
            List<AstarNode> flights = new ArrayList<>();
            while(n.parent != null){
                flights.add(n);
                n = n.parent;
            }
            flights.add(n);
            Collections.reverse(flights);
            for(AstarNode flight : flights){
                if(flight.vuelo != null){
                    //se inserta el envio con el vuelo en un entry
                    PlanViaje nuevo = new PlanViaje();
                    nuevo.setEnvio(envio);
                    nuevo.setVuelo(flight.vuelo);
                    planViajeService.insert(nuevo);
                }
            }
        }catch(Exception ex){
            System.out.println();
        }
    }
}


