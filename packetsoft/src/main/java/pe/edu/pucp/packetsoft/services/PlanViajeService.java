package pe.edu.pucp.packetsoft.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.dao.PlanViajeDao;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.EnvioRet;
import pe.edu.pucp.packetsoft.models.PlanViaje;
import pe.edu.pucp.packetsoft.models.PlanViajeRet;
import pe.edu.pucp.packetsoft.models.VueloUtil;

@Service
public class PlanViajeService {
    
    @Autowired
    private PlanViajeDao daoPlan;

    public PlanViaje insert(PlanViaje plan){
        return daoPlan.insert(plan);
    }

    public List<PlanViaje> getAll(){
        return daoPlan.getAll();
    }

    public PlanViaje get(int id){
        return daoPlan.get(id);
    }

    public List<PlanViaje> listByEnvio(Envio envio){
        return daoPlan.listByEnvio(envio);
    }

    public List<Envio> listDistinctEnvios(){
        return daoPlan.listDistinctEnvios();
    }

    public PlanViaje insertPlan(PlanViaje plan){
        return daoPlan.insertPlan(plan);
    }

    public void deleteAll(){
        daoPlan.deleteAll();
    }

    public void truncTable(){
        daoPlan.truncTable();
    }

    public void insertList(List<PlanViaje>listaPlanes){
        daoPlan.insertList(listaPlanes);
    }

    public PlanViajeRet getByEnvio(EnvioRet envio) throws IOException, ClassNotFoundException{
        PlanViajeRet result = null;
        List<PlanViaje> listaParadas = new ArrayList<>();
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("pdv"));
        try{
            
            Object aux = in.readObject();
            PlanViaje pv;
            int cont = 0;
            while(aux!=null){
                // System.out.print(cont+"|");
                if(cont == 2865){
                    System.err.println("owo");
                }
                if(aux instanceof PlanViaje){
                    pv = (PlanViaje)aux;
                    if(pv.getId_envio_ret().compareTo(envio.getId()) == 0){
                        listaParadas.add(pv);
                    }
                }
                aux = in.readObject();
                cont++;
            }
            
        }catch(IOException | ClassNotFoundException ex){
            System.err.println("Se llego al final del archivo: "+ ex.getMessage());
            in.close();
            List<VueloUtil> itinerario = new ArrayList<VueloUtil>();
            for (PlanViaje planViaje : listaParadas) {
                itinerario.add(planViaje.getVuelo_util());
            }
            PlanViajeRet nuevoPlanViajeRet = new PlanViajeRet();
            nuevoPlanViajeRet.setEnvio(envio);
            nuevoPlanViajeRet.setItinerario(itinerario);
            result = nuevoPlanViajeRet;
        }
        return result;
    }

    public List<VueloUtil> getItinerarioByEnvio(EnvioRet envio) throws IOException, ClassNotFoundException{
        List<VueloUtil>  result = null;
        List<PlanViaje> listaParadas = new ArrayList<>();
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("pdv"));
        try{
            
            Object aux = in.readObject();
            PlanViaje pv;
            int cont = 0;
            while(aux!=null){
                // System.out.print(cont+"|");
                if(cont == 2865){
                    System.err.println("owo");
                }
                if(aux instanceof PlanViaje){
                    pv = (PlanViaje)aux;
                    if(pv.getId_envio_ret().compareTo(envio.getId()) == 0){
                        listaParadas.add(pv);
                    }
                }
                aux = in.readObject();
                cont++;
            }
            
        }catch(IOException | ClassNotFoundException ex){
            System.err.println("Se llego al final del archivo: "+ ex.getMessage());
            in.close();
            List<VueloUtil> itinerario = new ArrayList<VueloUtil>();
            for (PlanViaje planViaje : listaParadas) {
                itinerario.add(planViaje.getVuelo_util());
            }
            result = itinerario;
        }
        return result;
    }
}
