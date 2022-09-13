package pe.edu.pucp.packetsoft.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.services.AeropuertoService;
import java.util.List;

@RestController
@RequestMapping("/aeropuerto")
@CrossOrigin
public class AeropuertoController {
    @Autowired
    private AeropuertoService aeropuertoService;
    
    @GetMapping(value = "/list")
    List<Aeropuerto> getAll(){
        return aeropuertoService.getAll();
    }

    @PostMapping(value = "/insert")
    Aeropuerto insert(@RequestBody Aeropuerto aero){
        return aeropuertoService.insert(aero);
    }

}
