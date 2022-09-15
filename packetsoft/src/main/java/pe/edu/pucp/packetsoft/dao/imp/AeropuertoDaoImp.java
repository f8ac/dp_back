package pe.edu.pucp.packetsoft.dao.imp;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import pe.edu.pucp.packetsoft.dao.AeropuertoDao;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import java.util.List;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class AeropuertoDaoImp implements AeropuertoDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public List<Aeropuerto> getAll() {
        List<Aeropuerto> list = null;
        try{
            var hql = "from Aeropuerto as a";
            list = entityManager.createQuery(hql).getResultList();
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return list;
    }



    @Transactional
    @Override
    public Aeropuerto get(int id) {
        Aeropuerto aeropuerto = null;
        try {
            aeropuerto = entityManager.find(Aeropuerto.class, id);
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return aeropuerto;
    }



    @Transactional
    @Override
    public Aeropuerto insert(Aeropuerto aeropuerto) {
        Aeropuerto result = null;
        try{
            result = entityManager.merge(aeropuerto);
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return result;
    }








}
