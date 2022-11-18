package pe.edu.pucp.packetsoft.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.Paquete;
import pe.edu.pucp.packetsoft.models.Vuelo;
import pe.edu.pucp.packetsoft.models.VueloUtil;

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
    public VueloUtil vuelo; // solamente usado para la ruta resultado
    public List<Paquete> inventario;

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

        Edge(int weight, AstarNode node, VueloUtil vuelo){
            this.weight = weight;
            this.node = node;
            this.vuelo = vuelo;
        }

        public int weight;
        public AstarNode node;
        public VueloUtil vuelo;
    }

    public void addBranch(int weight, AstarNode node){
        Edge newEdge = new Edge(weight, node);
        neighbors.add(newEdge);
    }

    public void addBranch(int weight, AstarNode node, VueloUtil vuelo){
        Edge newEdge = new Edge(weight, node, vuelo);
        neighbors.add(newEdge);
    }

    public double calculateHeuristic(AstarNode target){
        
        return this.h;
    }

    public double calculateHeuristic(AstarNode target, Envio envio,Edge edge){
        int tiempoRestante  = compareTimes(edge.vuelo.getVuelo().getHora_salida(),envio.getFecha_hora());
        // this.h = (double)tiempoRestante;
        if(tiempoRestante < 0){
            this.h = Double.MAX_VALUE;
            // System.out.print(".");
        }
        // else if(fueraDePlazo(edge.vuelo,envio)){
        //     this.h = Double.MAX_VALUE;
        // }
        else{
            if(edge.vuelo.getVuelo().getCapacidad_total() == edge.vuelo.getVuelo().getCapacidad_utilizada()){
                System.err.println("El vuelo ya no admite más paquetes");
                this.h = Double.MAX_VALUE;
            }else{
                this.h = tiempoRestante;
                // System.out.print("*");
            }
            
        }
        return this.h;
    }

    public static Boolean fueraDePlazo(Vuelo vuelo,Envio envio){
        Boolean result = false;
        try{
            int horasAgregadas;
            if(envio.getIntercontinental())
                horasAgregadas = 48;
            else
                horasAgregadas = 24;
            if(vuelo.getHora_llegada().after(addHoursToDate(envio.getFecha_hora(),horasAgregadas))){
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

    public int compareTimes(Date d1, Date d2){
        int t1;
        int t2;

        t1 = (int) (d1.getTime() % (24*60*60*1000L));
        t2 = (int) (d2.getTime() % (24*60*60*1000L));
        return (t1 - t2);
    }
}
