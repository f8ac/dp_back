package pe.edu.pucp.packetsoft.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private int itEtapaLocal;
    private int itEtapaInten;
    private int itEtapaDiver;
    // private List<Aeropuerto> aeropuertos;
    private int tamanioMatriz;
    private int[][] matrizR;
    private int[][] matrizF;
    private int cantMovFrecuentes = 10;
    private int tenenciaMin = 5;
    private int tenenciaMax = 10;

    public void ejecutarAlgoritmo(int id1, int id2, int id3) {

        this.itEtapaLocal = id1;
        this.itEtapaInten = id2;
        this.itEtapaDiver = id3;

        List<Aeropuerto> aeropuertos = aeropuertoService.getAll();
        tamanioMatriz = aeropuertos.size();
        matrizR = new int[aeropuertos.size()][aeropuertos.size()];
        matrizF = new int[aeropuertos.size()][aeropuertos.size()];

        List<Vuelo> mejorSolucion = new ArrayList<>();

        List<Aeropuerto> solucionInicial = new ArrayList<Aeropuerto>();
        solucionInicial.add(aeropuertos.get(2));
        solucionInicial.add(aeropuertos.get(1));
        solucionInicial.add(aeropuertos.get(3));

        //Vuelos
        List<Vuelo> soluIniVuelo = new ArrayList<Vuelo>();
        soluIniVuelo.add(vueloService.get(2));
        soluIniVuelo.add(vueloService.get(5));

        //Envio
        Envio envio = envioService.get(1);

        // empieza el algoritmo
        mejorSolucion.addAll(soluIniVuelo);
        for (int i = 0; i < this.itEtapaLocal; i++) { // primer for itEtapaLocal
            movimiento(matrizR, matrizF, mejorSolucion, solucionInicial, soluIniVuelo);
        }

        matrizR = new int[aeropuertos.size()][aeropuertos.size()];
        for (int i = 0; i < this.itEtapaInten; i++) { // segundo for itEtapaInten

        }
        matrizR = new int[aeropuertos.size()][aeropuertos.size()];
        penalizarMovimientos(matrizR, matrizF, cantMovFrecuentes);

        for (int i = 0; i < this.itEtapaDiver; i++) { // tercer for itEtapaDiver

        }
    }

    void movimiento(int[][] matrizR, int[][] matrizF, List<Vuelo> mejorSolucion,
            List<Aeropuerto> solucionInicial, List<Vuelo> soluInicialVuelo) {
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

        int totalVecinos = calcularTotalVecinos(aeropuertosMatriz);

        boolean banderaMov = false;

        List<Aeropuerto> solucionAux = new ArrayList<Aeropuerto>();

        List<Vuelo> solucionAuxVuelo = new ArrayList<Vuelo>();

        List<Aeropuerto> mejorMov = new ArrayList<Aeropuerto>();
        List<Vuelo> mejorMovVuelo = new ArrayList<Vuelo>();

        int mejori=0;

        for (int i = 0; i < totalVecinos; i++) {
            banderaMov = false;

            solucionAux.clear();
            solucionAux.addAll(solucionInicial);

            solucionAuxVuelo.clear();
            solucionAuxVuelo.addAll(soluInicialVuelo);

            if(matrizR[i][i]==0){
                boolean valido = validarInsercion(i,aeropuertosMatriz,solucionAux,solucionAuxVuelo);
                if(valido){
                    banderaMov=true;
                }
                if(banderaMov){
                    if(mejorMov.isEmpty() && mejorMovVuelo.isEmpty()){
                        mejorMov.addAll(solucionAux);
                        mejori=i;
                    }else{
                        if(fitness(solucionAuxVuelo)>fitness(mejorMovVuelo)){
                            mejorMov.clear();
                            mejorMov.addAll(solucionAux);
                            mejori=i;
                        }
                    }
                }
            }
            if(matrizR[i][i]>0){
                matrizR[i][i]--;
            }
        }

        if(!(mejorMov.isEmpty() &&  mejorMovVuelo.isEmpty())){
            solucionInicial.addAll(mejorMov);
            soluInicialVuelo.addAll(mejorMovVuelo);
            matrizR[mejori][mejori]+=5;
            matrizF[mejori][mejori]+=1;
            if(fitness(soluInicialVuelo)>fitness(mejorSolucion)){
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

    int calcularTotalVecinos(List<List <Aeropuerto>> vecinos){
        int total=0;
        for (int i=0;i<vecinos.size();i++){
            total+=vecinos.get(i).size();
        }
        return total;
    }

    boolean validarInsercion(int i,List<List <Aeropuerto>> vecinos,List<Aeropuerto> soluAux,List<Vuelo> soluAuxVuelo){

        return true;
    }

    double fitness(List<Vuelo> solu){

        return 2.0;
    }
}
