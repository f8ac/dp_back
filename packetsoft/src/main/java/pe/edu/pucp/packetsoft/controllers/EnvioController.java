package pe.edu.pucp.packetsoft.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.services.EnvioService;

import java.io.IOException;
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

    @PostMapping(value = "/insert")
    Envio insert(@RequestBody Envio envio){
        return envioService.insert(envio);
    }


    @PostMapping(value = "/insertfile")
    void insertfile() throws IOException, InterruptedException{
        envioService.insertfile();
    }

}
