package pe.edu.pucp.packetsoft.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.dao.VueloDao;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Vuelo;

@Service
public class VueloService {
    @Autowired
    private VueloDao daoEmpresa;
    @Autowired
    private AeropuertoService aeropuertoService;

    public List<Vuelo> getAll(){
        return daoEmpresa.getAll();
    }

    public Vuelo get(int id){
        return daoEmpresa.get(id);
    }

    public Vuelo insert(Vuelo empresa){
        return daoEmpresa.insert(empresa);
    }




    public void insertfile() throws IOException, InterruptedException {
        String line="";
        BufferedReader br = new BufferedReader(new FileReader("packetsoft/src/main/resources/vuelosv01.txt"));
        while((line=br.readLine()) != null) {
            String [] data=line.split("-"); // separa las palabras en un array

            // aeropuerto de salida y llegada
            Aeropuerto aeroSalida = new Aeropuerto();
            Aeropuerto aeroLlegada = new Aeropuerto();
            aeroSalida.setCod_aeropuerto(data[0]);
            aeroLlegada.setCod_aeropuerto(data[1]);
            Aeropuerto aeroSalidaDefinido = aeropuertoService.getByCodigo(aeroSalida);
            Aeropuerto aeroLlegadaDefinido = aeropuertoService.getByCodigo(aeroLlegada);


            Calendar calendarioSalida = Calendar.getInstance(); // tiempos de salida y llegada del vuelo
            calendarioSalida.set(Calendar.HOUR_OF_DAY, Integer.parseInt(data[2].split(":")[0]));
            calendarioSalida.set(Calendar.MINUTE, Integer.parseInt(data[2].split(":")[1]));
            Calendar calendarioLlegada = Calendar.getInstance();
            calendarioLlegada.set(Calendar.HOUR_OF_DAY, Integer.parseInt(data[3].split(":")[0])+5);
            calendarioLlegada.set(Calendar.MINUTE, Integer.parseInt(data[3].split(":")[1]));


            Vuelo vuelo = new Vuelo(); // creacion del vuelo
            vuelo.setHora_salida(calendarioSalida.getTime());
            vuelo.setHora_llegada(calendarioLlegada.getTime());
            vuelo.setCapacidad_utilizada(0);
            vuelo.setCapacidad_total(100);
            vuelo.setAeropuerto_salida(aeroSalidaDefinido);
            vuelo.setAeropuerto_llegada(aeroLlegadaDefinido);

            daoEmpresa.insert(vuelo); // inserta el vuelo
        }
        br.close();
    }

}
