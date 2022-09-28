package pe.edu.pucp.packetsoft.dao;

import java.util.List;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Vuelo;

public interface VueloDao {
    List<Vuelo> getAll();
    Vuelo get(int id);
    Vuelo insert(Vuelo vuelo);
    List<Vuelo> listVecinos(Aeropuerto aeropuerto, List<Integer> aeropuertosId);
    List<Vuelo> listVecinosLlegada(Aeropuerto aeropuerto, List<Integer> aeropuertosId);
}
