package pe.edu.pucp.packetsoft.dao;

import java.util.List;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.AeropuertoRet;

public interface AeropuertoDao {
    List<Aeropuerto> getAll();
    Aeropuerto get(int id);
    Aeropuerto insert(Aeropuerto aeropuerto);
    Aeropuerto getByCodigo(Aeropuerto aeropuerto);
    Aeropuerto getByCodigoString(String codigo);
    AeropuertoRet insertRet(AeropuertoRet aeropuertoRet);
}
