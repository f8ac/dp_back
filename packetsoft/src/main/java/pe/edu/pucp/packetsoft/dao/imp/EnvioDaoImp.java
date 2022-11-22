package pe.edu.pucp.packetsoft.dao.imp;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import pe.edu.pucp.packetsoft.controllers.Prm;
import pe.edu.pucp.packetsoft.dao.EnvioDao;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.EnvioRet;



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


    @Transactional
    @Override
    public Envio get(int id) {
        Envio envio = null;
        try {
            envio = entityManager.find(Envio.class, id);
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return envio;
    }

    @Override
    public List<Envio> listOrdenFecha() {
        List<Envio> result = null;
        try{
            var hql = "from Envio as e order by e.fecha_hora";
            result = entityManager.createQuery(hql).getResultList();
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return result;
    }


    @Override
    public List<Envio> listCertainHoursFromDatetime(Prm param) {
        List<Envio> result = null;
        try{
            Calendar calInicio = Calendar.getInstance();
            Calendar calFin = Calendar.getInstance();
            calInicio.set(Calendar.YEAR         ,param.anio);
            calInicio.set(Calendar.MONTH        ,param.mes-1);
            calInicio.set(Calendar.DAY_OF_MONTH ,param.dia);
            calInicio.set(Calendar.HOUR_OF_DAY  ,param.hora);
            calInicio.set(Calendar.MINUTE       ,param.minuto);

            calFin.setTime(calInicio.getTime());
            calFin.add(Calendar.HOUR_OF_DAY , param.horaSimul);
            calFin.add(Calendar.MINUTE      , param.minSimul);

            System.out.println(calInicio.getTime());
            System.out.println(calFin.getTime());
            var hql = "from Envio as e where (e.fecha_hora between :start and :end) order by e.fecha_hora";
            result = entityManager.createQuery(hql)
                .setParameter("start"   , calInicio.getTime()   , TemporalType.DATE)
                .setParameter("end"     , calFin.getTime()      , TemporalType.DATE)
                .getResultList();
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    @Transactional
    @Override
    public EnvioRet insertRet(EnvioRet envio){
        EnvioRet result = null;
        try{
            result = entityManager.merge(envio);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }
}
