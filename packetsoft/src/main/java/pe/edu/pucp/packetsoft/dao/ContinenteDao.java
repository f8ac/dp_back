package pe.edu.pucp.packetsoft.dao;

import java.util.List;

import pe.edu.pucp.packetsoft.models.Continente;

public interface ContinenteDao {
    List<Continente> getAll();
    //Continente get(int id);
}
