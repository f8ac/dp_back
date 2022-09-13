package pe.edu.pucp.packetsoft.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.pucp.packetsoft.models.Paquete;
import pe.edu.pucp.packetsoft.services.PaqueteService;

@RestController
@RequestMapping("/paquete")
@CrossOrigin
public class PaqueteController {
    
    @Autowired
    private PaqueteService paqueteService;

    @PostMapping(value = "/insert")
    Paquete insert(Paquete paquete){
        return paqueteService.insert(paquete);
    }

    @GetMapping(value = "/list")
    List<Paquete> getAll(){
        return paqueteService.getAll();
    }

    @GetMapping(value = "/{id}")
    Paquete get(@PathVariable int id){
        return paqueteService.get(id);
    }
    
}
