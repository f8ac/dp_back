package pe.edu.pucp.packetsoft.dao;

import java.util.List;

import pe.edu.pucp.packetsoft.models.Paquete;

public interface PaqueteDao {
    public Paquete insert(Paquete paquete);
    public List<Paquete> getAll();
    public Paquete get(int id);
}
