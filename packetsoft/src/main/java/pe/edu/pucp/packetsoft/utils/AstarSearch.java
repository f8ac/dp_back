package pe.edu.pucp.packetsoft.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import pe.edu.pucp.packetsoft.models.Envio;

public class AstarSearch {
    public static AstarNode aStar(AstarNode start, AstarNode target,Envio envio){

        PriorityQueue<AstarNode> closedList = new PriorityQueue<>();
        PriorityQueue<AstarNode> openList = new PriorityQueue<>();
    
        start.f = start.g + start.calculateHeuristic(target,envio.getFecha_hora(),start.neighbors.get(0));
        openList.add(start);
    
        while(!openList.isEmpty()){
            AstarNode n = openList.peek();
            if(n == target){
                return n;
            }
            for(AstarNode.Edge edge : n.neighbors){
                if( edge.vuelo.getCapacidad_utilizada() + envio.getCant_paquetes_total() > edge.vuelo.getCapacidad_total() 
                    || edge.vuelo.getHora_salida().before(envio.getFecha_hora())){
                    System.out.print(">");
                }else{
                    AstarNode m = edge.node;
                    double totalWeight = n.g + edge.weight;
                    if(!openList.contains(m) && !closedList.contains(m)){
                        m.parent = n;
                        m.vuelo = edge.vuelo;
                        m.g = totalWeight;
                        m.f = m.g + m.calculateHeuristic(target,envio.getFecha_hora(),edge);
                        openList.add(m);
                    } else {
                        if(totalWeight < m.g){
                            m.parent = n;
                            m.vuelo = edge.vuelo;
                            m.g = totalWeight;
                            m.f = m.g + m.calculateHeuristic(target,envio.getFecha_hora(),edge);
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

    public static void restaAlmacenamiento(AstarNode target, Envio envio){
        AstarNode n = target;
        if(n==null)
            return;
        while(n.parent != null){
            if(n.vuelo != null){
                int cantidad_actual = n.vuelo.getCapacidad_utilizada();
                n.vuelo.setCapacidad_utilizada(cantidad_actual + envio.getCant_paquetes_total());
                System.out.print("<"+(100 - n.vuelo.getCapacidad_utilizada())+">");
            }
            n = n.parent;
        }
    }
    
    public static void printPath(AstarNode target){
        AstarNode n = target;
    
        if(n==null)
            return;
    
        List<Integer> ids = new ArrayList<>();
    
        while(n.parent != null){
            ids.add(n.id);
            n = n.parent;
        }
        ids.add(n.id);
        Collections.reverse(ids);
        // System.out.println("\n==================================");
        for(int id : ids){
            System.out.print(id + " ");
        }
        // System.out.println("\n==================================");
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
    
        // System.out.println("==================================");
        for (AstarNode astarNode : ids) {
            System.out.print(astarNode.id + "(" + astarNode.h +")");
        }
        // System.out.println("==================================");
        System.out.println("");
    }

    public static int compareTimes(Date d1, Date d2){
        int t1;
        int t2;

        t1 = (int) (d1.getTime() % (24*60*60*1000L));
        t2 = (int) (d2.getTime() % (24*60*60*1000L));
        return (t1 - t2);
    }
}
