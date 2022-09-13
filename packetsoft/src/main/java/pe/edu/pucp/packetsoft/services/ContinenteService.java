package pe.edu.pucp.packetsoft.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.dao.ContinenteDao;
import pe.edu.pucp.packetsoft.models.Continente;

@Service
public class ContinenteService {
    @Autowired
    private ContinenteDao daoContinente;

    public List<Continente> getAll(){
        return daoContinente.getAll();
    }
}
