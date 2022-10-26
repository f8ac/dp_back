package pe.edu.pucp.packetsoft.dao.imp;

import java.util.Calendar;
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
@SuppressWarnings({"unchecked","null"})
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

    @Override
    public Vuelo buscarVuelo1(int idinicio, int idfin, Calendar horaSalida, int paquetes) {
        List<Vuelo> vuelo = null;

       java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
       String currentTime = sdf.format(horaSalida.getTime());

        try {
            var hql = "from Vuelo as v where v.aeropuerto_salida = "+idinicio+" and v.aeropuerto_llegada = "+idfin+" and v.hora_salida >"+"'"+currentTime+"'"+" and (v.capacidad_total-v.capacidad_utilizada)>="+paquetes+" order by v.tiempo_vuelo_minutos";
            vuelo = entityManager.createQuery(hql).getResultList();
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }

        if(vuelo.size()!=0){
            return vuelo.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Vuelo buscarVuelo2(int idinicio, int idfin, Calendar horaSalida, Calendar horaLlegada, int paquetes) {
        List<Vuelo> vuelo = null;
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentTime1 = sdf.format(horaSalida.getTime());
        String currentTime2 = sdf.format(horaLlegada.getTime());

        try {
            var hql = "from Vuelo as v where v.aeropuerto_salida = "+idinicio+" and v.aeropuerto_llegada = "+idfin+" and v.hora_salida >"+"'"+currentTime1+"'"+" and v.hora_llegada <"+"'"+currentTime2+"'"+" and (v.capacidad_total-v.capacidad_utilizada)>="+paquetes+" order by v.tiempo_vuelo_minutos";
            vuelo = entityManager.createQuery(hql).getResultList();
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }

        if(vuelo.size()!=0){
            return vuelo.get(0);
        }else{
            return null;
        }
    }


    
}
