package pe.edu.pucp.packetsoft.dao.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import pe.edu.pucp.packetsoft.dao.PlanViajeDao;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.PlanViaje;


@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class PlanViajeDaoImp  implements PlanViajeDao{

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @Override
    public PlanViaje insert(PlanViaje plan) {
        PlanViaje result = null;
        try{
            result = entityManager.merge(plan);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @Transactional
    @Override
    public List<PlanViaje> getAll() {
        List<PlanViaje> result = null;
        try{
            var hql = "from PlanViaje";
            result = entityManager.createQuery(hql).getResultList();
        }catch (Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @Transactional
    @Override
    public PlanViaje get(int id) {
        PlanViaje result = null;
        try{
            result = entityManager.find(PlanViaje.class, id);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @Override
    public List<PlanViaje> listByEnvio(Envio envio) {
        List<PlanViaje> result = null;
        try{
            var hql = "from PlanViaje pv where pv.envio = " + envio.getId();
            result = entityManager.createQuery(hql).getResultList();
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }
    
}
