package pe.edu.pucp.packetsoft.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.services.AeropuertoService;
import pe.edu.pucp.packetsoft.services.ContinenteService;
import pe.edu.pucp.packetsoft.services.EnvioService;
import pe.edu.pucp.packetsoft.utils.AstarNode;

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

    @PostMapping(value = "/main")
    String main(){
        String result = null;
        // insert();
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
            List<Envio> listaEnvios = envioService.getAll();
            for (Envio envio : listaEnvios) {
                aeropuertoService.get(envio.getAero_destino().getId());
            }
            result = "insertado xd";
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        



        return result;
    }

    @PostMapping(value = "/insert")
    String insert(){
        String result = null;
        try{

            continenteService.insertTodos();
            aeropuertoService.insertfile();
            envioService.insertfile();
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
