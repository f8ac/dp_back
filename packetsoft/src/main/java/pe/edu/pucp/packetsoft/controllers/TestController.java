package pe.edu.pucp.packetsoft.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.services.AeropuertoService;
import pe.edu.pucp.packetsoft.services.UsuarioService;

@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {
    
    @Autowired
    private AeropuertoService aeropuertoService;

    @PostMapping(value = "/")
    String main(){
        List<Aeropuerto> listaAeropuerto = null;

        try{
            listaAeropuerto = aeropuertoService.getAll();
            System.out.println(listaAeropuerto);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return "main";
    }

}
