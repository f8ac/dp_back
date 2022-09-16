package pe.edu.pucp.packetsoft.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.packetsoft.models.Continente;
import pe.edu.pucp.packetsoft.services.ContinenteService;

@RestController
@RequestMapping("/continente")
@CrossOrigin
public class ContinenteController {
    
    @Autowired
    private ContinenteService continenteService;

    @GetMapping(value = "/list")
    List<Continente> getAll(){
        return continenteService.getAll();
    }

    @PostMapping(value = "/insert")
    Continente insert(@RequestBody Continente continente){
        return continenteService.insert(continente);
    }

    @PostMapping(value = "/insertTodos")
    void insertTodos(){
        continenteService.insertTodos();
    }

}
