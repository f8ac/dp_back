package pe.edu.pucp.packetsoft.services;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.PacketsoftApplication;
import pe.edu.pucp.packetsoft.controllers.Prm;
import pe.edu.pucp.packetsoft.dao.EnvioDao;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.EnvioRet;


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
                calendarioSalida.set(Calendar.SECOND, 0);
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

    public List<Envio> listOrdenFecha(){
        return daoEnvio.listOrdenFecha();
    }

    public List<Envio> listCertainHoursFromDatetime(Prm param){
        return daoEnvio.listCertainHoursFromDatetime(param);
    }

    public List<Envio> readFilesToLocalWithParam(Prm param, Hashtable<String, Aeropuerto> aeroHash){
        List<Envio> result = null;
        try{
            String rutaFolder = "src/main/resources/pack_enviados";
            File folder = new File(rutaFolder);
            File[] listOfFiles = folder.listFiles();
            //listOfFiles.length
            List<Envio> listaEnvios = new ArrayList<>();
            for (int i = 0; i < listOfFiles.length; i++) { // lee los archivos de la ruta carpeta
                if (listOfFiles[i].isFile()) {
                    System.out.print("\nFile " + listOfFiles[i].getName());
                    listaEnvios.addAll(readFileToLocalWithParam(listOfFiles[i].getName(), rutaFolder, param, aeroHash));
                }
            }
            //una vez obtenida la lista de envios toca ordenar por fecha
            Collections.sort(listaEnvios, new Comparator<Envio>(){
                public int compare(Envio envio1,Envio envio2){
                    return envio1.getFecha_hora().compareTo(envio2.getFecha_hora());
                }
            });

            result = listaEnvios;

        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    public List<Envio> readFileToLocalWithParam(String nombreArchivo, String rutaFolder, Prm param, Hashtable<String, Aeropuerto> aeroHash) throws FileNotFoundException, ParseException{
        List<Envio> result = null;
        String line="";
        try {

            List<Envio> listaEnvios = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(rutaFolder + "/" + nombreArchivo));
            String [] data;
            Calendar calendarioSalida = Calendar.getInstance();
            // definimos primero el aeropuerto de salida
            String nomSalida = nombreArchivo.substring(13,17);
            Aeropuerto aeroSalidaDefinido = aeroHash.get(nomSalida);
            Aeropuerto aeroLlegadaDefinido;

            //variables para el aeropuerto de llegada
            String nomLlegada;
            // int cont = 0;
            while((line=br.readLine()) != null) {
                data=line.split("-"); // separa las palabras en un array

                // calendario de envio
                 // tiempos de salida y llegada del vuelo
                calendarioSalida.set(Calendar.YEAR,         Integer.parseInt( data[1].substring(0,4) ));
                calendarioSalida.set(Calendar.MONTH,        Integer.parseInt( data[1].substring(4,6) )-1);
                calendarioSalida.set(Calendar.DAY_OF_MONTH, Integer.parseInt( data[1].substring(6,8) ));
                calendarioSalida.set(Calendar.HOUR_OF_DAY,  Integer.parseInt( data[2].substring(0,2) ));
                calendarioSalida.set(Calendar.MINUTE,       Integer.parseInt( data[2].substring(3,5) ));
                calendarioSalida.set(Calendar.SECOND,       0);
                calendarioSalida.add(Calendar.HOUR_OF_DAY,  aeroSalidaDefinido.getNum_zona_horaria().intValue()*-1);
                // System.err.print(".");
                // nLine++;
                if(!inTimeInterval(calendarioSalida.getTime(), param)){
                    continue;
                }
                // System.err.print(cont+"|");
                // cont ++;

                // aeropuerto de llegada
                
                nomLlegada = data[3].split(":")[0];
                aeroLlegadaDefinido = aeroHash.get(nomLlegada);

                // crea el objeto envio a insertar o agregar a lista
                Envio envio = new Envio();
                envio.setCodigo_envio(data[0]);
                envio.setFecha_hora(calendarioSalida.getTime());
                envio.setAero_origen(aeroSalidaDefinido);
                envio.setAero_destino(aeroLlegadaDefinido);
                envio.setCant_paquetes_total(Integer.parseInt(data[3].split(":")[1]));
                
                listaEnvios.add(envio);
            }
            br.close();
            result = listaEnvios;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    Boolean inTimeInterval(Date datetime, Prm param){
        Boolean result = null;
        try{
            Calendar calInicio = Calendar.getInstance();
            calInicio.set(Calendar.YEAR         ,param.anio);
            calInicio.set(Calendar.MONTH        ,param.mes-1);
            calInicio.set(Calendar.DAY_OF_MONTH ,param.dia);
            calInicio.set(Calendar.HOUR_OF_DAY  ,param.hora);
            calInicio.set(Calendar.MINUTE       ,param.minuto);

            Calendar calFin = Calendar.getInstance();
            calFin.setTime(calInicio.getTime());
            calFin.add(Calendar.HOUR_OF_DAY , param.horaSimul);
            calFin.add(Calendar.MINUTE      , param.minSimul);
            result = false;
            if(datetime.before(calFin.getTime()) && datetime.after(calInicio.getTime())){
                result = true;
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    public EnvioRet insertRet(EnvioRet envioRet){
        return daoEnvio.insertRet(envioRet);
    }

    public String load(){
        String result =  null;
        try{
            String rutaFolder = "src/main/resources/pack_enviados";
            File folder = new File(rutaFolder);
            File[] listOfFiles = folder.listFiles();
            //listOfFiles.length
            List<Envio> listaEnvios = new ArrayList<>();
            for (int i = 0; i < listOfFiles.length; i++) { // lee los archivos de la ruta carpeta
                if (listOfFiles[i].isFile()) {
                    System.out.print("\nFile " + listOfFiles[i].getName());
                    listaEnvios.addAll(readFileToLocal(listOfFiles[i].getName(), rutaFolder, PacketsoftApplication.aeroHash));
                }
                listOfFiles[i] = null;
            }
            //una vez obtenida la lista de envios toca ordenar por fecha
            Collections.sort(listaEnvios, new Comparator<Envio>(){
                public int compare(Envio envio1,Envio envio2){
                    return envio1.getFecha_hora().compareTo(envio2.getFecha_hora());
                }
            });
            PacketsoftApplication.listaEnvios = listaEnvios;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    public List<Envio> readFileToLocal(String nombreArchivo, String rutaFolder, Hashtable<String, Aeropuerto> aeroHash) throws FileNotFoundException, ParseException{
        List<Envio> result = null;
        String line="";
        try {

            List<Envio> listaEnvios = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(rutaFolder + "/" + nombreArchivo));
            String [] data;
            Calendar calendarioSalida = Calendar.getInstance();
            // definimos primero el aeropuerto de salida
            String nomSalida = nombreArchivo.substring(13,17);
            Aeropuerto aeroSalidaDefinido = aeroHash.get(nomSalida);
            Aeropuerto aeroLlegadaDefinido;

            //variables para el aeropuerto de llegada
            String nomLlegada;
            // int cont = 0;
            while((line=br.readLine()) != null) {
                data=line.split("-"); // separa las palabras en un array

                // calendario de envio
                 // tiempos de salida y llegada del vuelo
                calendarioSalida.set(Calendar.YEAR,         Integer.parseInt( data[1].substring(0,4) ));
                calendarioSalida.set(Calendar.MONTH,        Integer.parseInt( data[1].substring(4,6) )-1);
                calendarioSalida.set(Calendar.DAY_OF_MONTH, Integer.parseInt( data[1].substring(6,8) ));
                calendarioSalida.set(Calendar.HOUR_OF_DAY,  Integer.parseInt( data[2].substring(0,2) ));
                calendarioSalida.set(Calendar.MINUTE,       Integer.parseInt( data[2].substring(3,5) ));
                calendarioSalida.set(Calendar.SECOND,       0);
                calendarioSalida.add(Calendar.HOUR_OF_DAY,  aeroSalidaDefinido.getNum_zona_horaria().intValue()*-1);
                // System.err.print(".");
                // nLine++;
                // System.err.print(cont+"|");
                // cont ++;

                // aeropuerto de llegada
                
                nomLlegada = data[3].split(":")[0];
                aeroLlegadaDefinido = aeroHash.get(nomLlegada);

                // crea el objeto envio a insertar o agregar a lista
                Envio envio = new Envio();
                envio.setCodigo_envio(data[0]);
                envio.setFecha_hora(calendarioSalida.getTime());
                envio.setAero_origen(aeroSalidaDefinido);
                envio.setAero_destino(aeroLlegadaDefinido);
                envio.setCant_paquetes_total(Integer.parseInt(data[3].split(":")[1]));
                
                listaEnvios.add(envio);
            }
            br.close();
            result = listaEnvios;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Envio> copyNeededEnvios(Prm param){
        List<Envio> result = null;
        try{
            List<Envio> listaEnvios = new ArrayList<>();
            for (Envio envio : PacketsoftApplication.listaEnvios) {
                if(inTimeInterval(envio.getFecha_hora(), param)){
                    listaEnvios.add(envio);
                }
            }
            result = listaEnvios;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    public String loadNeeded(Prm param){
        String result =  null;
        try{
            String rutaFolder = "src/main/resources/pack_enviados";
            File folder = new File(rutaFolder);
            File[] listOfFiles = folder.listFiles();
            //listOfFiles.length
            List<Envio> listaEnvios = new ArrayList<>();
            for (int i = 0; i < listOfFiles.length; i++) { // lee los archivos de la ruta carpeta
                if (listOfFiles[i].isFile()) {
                    System.out.print("\nFile " + listOfFiles[i].getName());
                    listaEnvios.addAll(readFileToLocalWithParam(listOfFiles[i].getName(), rutaFolder, param, PacketsoftApplication.aeroHash));
                }
            }
            //una vez obtenida la lista de envios toca ordenar por fecha
            Collections.sort(listaEnvios, new Comparator<Envio>(){
                public int compare(Envio envio1,Envio envio2){
                    return envio1.getFecha_hora().compareTo(envio2.getFecha_hora());
                }
            });
            PacketsoftApplication.listaEnvios = listaEnvios;
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }
}
