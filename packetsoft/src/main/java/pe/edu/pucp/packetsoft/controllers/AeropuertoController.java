package pe.edu.pucp.packetsoft.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.AeropuertoRet;
import pe.edu.pucp.packetsoft.services.AeropuertoService;

import java.io.IOException;
import java.util.Hashtable;
// import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/aeropuerto")
@CrossOrigin("*")
public class AeropuertoController {
    @Autowired
    private AeropuertoService aeropuertoService;
    
    @GetMapping(value = "/list")
    List<Aeropuerto> getAll(){
        return aeropuertoService.getAll();
    }

    @GetMapping(value = "/get/{id}")
    Aeropuerto get(@PathVariable int id){
        Aeropuerto aeropuerto;
        aeropuerto = aeropuertoService.get(id);
        return aeropuerto;
    }

    @PostMapping(value = "/insert")
    Aeropuerto insert(@RequestBody Aeropuerto aero){
        return aeropuertoService.insert(aero);
    }


    @PostMapping(value = "/insertfile")
    void insertfile() throws IOException, InterruptedException{
        aeropuertoService.insertfile();
    }

    @PostMapping(value = "/get/codigo")
    Aeropuerto getByCodigo(@RequestBody Aeropuerto aeropuerto){
        return aeropuertoService.getByCodigo(aeropuerto);
    }

    @PostMapping(value = "/get/codigo/string")
    Aeropuerto getByCodigo(@RequestBody String codigo){
        Aeropuerto aeropuerto = aeropuertoService.getByCodigoString(codigo);
        return aeropuerto;
    }

    @PostMapping(value = "ret/insert")
    AeropuertoRet insertRet(@RequestBody AeropuertoRet aeropuertoRet){
        return aeropuertoService.insertRet(aeropuertoRet);
    }

    @PostMapping(value = "/ret/insert/all")
    public void insertAll(){
        aeropuertoService.insertAll();
    }

    @PostMapping(value = "/load")
    void load(){
        aeropuertoService.load();
    }

    public static Hashtable<String, Aeropuerto> airportToHash(List<Aeropuerto> lista){
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
