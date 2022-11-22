package pe.edu.pucp.packetsoft.dao.imp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import pe.edu.pucp.packetsoft.dao.AeropuertoDao;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.AeropuertoRet;

import java.util.List;

@Transactional
@Repository
@SuppressWarnings({ "unchecked", "null" })
public class AeropuertoDaoImp implements AeropuertoDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public List<Aeropuerto> getAll() {
        List<Aeropuerto> list = null;
        try {
            var hql = "from Aeropuerto as a";
            list = entityManager.createQuery(hql).getResultList();
        } catch (Exception exception) {
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
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return aeropuerto;
    }

    @Transactional
    @Override
    public Aeropuerto insert(Aeropuerto aeropuerto) {
        Aeropuerto result = null;
        try {
            result = entityManager.merge(aeropuerto);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return result;
    }

    @Transactional
    @Override
    public Aeropuerto getByCodigo(Aeropuerto aeropuerto) {
        List<Aeropuerto> list = null;
        try {
            var hql = "from Aeropuerto as a where a.cod_aeropuerto = '" + aeropuerto.getCod_aeropuerto() + "'";
            list = entityManager.createQuery(hql).getResultList();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return list.get(0);
    }

    @Transactional
    @Override
    public Aeropuerto getByCodigoString(String codigo) {
        Aeropuerto result = null;
        try {
            var hql = "from Aeropuerto as a where a.cod_aeropuerto = '" + codigo + "'";
            result = entityManager.createQuery(hql,Aeropuerto.class).getSingleResult();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return result;
    }

    @Transactional
    @Override
    public AeropuertoRet insertRet(AeropuertoRet aeropuertoRet) {
        AeropuertoRet result = null;
        try {
            result = entityManager.merge(aeropuertoRet);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return result;
    }

}
