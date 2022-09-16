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

    public Continente insert(Continente continente){
        return daoContinente.insert(continente);
    }

    public void insertTodos(){
        Continente con1 = new Continente("America");
        Continente con2 = new Continente("Europoa");
        Continente con3 = new Continente("Africa");


        daoContinente.insert(con1);
        daoContinente.insert(con2);
        daoContinente.insert(con3);
    }
}
