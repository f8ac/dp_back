package pe.edu.pucp.packetsoft.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.EnvioRet;
import pe.edu.pucp.packetsoft.models.PlanViaje;
import pe.edu.pucp.packetsoft.models.PlanViajeRet;
import pe.edu.pucp.packetsoft.models.VueloUtil;
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

    @PostMapping(value = "/list/envio")
    List<PlanViaje> listByEnvio(@RequestBody Envio envio){
        return planViajeService.listByEnvio(envio);
    }

    @PostMapping(value = "/get/envio/ret")
    PlanViajeRet getPlanViajeByEnvio(@RequestBody Envio envio){
        PlanViajeRet result = null;
        try{
            List<PlanViaje> viajesXenvio = planViajeService.listByEnvio(envio);
            PlanViajeRet nuevoPlanViajeRet = new PlanViajeRet();
            viajesXenvio.get(0).getId_envio_ret();
            EnvioRet envioRet = new EnvioRet();
            envioRet.getAttributesFromEnvio(envio);
            nuevoPlanViajeRet.setEnvio(envioRet);
            List<VueloUtil> itinerario = new ArrayList<VueloUtil>();
            for (PlanViaje planViaje : viajesXenvio) {
                itinerario.add(planViaje.getVuelo_util());
            }
            nuevoPlanViajeRet.setItinerario(itinerario);
            result = nuevoPlanViajeRet;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @PostMapping(value = "/list/envio/ret")
    List<PlanViajeRet> listPlanViajeRet(){
        List<PlanViajeRet> result = null;
        try{
            List<PlanViajeRet> tablaPlanes = new ArrayList<PlanViajeRet>();
            List<Envio> listaEnvios = planViajeService.listDistinctEnvios();
            for (Envio envio : listaEnvios) {
                tablaPlanes.add(getPlanViajeByEnvio(envio));
            }
            result = tablaPlanes;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @DeleteMapping(value = "/delete/all")
    void deleteAll(){
        planViajeService.deleteAll();
    }

    @PostMapping(value = "/get/envio")
    PlanViajeRet getByEnvio(@RequestBody EnvioRet envio){
        PlanViajeRet result = null;
        try{
            result = planViajeService.getByEnvio(envio);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }
}
