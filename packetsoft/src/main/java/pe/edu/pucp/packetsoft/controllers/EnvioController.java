package pe.edu.pucp.packetsoft.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.services.AeropuertoService;
import pe.edu.pucp.packetsoft.services.EnvioService;

import java.io.IOException;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.List;

@RestController
@RequestMapping("/envio")
@CrossOrigin
public class EnvioController {
    @Autowired
    private EnvioService envioService;
    @Autowired
    private AeropuertoService aeropuertoService;
    
    @GetMapping(value = "/list")
    List<Envio> getAll(){
        return envioService.getAll();
    }

    @GetMapping(value = "/get/{id}")
    Envio get(@PathVariable int id){
        Envio envio = envioService.get(id);
        return envio;
    }

    @PostMapping(value = "/insert")
    Envio insert(@RequestBody Envio envio){
        return envioService.insert(envio);
    }


    @PostMapping(value = "/insertfile")
    void insertfile() throws IOException, InterruptedException, ParseException{
        envioService.insertfile();
    }

    @PostMapping(value = "/list/orden/fecha")
    List<Envio> listOrdenFecha(){
        return envioService.listOrdenFecha();
    }

    @PostMapping(value = "/list/table")
    List<Envio> main(@RequestBody Prm param){
        List<Envio> result = null;
        try{
            List<Aeropuerto> listaAeropuertos = aeropuertoService.getAll();
            Hashtable<String, Aeropuerto> aeroHash = airportToHash(listaAeropuertos);
            List<Envio> listaEnvios = envioService.readFilesToLocal(param,aeroHash);
            result = listaEnvios;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    Hashtable<String, Aeropuerto> airportToHash(List<Aeropuerto> lista){
        Hashtable<String, Aeropuerto> result = null;
        try{
            Hashtable<String, Aeropuerto> aeroHash = new Hashtable<>();
            for (Aeropuerto aeropuerto : lista) {
                aeroHash.put(aeropuerto.getCod_aeropuerto(), aeropuerto);
            }
            result = aeroHash;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

}
