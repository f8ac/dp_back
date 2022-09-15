package pe.edu.pucp.packetsoft.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.packetsoft.models.Vuelo;
import pe.edu.pucp.packetsoft.services.VueloService;

@RestController
@RequestMapping("/vuelo")
@CrossOrigin
public class VueloController {
    @Autowired
    private VueloService vueloService;

    @GetMapping(value = "/list")
    List<Vuelo> getAll(){
        return vueloService.getAll();
    }

    @GetMapping(value = "/get/{id}")
    Vuelo get(@PathVariable int id){
        return vueloService.get(id);
    }

    @PostMapping(value = "/insert")
    Vuelo insert(@RequestBody Vuelo empresa){
        return vueloService.insert(empresa);
    }

    @PostMapping(value = "/insertfile")
    void insertfile() throws IOException, InterruptedException{
        vueloService.insertfile();
    }

}
