package pe.edu.pucp.packetsoft.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

public class AstarSearch {
    public static AstarNode aStar(AstarNode start, AstarNode target,Date time){

        PriorityQueue<AstarNode> closedList = new PriorityQueue<>();
        PriorityQueue<AstarNode> openList = new PriorityQueue<>();
    
        start.f = start.g + start.calculateHeuristic(target,time,start.neighbors.get(0));
        openList.add(start);
    
        while(!openList.isEmpty()){
            AstarNode n = openList.peek();
            if(n == target){
                return n;
            }
    
            for(AstarNode.Edge edge : n.neighbors){
                AstarNode m = edge.node;
                double totalWeight = n.g + edge.weight;
    
                if(!openList.contains(m) && !closedList.contains(m)){
                    m.parent = n;
                    m.g = totalWeight;
                    m.f = m.g + m.calculateHeuristic(target,time,edge);
                    openList.add(m);
                } else {
                    if(totalWeight < m.g){
                        m.parent = n;
                        m.g = totalWeight;
                        m.f = m.g + m.calculateHeuristic(target,time,edge);
    
                        if(closedList.contains(m)){
                            closedList.remove(m);
                            openList.add(m);
                        }
                    }
                }
            }
    
            openList.remove(n);
            closedList.add(n);
        }
        return null;
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
    
        for(int id : ids){
            System.out.print(id + " ");
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
}
