package pe.edu.pucp.packetsoft.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.pucp.packetsoft.models.Paquete;
import pe.edu.pucp.packetsoft.models.PlanViaje;
import pe.edu.pucp.packetsoft.services.PaqueteService;
import pe.edu.pucp.packetsoft.services.PlanViajeService;

@RestController
@RequestMapping("/planviaje")
@CrossOrigin
public class PlanViajeController {
    
    @Autowired
    private PlanViajeService planViajeService;

    @PostMapping(value = "/insert")
    PlanViaje insert(@RequestBody PlanViaje plan){
        return planViajeService.insert(plan);
    }

    @GetMapping(value = "/list")
    List<PlanViaje> getAll(){
        return planViajeService.getAll();
    }

    @GetMapping(value = "/{}")
    PlanViaje get(@PathVariable int id){
        return planViajeService.get(id);
    }
}
