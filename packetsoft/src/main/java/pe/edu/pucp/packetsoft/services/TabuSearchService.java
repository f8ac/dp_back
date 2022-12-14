package pe.edu.pucp.packetsoft.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.Vuelo;

@Service
public class TabuSearchService {
    @Autowired
    private AeropuertoService aeropuertoService;
    @Autowired
    private VueloService vueloService;
    @Autowired
    private EnvioService envioService;
    // @Autowired
    // private AstarService astarService;

    private int itEtapaLocal;
    // private int itEtapaInten;
    // private int itEtapaDiver;
    // private List<Aeropuerto> aeropuertos;
    private int tamanioMatriz;
    private int[][] matrizR;
    private int[][] matrizF;
    // private int cantMovFrecuentes = 10;
    // private int tenenciaMin = 5;
    // private int tenenciaMax = 10;

    private int posA;
    private int posV;

    public void ejecutarAlgoritmo(int id1, int id2, int id3) {

        this.itEtapaLocal = id1;
        // this.itEtapaInten = id2;
        // this.itEtapaDiver = id3;

        List<Aeropuerto> aeropuertos = aeropuertoService.getAll();
        tamanioMatriz = aeropuertos.size();
        matrizR = new int[aeropuertos.size()][aeropuertos.size()];
        matrizF = new int[aeropuertos.size()][aeropuertos.size()];

        List<Vuelo> mejorSolucion = new ArrayList<>();

        //astarService.pruebaUnica();

        List<Aeropuerto> solucionInicial = new ArrayList<Aeropuerto>();
        //solucionInicial.add(aeropuertos.get(2));
        //solucionInicial.add(aeropuertos.get(1));

        //Vuelos
        List<Vuelo> soluIniVuelo = new ArrayList<Vuelo>();
        //soluIniVuelo.add(vueloService.get(2));

        //Envio
        Envio envio = envioService.get(11347);

        List<Integer> idsVuelos = null;

        // idsVuelos=astarService.pruebaUnica(envio);
        idsVuelos = new ArrayList<>();

        for(int i=0;i<idsVuelos.size();i++){
            soluIniVuelo.add(vueloService.get(idsVuelos.get(i)));
        }

        for(int i=0;i<idsVuelos.size();i++){
            if(i==0){
                solucionInicial.add(soluIniVuelo.get(i).getAeropuerto_salida());
                solucionInicial.add(soluIniVuelo.get(i).getAeropuerto_llegada());
            }else{
                solucionInicial.add(soluIniVuelo.get(i).getAeropuerto_llegada());
            }
        }

        // empieza el algoritmo
        mejorSolucion.addAll(soluIniVuelo);
        for (int i = 0; i < this.itEtapaLocal; i++) { // primer for itEtapaLocal
            movimiento(matrizR, matrizF, mejorSolucion, solucionInicial, soluIniVuelo,envio);
        }

        /*for(int i=0;i<mejorSolucion.size();i++){
            System.out.print(mejorSolucion.get(i).getId()+"->");
        }*/
            

        /*matrizR = new int[aeropuertos.size()][aeropuertos.size()];
        for (int i = 0; i < this.itEtapaInten; i++) { // segundo for itEtapaInten

        }
        matrizR = new int[aeropuertos.size()][aeropuertos.size()];
        penalizarMovimientos(matrizR, matrizF, cantMovFrecuentes);
        for (int i = 0; i < this.itEtapaDiver; i++) { // tercer for itEtapaDiver

        }*/
    }

