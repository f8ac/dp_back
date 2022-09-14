package pe.edu.pucp.packetsoft.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.dao.AeropuertoDao;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Continente;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.function.ToDoubleFunction;

@Service
public class AeropuertoService {
    @Autowired
    private AeropuertoDao daoAeropuerto;

    public List<Aeropuerto> getAll(){
        return daoAeropuerto.getAll();
    }

    public Aeropuerto insert(Aeropuerto aeropuerto){
        return daoAeropuerto.insert(aeropuerto);
    }


    String line="";
    public void insertfile() throws IOException {
    //    try {
            System.out.println("hola");
            BufferedReader br = new BufferedReader(new FileReader("packetsoft/src/main/resources/aeropuertosv01.txt"));
            while((line=br.readLine()) != null) {
                String [] data=line.split(" ");
                Aeropuerto aero = new Aeropuerto();
                aero.setCod_aeropuerto(data[0]);
                aero.setNombre(data[1]);
                aero.setPais(data[2]);
                aero.setCod_ciudad(data[3]);
                aero.setZona_horaria("UTC-5");
                aero.setNum_zona_horaria(6.0);
                aero.setLongitud(10.0);
                aero.setLatitud(10.0);
                aero.setCapacidad_total(100);
                aero.setCapacidad_utilizado(10);

                Continente con = new Continente();
                con.setId(1);
                aero.setContinente(con);
                System.out.println(data[0]);

                
                daoAeropuerto.insert(aero);

            }
            br.close();
//        }
    }


}
