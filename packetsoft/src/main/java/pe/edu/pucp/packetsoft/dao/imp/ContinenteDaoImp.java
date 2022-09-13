package pe.edu.pucp.packetsoft.dao.imp;

import java.util.List;

import pe.edu.pucp.packetsoft.dao.ContinenteDao;
import pe.edu.pucp.packetsoft.models.Continente;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class ContinenteDaoImp implements ContinenteDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public List<Continente> getAll() {
        List<Continente> list = null;
        try {
            var hql = "from Continente as c";
            list = entityManager.createQuery(hql).getResultList();
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return list;
    }
}