    void movimiento(int[][] matrizR, int[][] matrizF, List<Vuelo> mejorSolucion,
            List<Aeropuerto> solucionInicial, List<Vuelo> soluInicialVuelo,Envio envio) {
        /*
         * 
         * solucionInicial <- (A1,A2,3,4,A5)
         * 
         */

        List<Aeropuerto> aeropuertosVecinos;
        List<List<Aeropuerto>> aeropuertosMatriz = new ArrayList<List<Aeropuerto>>();

        //Lista de ids de solucion inicial
        List<Integer> solucionInicialId = new ArrayList<Integer>();
        for (int i=0;i<solucionInicial.size();i++){
            solucionInicialId.add(solucionInicial.get(i).getId());
        }

        for (int i = 0; i < solucionInicial.size()-1; i++) {
            aeropuertosVecinos = buscarVecinos(solucionInicial.get(i),solucionInicialId);
            aeropuertosMatriz.add(aeropuertosVecinos);
        }

        aeropuertosMatriz.add(obtenerlistVecinosLlegada(solucionInicial.get(solucionInicial.size()-1),solucionInicialId));

        List<Integer> cantidad = new ArrayList<Integer>();
 
        int totalVecinos = calcularTotalVecinos(aeropuertosMatriz,cantidad);

        boolean banderaMov = false;

        List<Aeropuerto> solucionAux = new ArrayList<Aeropuerto>();

        List<Vuelo> solucionAuxVuelo = new ArrayList<Vuelo>();

        List<Aeropuerto> mejorMov = new ArrayList<Aeropuerto>();
        List<Vuelo> mejorMovVuelo = new ArrayList<Vuelo>();

        int mejori=0;

        posA=-1;
        posV=-1;

        int j;

        for (int i = 0; i < totalVecinos; i++) {
            banderaMov = false;

            solucionAux.clear();
            solucionAux.addAll(solucionInicial);

            solucionAuxVuelo.clear();
            solucionAuxVuelo.addAll(soluInicialVuelo);

            hallarAeropuerto(i,cantidad);
            j = aeropuertosMatriz.get(posA).get(posV).getId();


            if(matrizR[j-1][j-1]==0){
                boolean valido = validarInsercion(i,aeropuertosMatriz,cantidad,solucionAux,solucionAuxVuelo,envio);
                if(valido){
                    banderaMov=true;
                }
                if(banderaMov){
                    if(mejorMov.isEmpty() && mejorMovVuelo.isEmpty()){
                        mejorMov.addAll(solucionAux);
                        mejori=j-1;
                    }else{
                        if(fitness(solucionAuxVuelo)<fitness(mejorMovVuelo)){
                            mejorMov.clear();
                            mejorMov.addAll(solucionAux);
                            mejori=j-1;
                        }
                    }
                }
            }
            if(matrizR[j-1][j-1]>0){
                matrizR[j-1][j-1]--;
            }
        }

        if(!(mejorMov.isEmpty() &&  mejorMovVuelo.isEmpty())){
            //solucionInicial.clear();
            //solucionInicial.addAll(mejorMov);
            //soluInicialVuelo.clear();
            //soluInicialVuelo.addAll(mejorMovVuelo);
            matrizR[mejori][mejori]+=5;
            matrizF[mejori][mejori]+=1;
            if(fitness(mejorMovVuelo)<fitness(mejorSolucion)){
                mejorSolucion.clear();
                mejorSolucion.addAll(soluInicialVuelo);
            }
        }
        
    }

    List<Aeropuerto> buscarVecinos(Aeropuerto aeropuerto,List<Integer> aeropuertosId) {
        List<Vuelo> vuelos = vueloService.listVecinos(aeropuerto,aeropuertosId);
        List<Aeropuerto> aeropuertos = new ArrayList<>();
        for (int i = 0; i < vuelos.size(); i++) {
            aeropuertos.add(vuelos.get(i).getAeropuerto_llegada());

        }
        List<Aeropuerto> aeropuertosWithoutDuplicates = new ArrayList<>(new HashSet<>(aeropuertos));
        return aeropuertosWithoutDuplicates;
        
    }

    List<Aeropuerto> obtenerlistVecinosLlegada(Aeropuerto aeropuerto,List<Integer> aeropuertosId) {
        List<Vuelo> vuelos = vueloService.listVecinosLlegada(aeropuerto,aeropuertosId);
        List<Aeropuerto> aeropuertos = new ArrayList<>();
        for (int i = 0; i < vuelos.size(); i++) {
            aeropuertos.add(vuelos.get(i).getAeropuerto_salida());

        }
        List<Aeropuerto> aeropuertosWithoutDuplicates = new ArrayList<>(new HashSet<>(aeropuertos));
        return aeropuertosWithoutDuplicates;
    }

    void penalizarMovimientos(int[][] matrizR, int[][] matrizF, int cantMovFrecuentes) {
        int cantMovPenalizados = 5;
        for (int i = 0; i < this.tamanioMatriz; i++) {
            for (int j = 0; j < this.tamanioMatriz; j++) {
                if (matrizF[i][j] >= cantMovFrecuentes) {
                    matrizR[i][j] = cantMovPenalizados;
                }
            }
        }

    }

    int calcularTotalVecinos(List<List <Aeropuerto>> vecinos, List<Integer> cantidad){
        int total=0;
        for (int i=0;i<vecinos.size();i++){
            total+=vecinos.get(i).size();
            cantidad.add(vecinos.get(i).size());
        }
        return total;
    }

