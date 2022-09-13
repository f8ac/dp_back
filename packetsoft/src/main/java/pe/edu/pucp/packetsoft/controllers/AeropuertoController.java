package pe.edu.pucp.packetsoft.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.services.AeropuertoService;

@RestController
@RequestMapping("/aeropuerto")
@CrossOrigin
public class AeropuertoController {
    @Autowired
    private AeropuertoService aeropuertoService;
    
    @PostMapping(value = "/insert")
    Aeropuerto insert(@RequestBody Aeropuerto aero){
        return aeropuertoService.insert(aero);
    }

}
