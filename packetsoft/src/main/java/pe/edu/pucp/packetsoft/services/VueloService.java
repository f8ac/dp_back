package pe.edu.pucp.packetsoft.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.dao.VueloDao;
import pe.edu.pucp.packetsoft.models.Vuelo;

@Service
public class VueloService {
    @Autowired
    private VueloDao daoEmpresa;

    public List<Vuelo> getAll(){
        return daoEmpresa.getAll();
    }

    public Vuelo get(int id){
        return daoEmpresa.get(id);
    }

    public Vuelo insert(Vuelo empresa){
        return daoEmpresa.insert(empresa);
    }
}
