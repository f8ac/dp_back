package pe.edu.pucp.packetsoft.dao;

import java.util.List;

import pe.edu.pucp.packetsoft.controllers.Prm;
import pe.edu.pucp.packetsoft.models.Envio;

public interface EnvioDao {
    List<Envio> getAll();
    Envio insert(Envio envio);
    Envio get(int id);
    List<Envio> listOrdenFecha();
    List<Envio> listCertainHoursFromDatetime(Prm param);
}
