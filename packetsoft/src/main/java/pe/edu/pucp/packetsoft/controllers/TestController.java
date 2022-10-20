package pe.edu.pucp.packetsoft.controllers;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.Vuelo;
import pe.edu.pucp.packetsoft.services.AeropuertoService;
import pe.edu.pucp.packetsoft.services.ContinenteService;
import pe.edu.pucp.packetsoft.services.EnvioService;
import pe.edu.pucp.packetsoft.services.VueloService;
import pe.edu.pucp.packetsoft.utils.AstarNode;
import pe.edu.pucp.packetsoft.utils.AstarSearch;

@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {
    
    @Autowired
    private AeropuertoService aeropuertoService;

    @Autowired
    private ContinenteService continenteService;

    @Autowired
    private EnvioService envioService;

    @Autowired
    private VueloService vueloService;

    @PostMapping(value = "/main")
    String main(@RequestBody int[] iter){
        String result = null;
        try{
            // SE INSERTAN LOS AEROPUERTOS EN UNA LISTA DE NODOS DEL MISMO TAMANIO
            List<Aeropuerto> listaAeropuertos = aeropuertoService.getAll();
            List<AstarNode> listaNodos = Arrays.asList(new AstarNode[listaAeropuertos.size()]);
            int i = 0;
            for (Aeropuerto aeropuerto : listaAeropuertos) {
                AstarNode nodo = new AstarNode(0);
                nodo.setAeropuerto(aeropuerto);
                listaNodos.set(i,nodo);
                i++;
            }
            // SE INSERTAN LOS COSTOS DE LOS VERTICES EN ORDEN DE LLEGADA
            List<Vuelo> listaVuelos = vueloService.getAll();
            for (Vuelo vuelo : listaVuelos) {

                int iOrigen = -1, iDestino = -1, j = 0;
                for (AstarNode nodo : listaNodos) {
                    // ENCONTRAMOS EL NODO ORIGEN EN LA LISTA
                    if(nodo.getAeropuerto().getId() == vuelo.getAeropuerto_salida().getId()){
                        iOrigen = j;
                    }
                    // ENCONTRAMOS EL NODO DESTINO EN LA LISTA
                    if(nodo.getAeropuerto().getId() == vuelo.getAeropuerto_llegada().getId()){
                        iDestino = j; 
                    }
                    j++;
                }
                // Evaluamos los aeropuertos de origen y destino.
                // Si estan en un mismo continente el almacenamiento sera un numero entre 20 y 30,
                // para distinto continente sera un numero entre 25 y 40
                int rand, max, min; 
                if(vuelo.getAeropuerto_salida().getContinente().getId() == vuelo.getAeropuerto_llegada().getContinente().getId()){
                    max = 30; min = 20;
                }else{
                    max = 40; min = 25;
                }
                rand = (int)((Math.random()*(max - min))+min);
                vuelo.setCapacidad_total(rand*10);
                int costo = vuelo.getTiempo_vuelo_minutos();
                listaNodos.get(iOrigen).addBranch(costo, listaNodos.get(iDestino),vuelo);
                // vuelo.setCapacidad_utilizada(iter[1]*vuelo.getCapacidad_total()/100);
            }
            // EL MAPEO ESTA TERMINADO ================================================================================
            /* 
            PARA CONOCER EL RESULTADO, TENEMOS QUE LLAMAR A 
            AstarSearch tomando el origen y el destino de un envio
            Para la simulacion total es mejor iterar los envios, luego ya se 
            iteraran minutos simulando el paso del tiempo
            */

            //OBTENEMOS LOS ENVIOS ORDENADOS POR FECHA
            List<Envio> listaEnvios = envioService.listOrdenFecha();

            // PRUEBA CON SOLO UN ELEMENTO ========================================================================== 
            // Envio envioPrueba = listaEnvios.get(100);
            // int iOrigen  = indexNodoAeropuerto(listaNodos,  envioPrueba.getAero_origen());
            // int iDestino = indexNodoAeropuerto(listaNodos, envioPrueba.getAero_destino());
            // AstarNode res = AstarSearch.aStar(listaNodos.get(iOrigen), listaNodos.get(iDestino),envioPrueba.getFecha_hora());
            // AstarSearch.restaAlmacenamiento(res, envioPrueba);
            // // AstarNode res = AstarSearch.aStar(listaNodos.get(0), listaNodos.get(35));
            // // AstarSearch.printNewPath(res);
            // AstarSearch.printPath(res);
    
            // PRUEBA CON LA LISTA DE ENVIOS ========================================================================== 
            // Date fechaInicio = new Date();
            Calendar calInicio = Calendar.getInstance();
            Calendar calFin = Calendar.getInstance();
            calInicio.set(iter[4], iter[3]-1, iter[2], 0, 0, 0);
            if(iter[5] != 0){
                calFin.setTime(calInicio.getTime());
                calFin.add(Calendar.DATE, iter[5]);
            }else{
                calFin.set(iter[4]+1, iter[3]-1, iter[2], 0, 0, 0);
            }

            int j = 0;
            StopWatch watch = new  StopWatch();
            watch.start();
            for (Envio envioActual : listaEnvios) {

                if(j == iter[0]){
                    break;
                }
                if(!envioActual.getFecha_hora().before(calFin.getTime())){
                    break;
                }
                if(!envioActual.getFecha_hora().after(calInicio.getTime())){
                    continue;
                }
                
                System.out.print(j+") "+envioActual.getFecha_hora() + " ");
                envioActual = listaEnvios.get(j);
                int origen  = indexNodoAeropuerto(listaNodos,  envioActual.getAero_origen());
                int destino = indexNodoAeropuerto(listaNodos, envioActual.getAero_destino());

                AstarNode target = AstarSearch.aStar(listaNodos.get(origen), listaNodos.get(destino), envioActual);

                AstarSearch.restaAlmacenamiento(target, envioActual);
                AstarSearch.printPath(target);
                AstarSearch.clearParents(listaNodos);
                j++;
            }
            watch.stop();
            System.out.print("Tiempo total para procesar "+j+" envios: "+watch.getTotalTimeMillis()+" milisegundos.");
            result = "eksito";
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    int indexNodoAeropuerto(List<AstarNode> listaNodos,Aeropuerto aeropuerto){
        int i = 0;
        for (AstarNode astarNode : listaNodos) {
            if(astarNode.aeropuerto.getId() == aeropuerto.getId()){
                return i;
            }
            i++;
        }
        return -1;
    }

    @PostMapping(value = "/insert")
    String insert(){
        String result = null;
        try{
            continenteService.insertTodos();
            aeropuertoService.insertfile();
            // envioService.insertfile();
            vueloService.insertfile();
            
        }catch(Exception ex){
            System.err.println(ex.getMessage());
            result = "error lol";
        }
        return result;
    }

    /*   
    @PostMapping(value = "/obsolete")
    String obsoleteMains(@RequestParam("file") MultipartFile file){
        try{
            InputStream targeStream = new ByteArrayInputStream(file.getBytes());
            BufferedInputStream buffer = new BufferedInputStream(targeStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(buffer));
            String line;
            reader.readLine();
            reader.readLine();
            reader.readLine();
            while(reader.ready()){
                line = reader.readLine();
                String nombreContinente = line.trim();
                Continente continente = new Continente();
                continente.setNombre(nombreContinente);
                Continente result = continenteService.insert(continente);
                while(true){
                    line = reader.readLine();
                    if(line == null) break;
                    if(line == null || line.isEmpty() ){
                        break;
                    }
                    String codAeropuerto;
                    String nomPais;
                    String codCiudad;
                    codAeropuerto = line.substring(4, 11).trim();
                    nomPais = line.substring(32, 47).trim();
                    codCiudad = line.substring(48, 52).trim();
                    Aeropuerto aeropuerto = new Aeropuerto();
                    aeropuerto.setCod_aeropuerto(codAeropuerto);
                    aeropuerto.setCod_ciudad(codCiudad);
                    aeropuerto.setPais(nomPais);
                    aeropuerto.setContinente(result);
                    aeropuertoService.insert(aeropuerto);
                }
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return "main";
    } 
    */
}