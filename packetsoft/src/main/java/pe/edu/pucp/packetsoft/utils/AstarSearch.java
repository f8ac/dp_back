package pe.edu.pucp.packetsoft.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.Movimiento;
// import pe.edu.pucp.packetsoft.models.Vuelo;
import pe.edu.pucp.packetsoft.models.VueloRet;
import pe.edu.pucp.packetsoft.models.VueloUtil;

public class AstarSearch {
    public static AstarNode aStar(AstarNode start, AstarNode target,Envio envio){
        try{
            PriorityQueue<AstarNode> closedList = new PriorityQueue<>();
            PriorityQueue<AstarNode> openList = new PriorityQueue<>();
        
            start.f = start.g + start.calculateHeuristic(target,envio,start.neighbors.get(0));
            openList.add(start);
        
            while(!openList.isEmpty()){
                AstarNode n = openList.peek();
                if(n == target){
                    // System.out.print("$");
                    return n;
                }
                for(AstarNode.Edge edge : n.neighbors){
                    // if(edge.vuelo.getVuelo().getId() == 4741){
                    //     int i = 0;
                    // }
                    if( edge.vuelo.getCap_util_real() + envio.getCant_paquetes_total() > edge.vuelo.getCap_tot_real() 
                        || edge.vuelo.getSalida_real().before(envio.getFecha_hora())){
                        // System.out.println("Salida de vuelo vs registro envio:" + edge.vuelo.getHora_salida() + envio.getFecha_hora());
                    }else{
                        AstarNode m = edge.node;
                        double totalWeight = n.g + edge.weight;
                        if(!openList.contains(m) && !closedList.contains(m)){
                            m.parent = n;
                            m.vuelo = edge.vuelo;
                            m.g = totalWeight;
                            m.f = m.g + m.calculateHeuristic(target,envio,edge);
                            openList.add(m);
                        } else {
                            if(totalWeight < m.g){
                                m.parent = n;
                                m.vuelo = edge.vuelo;
                                m.g = totalWeight;
                                m.f = m.g + m.calculateHeuristic(target,envio,edge);
                                if(closedList.contains(m)){
                                    closedList.remove(m);
                                    openList.add(m);
                                }
                            }
                        }
                    }
                }
                openList.remove(n);
                closedList.add(n);
            }
            
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public static Boolean restaAlmacenamiento(AstarNode target, Envio envio, List<VueloRet> listaVuelos, PriorityQueue<Movimiento> colaPaquetes){
        AstarNode n = target;
        if(n==null)
            return false;

        int capAeroRegistro = envio.getAero_origen().getCapacidad_utilizado();
        envio.getAero_origen().setCapacidad_utilizado(capAeroRegistro + envio.getCant_paquetes_total());
        while(n.parent != null){
            if(n.vuelo != null){
                int cantidad_actual = n.vuelo.getCap_util_real();
                n.vuelo.setCap_util_real(cantidad_actual + envio.getCant_paquetes_total());
                // int idxVuelo = searchFlightReturnIndex( n.vuelo, listaVuelos);
                // listaVuelos.get(idxVuelo).getInventario().add(envio);

                // se tiene que guardar la informacion del envio, su entrada y su salida
                // de un aeropuerto en una cola, cuando el cronometro llegue a la hora
                // correspondiente a la cabeza de la cola, se suma o resta el almacenamiento
                // del aeropuerto respectivo.

                //creamos un movimiento de entrada y otro de salida
                Movimiento movEnt = new Movimiento();
                Movimiento movSal = new Movimiento();
                
                movEnt.setAeropuerto(n.vuelo.getVuelo().getAeropuerto_llegada());
                movEnt.setEnvio(envio);
                movEnt.setOperacion(-1);
                movEnt.setFecha(n.vuelo.getLlegada_real());

                movSal.setAeropuerto(n.vuelo.getVuelo().getAeropuerto_salida());
                movSal.setEnvio(envio);
                movSal.setOperacion(1);
                movSal.setFecha(n.vuelo.getSalida_real());
                
                colaPaquetes.add(movEnt);
                colaPaquetes.add(movSal);


                if(colapso(n,envio)){
                    System.err.println("COLAPSO: el paquete no ha llegado a tiempo.");
                    return true;
                }
            }
            n = n.parent;
        }
        return false;
    }

    public static Boolean colapso(AstarNode nodo,Envio envio){
        Boolean result = false;
        try{
            int horasAgregadas;
            if(envio.getIntercontinental()){
                horasAgregadas = 48;
            }else{
                horasAgregadas = 24;
            }
            if(nodo.vuelo.getLlegada_real().after(addHoursToDate(envio.getFecha_hora(),horasAgregadas))){
                System.out.println("\nHora Limite: "+ addHoursToDate(envio.getFecha_hora(),horasAgregadas));
                System.out.println("Hora de llegada: "+nodo.vuelo.getLlegada_real());
                return true;
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public static Date addHoursToDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }
    
    public static void printPath(AstarNode target){
        AstarNode n = target;
        if(n==null)
            return;
        List<AstarNode> flights = new ArrayList<>();
        while(n.parent != null){
            flights.add(n);
            n = n.parent;
        }
        flights.add(n);
        Collections.reverse(flights);
        for(AstarNode flight : flights){
            if(flight.vuelo != null){
                System.out.print(" ["+flight.vuelo.getId_aux()+"] ");
            }
            System.out.print(flight.aeropuerto.getId());
        }
        System.out.println("");
    }

    public static void printNewPath(AstarNode target){
        AstarNode n = target;
        if(n==null)
            return;
        List<AstarNode> ids = new ArrayList<AstarNode>();
        while(n.parent != null){
            ids.add(n);
            n = n.parent;
        }
        ids.add(n);
        Collections.reverse(ids);
        for (AstarNode astarNode : ids) {
            System.out.print(astarNode.id + "(" + astarNode.h +")");
        }
        System.out.println("");
    }

    public static int compareTimes(Date d1, Date d2){
        int t1;
        int t2;
        t1 = (int) (d1.getTime() % (24*60*60*1000L));
        t2 = (int) (d2.getTime() % (24*60*60*1000L));
        return (t1 - t2);
    }

    public static void clearParents(List<AstarNode> lista){
        for (AstarNode astarNode : lista) {
            astarNode.parent = null;
            astarNode.vuelo = null;
        }
    }

    public static List<Integer> generaListaIDS(AstarNode target){
        AstarNode n = target;
        if(n==null)
            return null;
        List<Integer> ids = new ArrayList<>();
        while(n.parent != null){
            ids.add(n.id);
            n = n.parent;
        }
        ids.add(n.id);
        Collections.reverse(ids);
        return ids;
    }

    static int searchFlightReturnIndex(VueloUtil vuelo, List<VueloRet> listaVuelosRet){
        int result = -1;
        try{
            int idVuelo = vuelo.getId_aux(), index = 0;
            for (VueloRet vueloRet : listaVuelosRet) {
                if(idVuelo == vueloRet.getVuelo_util().getId_aux()){
                    return index;
                }
                index++;
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }
}
