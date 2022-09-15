package pe.edu.pucp.packetsoft.dao;

import java.util.List;

import pe.edu.pucp.packetsoft.models.PlanViaje;

public interface PlanViajeDao {
    public PlanViaje insert(PlanViaje plan);
    public List<PlanViaje> getAll();
    public PlanViaje get(int id);
}
