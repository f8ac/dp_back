package pe.edu.pucp.packetsoft.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pe.edu.pucp.packetsoft.services.TabuSearchService;

@RestController
@RequestMapping("/tabuSearch")
@CrossOrigin
public class TabuSearchController {
    @Autowired
    private TabuSearchService tabuSearchService;

    @GetMapping(value = "/ejecutarAlgoritmo/{id1}/{id2}/{id3}")
    void ejecutarAlgoritmo(@PathVariable int id1, @PathVariable int id2, @PathVariable int id3){
        tabuSearchService.ejecutarAlgoritmo(id1,id2,id3);
    }


}
