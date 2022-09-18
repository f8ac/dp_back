package pe.edu.pucp.packetsoft.dao.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import pe.edu.pucp.packetsoft.dao.PaqueteDao;
import pe.edu.pucp.packetsoft.models.Paquete;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class PaqueteDaoImp implements PaqueteDao{
    
    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @Override
    public Paquete insert(Paquete paquete){
        Paquete result = null;
        try{
            result = entityManager.merge(paquete);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @Transactional
    @Override
    public List<Paquete> getAll() {
        List<Paquete> result = null;
        try{
            var hql = "from Paquete";
            result = entityManager.createQuery(hql).getResultList();
        }catch (Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @Transactional
    @Override
    public Paquete get(int id) {
        Paquete result = null;
        try{
            result = entityManager.find(Paquete.class, id);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

}
