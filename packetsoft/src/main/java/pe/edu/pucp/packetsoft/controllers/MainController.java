package pe.edu.pucp.packetsoft.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.pucp.packetsoft.services.AeropuertoService;
import pe.edu.pucp.packetsoft.services.ContinenteService;
import pe.edu.pucp.packetsoft.services.EnvioService;
import pe.edu.pucp.packetsoft.services.VueloService;

@RestController
@RequestMapping("/main")
@CrossOrigin
public class MainController {
    @Autowired
    private AeropuertoService aeropuertoService;
    @Autowired
    private ContinenteService continenteService;
    @Autowired
    private EnvioService envioService;
    @Autowired
    private VueloService vueloService;
}
