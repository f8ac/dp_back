package pe.edu.pucp.packetsoft.dao;

import java.util.List;

import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.PlanViaje;

public interface PlanViajeDao {
    public PlanViaje insert(PlanViaje plan);
    public List<PlanViaje> getAll();
    public PlanViaje get(int id);
    public List<PlanViaje> listByEnvio(Envio envio);
    public List<Envio> listDistinctEnvios();
    public PlanViaje insertPlan(PlanViaje plan);
    public void deleteAll();
    public void truncTable();
    public void insertList(List<PlanViaje>listaPlanes);
}
