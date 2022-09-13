package pe.edu.pucp.packetsoft.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.dao.PaqueteDao;
import pe.edu.pucp.packetsoft.models.Paquete;

@Service
public class PaqueteService {
    
    @Autowired
    private PaqueteDao daoPaquete;

    public Paquete insert(Paquete paquete){
        return daoPaquete.insert(paquete);
    }

    public List<Paquete> getAll(){
        return daoPaquete.getAll();
    }

    public Paquete get(int id){
        return daoPaquete.get(id);
    }

}
