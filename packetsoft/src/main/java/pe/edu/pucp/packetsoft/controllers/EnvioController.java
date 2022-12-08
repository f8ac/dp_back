package pe.edu.pucp.packetsoft.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.packetsoft.PacketsoftApplication;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.AeropuertoRet;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.EnvioRet;
import pe.edu.pucp.packetsoft.services.EnvioService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@RestController
@RequestMapping("/envio")
@CrossOrigin
public class EnvioController {
    @Autowired
    private EnvioService envioService;

    
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
    public List<EnvioRet> listTable(@RequestBody Prm param){
        List<EnvioRet> result = null;
        try{
            // List<Aeropuerto> listaAeropuertos = aeropuertoService.getAll();
            // Hashtable<String, Aeropuerto> aeroHash = airportToHash(listaAeropuertos);
            // need to fix: ask for envios in a certain date
            List<Envio> neededEnvios;
                neededEnvios = envioService.readFilesToLocalWithParam(param,PacketsoftApplication.aeroHash);
            // PacketsoftApplication.neededEnvios = envioService.copyNeededEnvios(param);
            List<EnvioRet> listaEnviosRet = convertEnviosToEnviosRet(neededEnvios);
            result = listaEnviosRet;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        System.out.println("Table consulted.");
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

    List<EnvioRet> convertEnviosToEnviosRet(List<Envio> listaEnvios){
        List<EnvioRet> result = null;
        try{
            List<EnvioRet> listaEnviosRet = new ArrayList<>();
            for (Envio envio : listaEnvios) {
                EnvioRet envioRet = new EnvioRet();
                copyNeededAttributesFromEnvio(envioRet,envio);
                listaEnviosRet.add(envioRet);
            }
            result = listaEnviosRet;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    void copyNeededAttributesFromEnvio(EnvioRet envioRet,Envio envio){
        try{
            envioRet.setId(envio.getCodigo_envio());
            envioRet.setFecha_hora(envio.getFecha_hora());
            // envioRet.getFecha_hora().settime
            //proceso especial con aeropuertos
            AeropuertoRet aeroOrigen = new AeropuertoRet();
            AeropuertoRet aeroDestino = new AeropuertoRet();
            aeroOrigen.getAttributesFromAeropuerto(envio.getAero_origen());
            aeroDestino.getAttributesFromAeropuerto(envio.getAero_destino());
            envioRet.setAero_origen(aeroOrigen);
            envioRet.setAero_destino(aeroDestino);
            envioRet.setCant_paquetes_total(envio.getCant_paquetes_total());
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    @PostMapping(value = "/get/colapso")
    Envio getColapso(){
        Envio result = null;
        try{
            result = PacketsoftApplication.envioCol;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @PostMapping(value = "/load")
    String load(){
        String result = null;
        try{
            result = envioService.load();
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }


    @PostMapping(value = "/load/needed")
    String loadNeeded(Prm param){
        String result = null;
        try{
            result = envioService.loadNeeded(param);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }   
}
