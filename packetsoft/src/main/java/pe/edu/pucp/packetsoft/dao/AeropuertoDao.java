package pe.edu.pucp.packetsoft.dao;

import java.util.List;
import pe.edu.pucp.packetsoft.models.Aeropuerto;

public interface AeropuertoDao {
    List<Aeropuerto> getAll();
    Aeropuerto insert(Aeropuerto aeropuerto);

}
