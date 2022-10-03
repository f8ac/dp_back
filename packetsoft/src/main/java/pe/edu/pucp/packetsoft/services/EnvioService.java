package pe.edu.pucp.packetsoft.services;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.dao.EnvioDao;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Envio;


@Service
public class EnvioService {
    @Autowired
    private EnvioDao daoEnvio;
    @Autowired
    private AeropuertoService aeropuertoService;


    public List<Envio> getAll(){
        return daoEnvio.getAll();
    }

    public Envio get(int id){
        return daoEnvio.get(id);
    }

    public Envio insert(Envio envio){
        return daoEnvio.insert(envio);
    }


    public void insertarFileAeroPaquetes(String nombreArchivo, String rutaFolder) throws FileNotFoundException, ParseException{
        String line="";

        try {
            BufferedReader br = new BufferedReader(new FileReader(rutaFolder + "/" + nombreArchivo));
            while((line=br.readLine()) != null) {
                String [] data=line.split("-"); // separa las palabras en un array

                // aeropuerto de salida y llegada
                Aeropuerto aeroSalida = new Aeropuerto();
                Aeropuerto aeroLlegada = new Aeropuerto();
                String nomSalida = nombreArchivo.substring(13,17);
                String nomLlegada = data[3].split(":")[0];
                aeroSalida.setCod_aeropuerto(nomSalida);
                aeroLlegada.setCod_aeropuerto(nomLlegada);
                Aeropuerto aeroSalidaDefinido = aeropuertoService.getByCodigo(aeroSalida);
                Aeropuerto aeroLlegadaDefinido = aeropuertoService.getByCodigo(aeroLlegada);
               
                // calendario de envio
                Calendar calendarioSalida = Calendar.getInstance(); // tiempos de salida y llegada del vuelo
                calendarioSalida.set(Calendar.YEAR, Integer.parseInt( data[1].substring(0,4) ));
                calendarioSalida.set(Calendar.MONTH, Integer.parseInt( data[1].substring(4,6) )-1);
                calendarioSalida.set(Calendar.DAY_OF_MONTH, Integer.parseInt( data[1].substring(6,8) ));
                calendarioSalida.set(Calendar.HOUR_OF_DAY, Integer.parseInt( data[2].substring(0,2) ));
                calendarioSalida.set(Calendar.MINUTE, Integer.parseInt( data[2].substring(3,5) ));
                calendarioSalida.add(Calendar.HOUR_OF_DAY, aeroSalidaDefinido.getNum_zona_horaria().intValue()*-1);

                // crea el objeto envio a insertar
                Envio envio = new Envio();
                envio.setCodigo_envio(data[0]);
                envio.setFecha_hora(calendarioSalida.getTime());
                envio.setAero_origen(aeroSalidaDefinido);
                envio.setAero_destino(aeroLlegadaDefinido);
                envio.setCant_paquetes_total(Integer.parseInt(data[3].split(":")[1]));
                

                daoEnvio.insert(envio);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertfile() throws IOException, InterruptedException, ParseException {
        String rutaFolder = "src/main/resources/pack_enviados";
        File folder = new File(rutaFolder);
        File[] listOfFiles = folder.listFiles();
        //listOfFiles.length
        for (int i = 0; i < listOfFiles.length; i++) { // lee los archivos de la ruta carpeta
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                insertarFileAeroPaquetes(listOfFiles[i].getName(), rutaFolder);
            }
        }

    }

}