    boolean validarInsercion(int i,List<List <Aeropuerto>> vecinos,List<Integer> cant,List<Aeropuerto> soluAux,List<Vuelo> soluAuxVuelo,Envio envio){
        boolean valido=false;
        posA=-1;
        posV=-1;
        int cantAeropSolAux = soluAux.size();
        hallarAeropuerto(i,cant);
        Vuelo vuelo1 = null,vuelo2=null;
        List<Vuelo> vuelos;
        //System.out.print("\n"+posA+" "+posV+" "+i+"esto");
        if(posA!=-1 && posV!=-1){

            if(posA==cantAeropSolAux-1){
                posA=posA-1;
            }
            soluAux.add(posA+1,vecinos.get(posA).get(posV));
            vuelos = buscarVuelos(posA+1,cantAeropSolAux,soluAux,soluAuxVuelo,vuelo1,vuelo2,envio);
            vuelo1 = vuelos.get(0);
            vuelo2 = vuelos.get(1);
            if(vuelo1!=null && vuelo2!=null){
                /*if(posA==cantAeropSolAux-1){
                    soluAuxVuelo.remove(posA-1);
                    soluAuxVuelo.add(posA-1,vuelo1);
                    soluAuxVuelo.add(posA,vuelo2);
                }else{*/
                soluAuxVuelo.remove(posA);
                soluAuxVuelo.add(posA,vuelo1);
                soluAuxVuelo.add(posA+1,vuelo2);
                
                valido=true;
            }
        }
        return valido;
    }

    double fitness(List<Vuelo> solu){
        int size = solu.size();
        int tiempo_vuelo=0;
        for(int i=0;i<size;i++){
            tiempo_vuelo+=solu.get(i).getTiempo_vuelo_minutos();
        }
        if(size==2){
            tiempo_vuelo*=3;
        }
        return tiempo_vuelo;
    }

    void hallarAeropuerto(int num,List<Integer> cant){
        int total=0,totalA=0;
        for(int i=0;i<cant.size();i++){
            total += cant.get(i);
            if(total>num){
                posA=i;
                if(i==0){
                    posV=num;
                }else{
                    posV=num-totalA;
                }
                break;
            }
            totalA=total;
        }
    }

    List<Vuelo> buscarVuelos(int pos,int total,List<Aeropuerto> soluAux,List<Vuelo> soluAuxVuelo,Vuelo vuelo1,Vuelo vuelo2,Envio envio){
        Calendar horasalida1 = Calendar.getInstance();
        Calendar horasalida2 = Calendar.getInstance();
        Calendar horallegada2 = Calendar.getInstance();
        List<Vuelo> vuelos=new ArrayList<Vuelo>();
        if(pos==1){
            horasalida1.setTime(envio.getFecha_hora());
        }else{
            horasalida1 = Calendar.getInstance();
            horasalida1.setTime(soluAuxVuelo.get(pos-2).getHora_llegada());
            horasalida1.add(Calendar.HOUR_OF_DAY, 1);
        }
        // vuelo1 = vueloService.buscarVuelo1(soluAux.get(pos-1).getId(),soluAux.get(pos).getId(),horasalida1,envio.getCant_paquetes_total());
        vuelos.add(vuelo1);
        if(vuelo1 != null){

            horasalida2.setTime(vuelo1.getHora_llegada());
            horasalida2.add(Calendar.HOUR_OF_DAY, 1);
            
            if(pos+1==total){
                // vuelo2 = vueloService.buscarVuelo1(soluAux.get(pos).getId(),soluAux.get(pos+1).getId(),horasalida2,envio.getCant_paquetes_total());
            }else{
                horallegada2.setTime(soluAuxVuelo.get(pos).getHora_salida());
                horallegada2.add(Calendar.HOUR_OF_DAY, -1);
                // vuelo2 = vueloService.buscarVuelo2(soluAux.get(pos).getId(),soluAux.get(pos+1).getId(),horasalida2,horallegada2,envio.getCant_paquetes_total());
            }
        }
        vuelos.add(vuelo2);
        return vuelos;
    }


    public void pruebaAlgoritmo(int id1, int id2, int id3) {
        StopWatch watch = new StopWatch();
        watch.start();
        for(int i=0;i<10;i++){
            ejecutarAlgoritmo(id1,id2,id3);
        }
        watch.stop();
        System.out.print("Tiempo total para procesar" +watch.getTotalTimeMillis()+" milisegundos.");
    }
}
