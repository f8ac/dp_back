package pe.edu.pucp.packetsoft.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.services.EnvioService;

import java.io.IOException;
import java.text.ParseException;
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
        return envioService.get(id);
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

}
