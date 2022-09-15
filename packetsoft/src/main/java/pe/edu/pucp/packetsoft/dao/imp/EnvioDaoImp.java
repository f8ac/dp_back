package pe.edu.pucp.packetsoft.dao.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import pe.edu.pucp.packetsoft.dao.EnvioDao;
import pe.edu.pucp.packetsoft.models.Envio;



@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class EnvioDaoImp implements EnvioDao {
    @PersistenceContext
    private EntityManager entityManager;
    

    @Transactional
    @Override
    public List<Envio> getAll() {
        List<Envio> list = null;
        try{
            var hql = "from Envio as a";
            list = entityManager.createQuery(hql).getResultList();
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return list;
    }


    @Transactional
    @Override
    public Envio insert(Envio envio) {
        Envio result = null;
        try{
            result = entityManager.merge(envio);
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return result;
    }
    
}
