package pe.edu.pucp.packetsoft.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.VueloRet;

public class AstarSearch {
    public static AstarNode aStar(AstarNode start, AstarNode target,Envio envio){

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
                if( edge.vuelo.getCapacidad_utilizada() + envio.getCant_paquetes_total() > edge.vuelo.getCapacidad_total() 
                    || edge.vuelo.getHora_salida().before(envio.getFecha_hora())){
                    // System.out.print(">");
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
        return null;
    }

    public static Boolean restaAlmacenamiento(AstarNode target, Envio envio, List<VueloRet> listaVuelos){
        AstarNode n = target;
        if(n==null)
            return false;
        while(n.parent != null){
            if(n.vuelo != null){
                int cantidad_actual = n.vuelo.getCapacidad_utilizada();
                n.vuelo.setCapacidad_utilizada(cantidad_actual + envio.getCant_paquetes_total());
                // elegir de la lista al vueloRet correspondiente
                // agregar el envio al inventario
                listaVuelos.get(n.vuelo.getId()-1).getInventario().add(envio);
                
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
            if(nodo.vuelo.getHora_llegada().after(addHoursToDate(envio.getFecha_hora(),horasAgregadas))){
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
                System.out.print(" ["+flight.vuelo.getId()+"] ");
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
}
