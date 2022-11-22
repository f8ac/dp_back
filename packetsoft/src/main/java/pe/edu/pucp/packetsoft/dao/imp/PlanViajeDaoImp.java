package pe.edu.pucp.packetsoft.dao.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import pe.edu.pucp.packetsoft.dao.PlanViajeDao;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.PlanViaje;
import pe.edu.pucp.packetsoft.models.VueloUtil;


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

    @Override
    public List<Envio> listDistinctEnvios() {
        List<Envio> result = null;
        try{
            var hql = "SELECT DISTINCT pv.envio from PlanViaje pv";
            result = entityManager.createQuery(hql).getResultList();
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @Transactional
    @Override
    public PlanViaje insertPlan(PlanViaje plan) {
        PlanViaje result = null;
        try{
            // buscar si ya existe el plan de viaje para este envio comparando el id del envio e id del vuelo
            var hql = "from PlanViaje p where p.id_envio_ret = '" + plan.getId_envio_ret() + "' and p.vuelo_util = " + plan.getVuelo_util().getId_aux();
            List<PlanViaje> listaPlanes = entityManager.createQuery(hql).getResultList();
            //si la lista tiene elementos, entonces ya existe el plan de viaje
            if(listaPlanes.size()==0 || listaPlanes == null){
                //primero insertamos el vueloutil
                VueloUtil resultVuelo = entityManager.merge(plan.getVuelo_util());
                //lo seteamos en el plan de viaje
                plan.setVuelo_util(resultVuelo);
                //insertamos el plan de viaje
                result = entityManager.merge(plan);
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @Override
    public void deleteAll() {
        List<PlanViaje> resultado = null;
        
        try{
            String hql = "FROM PlanViaje as p ";
            resultado =  entityManager.createQuery(hql).getResultList();

            for (PlanViaje planViaje : resultado) {
                entityManager.remove(planViaje);
            }
        }catch(Exception ex){ System.out.println(ex.getMessage()); }
        
    }

    @Transactional
    @Override
    public void truncTable() {
        try{
            String sql;
            sql = "DROP TABLE IF EXISTS vuelo_x_plan";
            entityManager.createNativeQuery(sql).executeUpdate();
            sql = "TRUNCATE TABLE plan_viaje";
            entityManager.createNativeQuery(sql).executeUpdate();
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    @Transactional
    @Override
    public void insertList(List<PlanViaje> listaPlanes) {
        try{
            VueloUtil vueloUtil;
            for (PlanViaje planViaje : listaPlanes) {
                vueloUtil = entityManager.merge(planViaje.getVuelo_util());
                planViaje.setVuelo_util(vueloUtil);
                entityManager.merge(planViaje);
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
    
}
