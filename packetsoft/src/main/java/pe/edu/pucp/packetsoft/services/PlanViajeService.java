package pe.edu.pucp.packetsoft.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.dao.PlanViajeDao;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.PlanViaje;

@Service
public class PlanViajeService {
    
    @Autowired
    private PlanViajeDao daoPlan;

    public PlanViaje insert(PlanViaje plan){
        return daoPlan.insert(plan);
    }

    public List<PlanViaje> getAll(){
        return daoPlan.getAll();
    }

    public PlanViaje get(int id){
        return daoPlan.get(id);
    }

    public List<PlanViaje> listByEnvio(Envio envio){
        return daoPlan.listByEnvio(envio);
    }

    public List<Envio> listDistinctEnvios(){
        return daoPlan.listDistinctEnvios();
    }
}
