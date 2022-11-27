package pe.edu.pucp.packetsoft.controllers;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.pucp.packetsoft.PacketsoftApplication;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.EnvioRet;
import pe.edu.pucp.packetsoft.models.Movimiento;
import pe.edu.pucp.packetsoft.models.PlanViaje;
import pe.edu.pucp.packetsoft.models.Vuelo;
import pe.edu.pucp.packetsoft.models.VueloRet;
import pe.edu.pucp.packetsoft.models.VueloUtil;
import pe.edu.pucp.packetsoft.services.AeropuertoService;
import pe.edu.pucp.packetsoft.services.ContinenteService;
import pe.edu.pucp.packetsoft.services.EnvioService;
import pe.edu.pucp.packetsoft.services.VueloService;
import pe.edu.pucp.packetsoft.utils.AstarNode;
import pe.edu.pucp.packetsoft.utils.AstarSearch;

@RestController
@RequestMapping("/main")
@CrossOrigin
public class MainController {
    @Autowired
    private ContinenteService continenteService;
    @Autowired
    private AeropuertoService aeropuertoService;
    @Autowired
    private EnvioService envioService;
    @Autowired
    private VueloService vueloService;

    @PostMapping(value = "/main")
    List<VueloRet> main(@RequestBody Prm param){
        List<VueloRet> result = null;
        try{
            //truncamos la tabla de planes de viaje
            // planViajeService.truncTable();

            // AEROPUERTOS
            // List<Aeropuerto> listaAeropuertos = aeropuertoService.getAll();
            if(PacketsoftApplication.listaAeropuertos.size() == 0){
                PacketsoftApplication.listaAeropuertos = aeropuertoService.getAll();
            }
            List<AstarNode> listaNodos = airportsToNodes(PacketsoftApplication.listaAeropuertos);

            //creamos una linked list que controle los paquetes que tienen que ser liberados
            // Comparator<Movimiento> comPaquetes = new PaquetesComp();
            // PriorityQueue<Movimiento> colaPaquetes = new PriorityQueue<Movimiento>(comPaquetes);
            
            // VUELOS / VERTICES 
            // creamos una lista de vertices a partir de la fecha que vamos a simular
            List<VueloUtil> listaVuelos = flightsForTodayAndTomorrow(param);
            List<VueloRet> listaVuelosRetorno = processFlights(listaVuelos, listaNodos, param);
            
            // EL MAPEO ESTA TERMINADO ================================================================================
            //OBTENEMOS LOS ENVIOS ORDENADOS POR FECHA
            // List<Envio> listaEnvios = envioService.listCertainHoursFromDatetime(param);
            PacketsoftApplication.neededEnvios = envioService.readFilesToLocalWithParam(param,PacketsoftApplication.aeroHash);

            
            // List<Envio> listaEnvios = envioService.copyNeededEnvios(param);

            Calendar calInicio = Calendar.getInstance();
            Calendar calFin = Calendar.getInstance();
            setStartAndStopCalendars(calInicio,calFin,param);

            System.out.println("Inicio: "+calInicio.getTime()+", Final: "+calFin.getTime());
            
            Calendar curDate = Calendar.getInstance();
            curDate.setTime(calInicio.getTime());
            int j = 0, contRows = 1, contEnvios = 0;
            Envio envioActual = new Envio();

            List<PlanViaje> listaPlanes = new ArrayList<>();

            StopWatch watch = new  StopWatch();

            // create oos
            ObjectOutputStream pdv = new ObjectOutputStream(new FileOutputStream("pdv",true));
            
            Boolean colapso = false;

            Envio envioColapsado = new Envio();
            watch.start();
            while(true){

                attendQueue(PacketsoftApplication.colaPaquetes, curDate);

                envioColapsado = envioActual = PacketsoftApplication.neededEnvios.get(j);

                // if(contRows == 5912){
                //     int x = 1;
                // }

                if(param.debug){
                    System.out.print("\n"+contRows+") "+curDate.getTime()+" "+envioActual.getCodigo_envio()+" "+envioActual.getFecha_hora() + " ");
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
                    if(AstarSearch.restaAlmacenamiento(target, envioActual, listaVuelosRetorno, PacketsoftApplication.colaPaquetes)){
                        System.out.println("COLAPSO: el paquete no ha llegado al aeropuerto a tiempo.");
                        System.out.println("ID envio fallido: " + envioActual.getCodigo_envio());
                        System.out.println("ID vuelo fallido: " + target.vuelo.getVuelo().getId());
                        System.out.println("Salida vuelo fallido: " + target.vuelo.getSalida_real());
                        System.out.println("Llegada vuelo fallido: " + target.vuelo.getLlegada_real());
                        System.out.println(target.vuelo.getVuelo().getAeropuerto_salida().getId());
                        System.out.println(target.vuelo.getVuelo().getAeropuerto_llegada().getId());
                        envioColapsado = envioActual;
                        colapso = true;
                        break;
                    }
                    savePlan(target,envioActual,listaPlanes);
                    // AstarSearch.printPath(target);
                    AstarSearch.clearParents(listaNodos);
                    contEnvios++;
                    j++;
                }
                contRows++;
                if(j >= PacketsoftApplication.neededEnvios.size()){
                    break;
                }else if(!sameDateTime(curDate.getTime(), PacketsoftApplication.neededEnvios.get(j).getFecha_hora())){
                    curDate.add(Calendar.MINUTE, 1);
                }
            }


            // planViajeService.insertList(listaPlanes);
            int i = 0;
            for (PlanViaje planViaje : listaPlanes) {
                // planViaje.setVuelo_util(vueloService.insertUtil(planViaje.getVuelo_util()));
                // planViajeService.insert(planViaje);
                pdv.writeObject(planViaje);
                i++;
            }
            System.out.println(i+" entries written.");
            pdv.close();
            watch.stop();
            System.out.print("Tiempo total para procesar "+contEnvios+" envios: "+watch.getTotalTimeMillis()+" milisegundos.");
            result = vuelosTomados(listaVuelosRetorno);
            if(colapso){
                VueloRet vueloColapso = new VueloRet();
                vueloColapso.setColapso(envioColapsado.getCodigo_envio()+","+new java.sql.Timestamp(envioColapsado.getFecha_hora().getTime()).toString());
                result.add(vueloColapso);
            }
            PacketsoftApplication.enviosNuevos = false;
            PacketsoftApplication.neededEnvios = null;
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



    List<VueloRet> processFlights(List<VueloUtil> listaVuelos, List<AstarNode> listaNodos, Prm param){
        List<VueloRet> result = null;
        try{
            List<VueloRet> listaVuelosRetorno = new ArrayList<VueloRet>();
            for (VueloUtil vuelo : listaVuelos) {
                int iOrigen = -1, iDestino = -1, j = 0;
                for (AstarNode nodo : listaNodos) {
                    // ENCONTRAMOS EL NODO ORIGEN Y DESTINO EN LA LISTA
                    if(nodo.getAeropuerto().getId() == vuelo.getVuelo().getAeropuerto_salida().getId())
                        iOrigen = j;
                    if(nodo.getAeropuerto().getId() == vuelo.getVuelo().getAeropuerto_llegada().getId())
                        iDestino = j; 
                    j++;
                }
                int cap; 
                if(vuelo.getVuelo().getAeropuerto_salida().getContinente().getId() == vuelo.getVuelo().getAeropuerto_llegada().getContinente().getId()){
                    if(vuelo.getVuelo().getAeropuerto_salida().getContinente().getId() == 1)
                        cap = 250;
                    else
                        cap = 300;
                }else
                    cap = 350;
                vuelo.setCap_tot_real(cap);
                int costo = vuelo.getVuelo().getTiempo_vuelo_minutos();
                // System.out.println(">"+iOrigen);
                listaNodos.get(iOrigen).addBranch(costo, listaNodos.get(iDestino),vuelo);
                // Agregamos este vuelo a la lista de vuelos retornables
                VueloRet vueloRetorno = new VueloRet();
                vueloRetorno.setVuelo_util(vuelo);
                listaVuelosRetorno.add(vueloRetorno);
                // inicializar el inventario vacio
                // List<Envio> inventario = new ArrayList<Envio>();
                // vueloRetorno.setInventario(inventario);
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
                if(vueloRet.getVuelo_util().getCap_util_real()>0){
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

    void savePlan(AstarNode target, Envio envio, List<PlanViaje> listaPlanes){
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
                    PlanViaje nuevo = new PlanViaje();
                    EnvioRet nuevoEnvioRet = new EnvioRet();
                    nuevoEnvioRet.getAttributesFromEnvio(envio);
                    // envioService.insertRet(nuevoEnvioRet);
                    nuevo.setId_envio_ret(envio.getCodigo_envio());
                    nuevo.setVuelo_util(flight.vuelo);
                    listaPlanes.add(nuevo);
                    // planViajeService.insert(nuevo);
                }
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    List<VueloUtil> flightsForTodayAndTomorrow(Prm param){
        List<VueloUtil> result = null;
        try{

            //get all flights
            List<Vuelo> flights = vueloService.getAll();
            flights.get(0).setCapacidad_utilizada(123);

            //create two local lists 
            List<VueloUtil> firstDay = new ArrayList<>();
            List<VueloUtil> secondDay = new ArrayList<>();

            //copy attributes to local declared classes two times
            // copyListButOnlyAttributes(firstDay, flights, 0);
            // copyListButOnlyAttributes(secondDay, flights, firstDay.size());

            //set data for vueloutil
            setAttributesVueloUtil(firstDay, flights, 0);
            setAttributesVueloUtil(secondDay, flights, firstDay.size());

            //set the time on the lists for today and tomorrow respectively
            Calendar calFirstDay = Calendar.getInstance();

            calFirstDay.set(Calendar.YEAR, param.anio);
            calFirstDay.set(Calendar.MONTH, param.mes-1);
            calFirstDay.set(Calendar.DAY_OF_MONTH, param.dia);
            setDepartureAndArrivalDates(firstDay,calFirstDay);

            calFirstDay.add(Calendar.DATE,1);
            setDepartureAndArrivalDates(secondDay,calFirstDay);

            //join the lists in only one list
            List<VueloUtil> joinedLists = firstDay;
            joinedLists.addAll(secondDay);

            //return the joined list

            result = joinedLists;

            //###################################################################            
            // result = vuelosNHoras;

        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    void copyListButOnlyAttributes(List<Vuelo>firstDay, List<Vuelo>flights, int offset){
        try{
            for (Vuelo vuelo : flights) {
                Vuelo nuevo = new Vuelo();
                copyFlightButOnlyAttributes(nuevo, vuelo, offset);
                firstDay.add(nuevo);
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    void copyFlightButOnlyAttributes(Vuelo nuevo, Vuelo vuelo, int offset){
        try{
            nuevo.setId(vuelo.getId()+offset);
            nuevo.setActivo(vuelo.getActivo());
            nuevo.setAeropuerto_salida(vuelo.getAeropuerto_salida());
            nuevo.setAeropuerto_llegada(vuelo.getAeropuerto_llegada());
            nuevo.setHora_salida(vuelo.getHora_salida());
            nuevo.setHora_llegada(vuelo.getHora_llegada());
            nuevo.setCapacidad_total(vuelo.getCapacidad_total());
            nuevo.setCapacidad_utilizada(vuelo.getCapacidad_utilizada());
            nuevo.setTiempo_vuelo_minutos(vuelo.getTiempo_vuelo_minutos());
            nuevo.setInternacional(vuelo.getInternacional());
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    void setDepartureAndArrivalDates(List<VueloUtil> flightsForTheDay,Calendar day){
        try{
            Calendar calLlegada = Calendar.getInstance();
            Calendar calSalida = Calendar.getInstance();

            long diff, diffInMillies;
            for (VueloUtil vuelo : flightsForTheDay) { //modificamos solamente la fecha a los vuelos, las horas son las correspondientes

                calLlegada.setTime(vuelo.getLlegada_real());
                calSalida.setTime(vuelo.getSalida_real());

                diffInMillies = Math.abs(vuelo.getLlegada_real().getTime() - vuelo.getSalida_real().getTime());
                diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                calLlegada.set(Calendar.DAY_OF_MONTH, day.get(Calendar.DAY_OF_MONTH));
                calLlegada.set(Calendar.MONTH, day.get(Calendar.MONTH));
                calLlegada.set(Calendar.YEAR, day.get(Calendar.YEAR));

                calSalida.set(Calendar.DAY_OF_MONTH, day.get(Calendar.DAY_OF_MONTH));
                calSalida.set(Calendar.MONTH, day.get(Calendar.MONTH));
                calSalida.set(Calendar.YEAR, day.get(Calendar.YEAR));

                calLlegada.add(Calendar.DATE, (int)diff);

                if(calLlegada.before(calSalida)){
                    calLlegada.add(Calendar.DATE,1);
                }

                vuelo.setLlegada_real(calLlegada.getTime());
                vuelo.setSalida_real(calSalida.getTime());                
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    void setAttributesVueloUtil(List<VueloUtil>firstDay, List<Vuelo>flights, int offset){
        try{
            for (Vuelo vuelo : flights) {
                VueloUtil nuevo = new VueloUtil();
                Vuelo vueloInfo = vuelo;

                nuevo.setId_aux(vuelo.getId());
                nuevo.setSalida_real(vuelo.getHora_salida());
                nuevo.setLlegada_real(vuelo.getHora_llegada());
                nuevo.setCap_util_real(0);
                nuevo.setVuelo(vueloInfo);

                firstDay.add(nuevo);
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    @PostMapping(value = "/setup")
    void setup(){
        try{
            continenteService.insertTodos();
            aeropuertoService.insertfile();
            vueloService.insertfile2();
            aeropuertoService.insertAll();
            
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    @PostMapping(value = "/reset")
    Boolean reset(){
        Boolean result = null;
        try{
            //resetear la cola de movimientos
            PacketsoftApplication.colaPaquetes = null;
            PacketsoftApplication.colaPaquetes = new PriorityQueue<>(PacketsoftApplication.comPaquetes);
            //setear todos los aeropuertos a cero
            for (Aeropuerto aeropuerto : PacketsoftApplication.listaAeropuertos) {
                aeropuerto.setCapacidad_utilizado(0);
            }
            //
            PacketsoftApplication.neededEnvios = null;
            PlanViajeController.deletePDV();
            result = true;
        }catch(Exception ex){   
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @PostMapping(value = "/load")
    String load(){
        String result = null;
        try{
            aeropuertoService.load();
            // envioService.load();
            result = "Load Successful.";
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @PostMapping(value = "/reset/envios")
    Boolean resetEnvios(){
        Boolean result = false;
        try{
            PacketsoftApplication.neededEnvios = null;
            PacketsoftApplication.enviosNuevos = false;
            result = true;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }
}


