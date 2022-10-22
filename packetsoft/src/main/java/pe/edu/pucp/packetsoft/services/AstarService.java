package pe.edu.pucp.packetsoft.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.Vuelo;
import pe.edu.pucp.packetsoft.utils.AstarNode;
import pe.edu.pucp.packetsoft.utils.AstarSearch;

@Service
public class AstarService {


    AeropuertoService aeropuertoService;

    VueloService vueloService;

    public List<Integer> pruebaUnica(Envio envioPrueba){
            List<Integer> result = null;
            try{
                // mapeo
                List<Aeropuerto> listaAeropuertos = aeropuertoService.getAll();
                List<AstarNode> listaNodos = Arrays.asList(new AstarNode[listaAeropuertos.size()]);
                int i = 0;
                for (Aeropuerto aeropuerto : listaAeropuertos) {
                    AstarNode nodo = new AstarNode(0);
                    nodo.setAeropuerto(aeropuerto);
                    listaNodos.set(i,nodo);
                    i++;
                }
                // SE INSERTAN LOS COSTOS DE LOS VERTICES EN ORDEN DE LLEGADA
                List<Vuelo> listaVuelos = vueloService.getAll();
                for (Vuelo vuelo : listaVuelos) {
    
                    int iOrigen = -1, iDestino = -1, j = 0;
                    for (AstarNode nodo : listaNodos) {
                        // ENCONTRAMOS EL NODO ORIGEN EN LA LISTA
                        if(nodo.getAeropuerto().getId() == vuelo.getAeropuerto_salida().getId()){
                            iOrigen = j;
                        }
                        // ENCONTRAMOS EL NODO DESTINO EN LA LISTA
                        if(nodo.getAeropuerto().getId() == vuelo.getAeropuerto_llegada().getId()){
                            iDestino = j; 
                        }
                        j++;
                    }
                    int costo = vuelo.getTiempo_vuelo_minutos();
                    listaNodos.get(iOrigen).addBranch(costo, listaNodos.get(iDestino),vuelo);
                }
    
                // astar unico
    
                int iOrigen  = indexNodoAeropuerto(listaNodos,  envioPrueba.getAero_origen());
                int iDestino = indexNodoAeropuerto(listaNodos, envioPrueba.getAero_destino());
    
                AstarNode res = AstarSearch.aStar(listaNodos.get(iOrigen), listaNodos.get(iDestino),envioPrueba);
    
                result = AstarSearch.generaListaIDS(res);
            }catch(Exception ex){
                System.err.println(ex.getMessage());
            }
            return result;
        
    }

    int indexNodoAeropuerto(List<AstarNode> listaNodos,Aeropuerto aeropuerto){
        int i = 0;
        for (AstarNode astarNode : listaNodos) {
            if(astarNode.aeropuerto.getId() == aeropuerto.getId()){
                return i;
            }
            i++;
        }
        return -1;
    }
    
}
