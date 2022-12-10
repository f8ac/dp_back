package pe.edu.pucp.packetsoft.controllers;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.pucp.packetsoft.PacketsoftApplication;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.PlanViaje;
import pe.edu.pucp.packetsoft.models.VueloRet;
import pe.edu.pucp.packetsoft.services.AeropuertoService;
import pe.edu.pucp.packetsoft.services.EnvioService;
import pe.edu.pucp.packetsoft.utils.AstarNode;
import pe.edu.pucp.packetsoft.utils.AstarSearch;

@RestController
@RequestMapping("/colapso")
@CrossOrigin
public class ColapsoController {

    @Autowired
    private AeropuertoService aeropuertoService;
    @Autowired
    private EnvioService envioService;

    @PostMapping(value = "/main")
    List<VueloRet> main(@RequestBody Prm param){
        List<VueloRet> result = null;
        try{
            // AEROPUERTOS
            // List<Aeropuerto> listaAeropuertos = aeropuertoService.getAll();
            if(PacketsoftApplication.listaAeropuertos.size() == 0){
                PacketsoftApplication.listaAeropuertos = aeropuertoService.getAll();
            }
            List<AstarNode> listaNodos = MainController.airportsToNodes(PacketsoftApplication.listaAeropuertos);
            
            // VUELOS / VERTICES 
            // creamos una lista de vertices a partir de la fecha que vamos a simular
            
            PacketsoftApplication.listaVuelosUtil = MainController.flightsForTodayAndTomorrow(param);
            List<VueloRet> listaVuelosRetorno = MainController.processFlights(PacketsoftApplication.listaVuelosUtil, listaNodos, param);
            
            // EL MAPEO ESTA TERMINADO ================================================================================
            //OBTENEMOS LOS ENVIOS ORDENADOS POR FECHA
            // List<Envio> listaEnvios = envioService.listCertainHoursFromDatetime(param);
            PacketsoftApplication.neededEnvios = envioService.readFilesToLocalWithParam(param,PacketsoftApplication.aeroHash);
            
            // List<Envio> listaEnvios = envioService.copyNeededEnvios(param);

            Calendar calInicio = Calendar.getInstance();
            Calendar calFin = Calendar.getInstance();
            MainController.setStartAndStopCalendars(calInicio,calFin,param);

            System.out.println("\nInicio: "+calInicio.getTime()+", Final: "+calFin.getTime());
            
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
                MainController.attendQueue(PacketsoftApplication.colaPaquetes, curDate, param);
                envioColapsado = envioActual = PacketsoftApplication.neededEnvios.get(j);
                // if(contRows == 52){
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
                if(MainController.sameDateTime(curDate.getTime() ,envioActual.getFecha_hora())){
                    if(MainController.esIntercontinental(envioActual))
                        envioActual.setIntercontinental(true);
                    else
                        envioActual.setIntercontinental(false);
                    // if(contEnvios == param.nEnvios)
                    //     break;

                    int origen  = MainController.indexNodoAeropuerto(listaNodos, envioActual.getAero_origen());
                    int destino = MainController.indexNodoAeropuerto(listaNodos, envioActual.getAero_destino());
                    if(param.debug){
                        System.out.print(">Ejecuta" + "["+contEnvios+"]");
                    }
                    AstarNode target = AstarSearch.aStar(listaNodos.get(origen), listaNodos.get(destino), envioActual);
                    if(AstarSearch.restaAlmacenamiento(target, envioActual, listaVuelosRetorno, PacketsoftApplication.colaPaquetes)){
                        System.out.println("COLAPSO: el paquete no ha llegado al aeropuerto a tiempo.");
                        System.out.println("ID envio fallido: " + envioActual.getCodigo_envio());
                        System.out.println("Salida de Envio fallido: " + envioActual.getFecha_hora());
                        System.out.println("Intercontinental: " + envioActual.getIntercontinental());
                        System.out.println("ID vuelo fallido: " + target.vuelo.getVuelo().getId());
                        System.out.println("Salida vuelo fallido: " + target.vuelo.getSalida_real());
                        System.out.println("Llegada vuelo fallido: " + target.vuelo.getLlegada_real());
                        System.out.println(target.vuelo.getVuelo().getAeropuerto_salida().getId());
                        System.out.println(target.vuelo.getVuelo().getAeropuerto_llegada().getId());
                        envioColapsado = envioActual;
                        colapso = true;
                        break;
                    }
                    MainController.savePlan(target,envioActual,listaPlanes);
                    // AstarSearch.printPath(target);
                    AstarSearch.clearParents(listaNodos);
                    contEnvios++;
                    j++;
                }
                contRows++;
                if(j >= PacketsoftApplication.neededEnvios.size()){
                    break;
                }else if(!MainController.sameDateTime(curDate.getTime(), PacketsoftApplication.neededEnvios.get(j).getFecha_hora())){
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
            result = MainController.vuelosTomados(listaVuelosRetorno,param);
            // if(colapso){
                VueloRet vueloColapso = new VueloRet();
                vueloColapso.setColapso(envioColapsado.getCodigo_envio()+","+new java.sql.Timestamp(envioColapsado.getFecha_hora().getTime()).toString());
                result.add(vueloColapso);
                PacketsoftApplication.envioCol = envioColapsado;
            // }
            PacketsoftApplication.enviosNuevos = false;
            PacketsoftApplication.neededEnvios = null;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }
}
