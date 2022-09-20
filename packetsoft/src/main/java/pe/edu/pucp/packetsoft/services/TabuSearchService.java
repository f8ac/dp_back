package pe.edu.pucp.packetsoft.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.utils.tabuSearch.TabuSearch;

@Service
public class TabuSearchService {
    @Autowired
    private AeropuertoService aeropuertoService;
    private int itEtapaLocal;
    private int itEtapaInten;
    private int itEtapaDiver;
    private List<Aeropuerto> aeropuertos;
    private int [][] matrizR;
    private int [][] matrizF;

    public void ejecutarAlgoritmo(int id1, int id2, int id3){
        this.itEtapaLocal = id1;
        this.itEtapaInten = id2;
        this.itEtapaDiver = id3;

        //TabuSearch tabuSearch = new TabuSearch(20,20,20);
        //tabuSearch.tabuSearchImplementation(); 
        List<Aeropuerto> aeropuertos = aeropuertoService.getAll();
        matrizR = new int [aeropuertos.size()][aeropuertos.size()];
        matrizF = new int [aeropuertos.size()][aeropuertos.size()];




        // matriz R ya esta
        // matriz F ya esta
        // solucion inicial falta

        for(int i = 0; i < this.itEtapaLocal; i++) { // primer for itEtapaLocal


        }


        for(int i = 0; i < this.itEtapaInten; i++) { // segundo for itEtapaInten



        }


        for(int i = 0; i < this.itEtapaDiver; i++) { // tercer for itEtapaDiver



        }
    }
}
