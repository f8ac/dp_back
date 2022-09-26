package pe.edu.pucp.packetsoft.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pe.edu.pucp.packetsoft.models.Aeropuerto;

@Getter @Setter
public class AstarNode implements Comparable<AstarNode> {
    // Id for readability of result purposes
    private static int idCounter = 0;
    public int id;

    // Parent in the path
    public AstarNode parent = null;

    public List<Edge> neighbors;

    // Evaluation functions
    public double f = Double.MAX_VALUE;
    public double g = Double.MAX_VALUE;
    // Hardcoded heuristic
    public double h; 

    public Aeropuerto aeropuerto;

    public AstarNode(double h){
        this.h = h;
        this.id = idCounter++;
        this.neighbors = new ArrayList<>();
    }

    @Override
    public int compareTo(AstarNode n) {
          return Double.compare(this.f, n.f);
    }

    public static class Edge {
        Edge(int weight, AstarNode node){
            this.weight = weight;
            this.node = node;
        }

        Edge(int weight, AstarNode node, Date salida, Date llegada){
            this.weight = weight;
            this.node = node;
            this.horaSalida = salida;
            this.horaLlegada = llegada;
        }

        public int weight;
        public AstarNode node;
        public Date horaSalida;
        public Date horaLlegada;
    }

    public void addBranch(int weight, AstarNode node){
        Edge newEdge = new Edge(weight, node);
        neighbors.add(newEdge);
    }

    public void addBranch(int weight, AstarNode node, Date salida, Date llegada){
        Edge newEdge = new Edge(weight, node, salida, llegada);
        neighbors.add(newEdge);
    }

    public double calculateHeuristic(AstarNode target){
        
        return this.h;
    }

    public double calculateHeuristic(AstarNode target, Date time,Edge edge){
        if(compareTimes(time, edge.horaSalida) > 0){
            return Double.MAX_VALUE; //no considerar este camino
        }
        return this.h;
    }

    public int compareTimes(Date d1, Date d2){
        int t1;
        int t2;

        t1 = (int) (d1.getTime() % (24*60*60*1000L));
        t2 = (int) (d2.getTime() % (24*60*60*1000L));
        return (t1 - t2);
    }
}
