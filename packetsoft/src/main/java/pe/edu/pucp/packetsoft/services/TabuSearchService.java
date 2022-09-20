package pe.edu.pucp.packetsoft.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Vuelo;

@Service
public class TabuSearchService {
    @Autowired
    private AeropuertoService aeropuertoService;
    @Autowired
    private VueloService vueloService;

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

        // mejorSolucion y solucion inicial
        List<Aeropuerto> mejorSolucion = new ArrayList<>();
        List<Aeropuerto> solucionInicial = new ArrayList<>();
        solucionInicial.add(aeropuertos.get(2));// SVMI-SEQM
        solucionInicial.add(aeropuertos.get(1));// SEQM -
        solucionInicial.add(aeropuertos.get(3)); // SBBR -
        // solucionInicial.add(aeropuertos.get(7));

        //

        // empieza el algoritmo
        mejorSolucion.addAll(solucionInicial);
        for (int i = 0; i < this.itEtapaLocal; i++) { // primer for itEtapaLocal
            movimiento(matrizR, matrizF, mejorSolucion, solucionInicial);
        }
        matrizR = new int[aeropuertos.size()][aeropuertos.size()];

        for (int i = 0; i < this.itEtapaInten; i++) { // segundo for itEtapaInten

        }
        matrizR = new int[aeropuertos.size()][aeropuertos.size()];
        penalizarMovimientos(matrizR, matrizF, cantMovFrecuentes);

        for (int i = 0; i < this.itEtapaDiver; i++) { // tercer for itEtapaDiver

        }
    }

    void movimiento(int[][] matrizR, int[][] matrizF, List<Aeropuerto> mejorSolucion,
            List<Aeropuerto> solucionInicial) {
        /*
         * 
         * solucionInicial <- (A1,A2,3,4,A5)
         * 
         */
        List<Aeropuerto> aeropuertosVecinos;
        List<List<Aeropuerto>> aeropuertosMatriz = new ArrayList<List<Aeropuerto>>();

        for (int i = 0; i < solucionInicial.size()-1; i++) {
            aeropuertosVecinos = buscarVecinos(solucionInicial.get(i));
            aeropuertosMatriz.add(aeropuertosVecinos);
            
        }
        aeropuertosMatriz.add(obtenerlistVecinosLlegada(solucionInicial.get(solucionInicial.size()-1 ) ));

        int totalVecinos = calcularTotalVecinos(aeropuertosMatriz);
        boolean banderaMov = false;
        List<Aeropuerto> solucionAux = new ArrayList<Aeropuerto>();
        solucionAux.addAll(solucionInicial);
        List<Aeropuerto> mejorMov = new ArrayList<Aeropuerto>();

        for (int i = 0; i < totalVecinos; i++) {
            for (int j = 0; j < totalVecinos; j++) {
               if(matrizR[i][j]==0){
                    if(i==j){
                        boolean valido = validarInsercion(i,aeropuertosMatriz,solucionAux);
                        if(valido){
                            banderaMov=true;
                        }
                    }else{

                    }
               }
               if(matrizR[i][j]>0){
                    matrizR[i][j]--;
               }
            }
        }
    }

    List<Aeropuerto> buscarVecinos(Aeropuerto aeropuerto) {
        List<Vuelo> vuelos = vueloService.listVecinos(aeropuerto);
        List<Aeropuerto> aeropuertos = new ArrayList<>();
        for (int i = 0; i < vuelos.size(); i++) {
            aeropuertos.add(vuelos.get(i).getAeropuerto_llegada());

        }
        List<Aeropuerto> aeropuertosWithoutDuplicates = new ArrayList<>(new HashSet<>(aeropuertos));
        return aeropuertosWithoutDuplicates;
        
    }

    List<Aeropuerto> obtenerlistVecinosLlegada(Aeropuerto aeropuerto) {
        List<Vuelo> vuelos = vueloService.listVecinosLlegada(aeropuerto);
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

}
