package pe.edu.pucp.packetsoft.dao.imp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import pe.edu.pucp.packetsoft.dao.VueloDao;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Vuelo;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class VueloDaoImp implements VueloDao{
    
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public List<Vuelo> getAll() {
        List<Vuelo> list = null;
        try {
            var hql = "from Vuelo as v";
            list = entityManager.createQuery(hql).getResultList();
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return list;
    }

    @Transactional
    @Override
    public Vuelo get(int id) {
        Vuelo vuelo = null;
        try {
            vuelo = entityManager.find(Vuelo.class, id);
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return vuelo;
    }

    @Transactional
    @Override
    public Vuelo insert(Vuelo vuelo) {
        Vuelo result = null;
        try {
            result = entityManager.merge(vuelo);
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return result;
    }

    @Transactional
    @Override
    public List<Vuelo> listVecinos(Aeropuerto aeropuerto,List<Integer> aeropuertosId) {
        List<Vuelo> list = null;
        try {
            String condicion="(";
            for(int i=0;i<aeropuertosId.size();i++){
                condicion+=aeropuertosId.get(i);
                if(i!=aeropuertosId.size()-1){
                    condicion+=",";
                }
            }
            condicion+=")";
            var hql = "from Vuelo as a where a.aeropuerto_salida = "+ aeropuerto.getId()+" and a.aeropuerto_llegada not in "+ condicion;
            list = entityManager.createQuery(hql).getResultList();
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return list;
    }

    @Transactional
    @Override
    public List<Vuelo> listVecinosLlegada(Aeropuerto aeropuerto,List<Integer> aeropuertosId) {
        List<Vuelo> list = null;
        try {
            String condicion="(";
            for(int i=0;i<aeropuertosId.size();i++){
                condicion+=aeropuertosId.get(i);
                if(i!=aeropuertosId.size()-1){
                    condicion+=",";
                }
            }
            condicion+=")";
            var hql = "from Vuelo as a where a.aeropuerto_llegada = "+ aeropuerto.getId()+" and a.aeropuerto_salida not in "+ condicion;
            list = entityManager.createQuery(hql).getResultList();
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return list;
    }


    
}
