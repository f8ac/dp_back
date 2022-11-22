package pe.edu.pucp.packetsoft.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.dao.VueloDao;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Vuelo;
import pe.edu.pucp.packetsoft.models.VueloUtil;

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

    public List<Vuelo> listVecinos(Aeropuerto aeropuerto, List<Integer> aeropuertosId){
        return daoEmpresa.listVecinos(aeropuerto, aeropuertosId);
    }

    public List<Vuelo> listVecinosLlegada(Aeropuerto aeropuerto, List<Integer> aeropuertosId){
        return daoEmpresa.listVecinosLlegada(aeropuerto,aeropuertosId);
    }
    

    public void insertfile() throws IOException, InterruptedException {
        int mes = Calendar.AUGUST, dia = 2, anio = 2022;
        for (int i = 0; i < 10; i++) {
            String line="";
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/vuelosv02.txt"));
            while((line=br.readLine()) != null) {
                String [] data=line.split("-"); // separa las palabras en un array

                // aeropuerto de salida y llegada
                Aeropuerto aeroSalida = new Aeropuerto();
                Aeropuerto aeroLlegada = new Aeropuerto();
                aeroSalida.setCod_aeropuerto(data[0]);
                aeroLlegada.setCod_aeropuerto(data[1]);
                Aeropuerto aeroSalidaDefinido = aeropuertoService.getByCodigo(aeroSalida);
                Aeropuerto aeroLlegadaDefinido = aeropuertoService.getByCodigo(aeroLlegada);

                int horaSalida  = Integer.parseInt(data[2].split(":")[0]);
                int horaLlegada = Integer.parseInt(data[3].split(":")[0]);
                int zonaHorariaSalida  =  aeroSalidaDefinido.getNum_zona_horaria().intValue();
                int zonaHorariaLlegada = aeroLlegadaDefinido.getNum_zona_horaria().intValue();
                int horaSalidaUTC,horaLlegadaUTC;
                int diaModifSalida = dia + i;
                if(zonaHorariaSalida > horaSalida){
                    horaSalidaUTC = 24 - (horaSalida - zonaHorariaSalida);
                    diaModifSalida --;
                }else{
                    horaSalidaUTC = (horaSalida - zonaHorariaSalida);
                    if(horaSalidaUTC > 24){
                        horaSalidaUTC -= 24;
                        diaModifSalida++;
                    }
                }
                int diaModifLlegada = diaModifSalida;
                if(zonaHorariaLlegada > horaLlegada ){
                    horaLlegadaUTC = 24 - (horaLlegada - zonaHorariaLlegada);
                }else{
                    horaLlegadaUTC = horaLlegada - zonaHorariaLlegada;
                    if(horaLlegadaUTC > 24){
                        horaLlegadaUTC -= 24;
                    }
                }

                int minutos_salida  = horaSalidaUTC  * 60 + Integer.parseInt(data[2].split(":")[1]);
                int minutos_llegada = horaLlegadaUTC * 60 + Integer.parseInt(data[3].split(":")[1]);

                if(minutos_salida > minutos_llegada){
                    diaModifLlegada++;
                }

                Calendar calendarioSalida = Calendar.getInstance(); // tiempos de salida y llegada del vuelo
                calendarioSalida.set(Calendar.HOUR_OF_DAY, horaSalidaUTC);
                calendarioSalida.set(Calendar.MINUTE, Integer.parseInt(data[2].split(":")[1]));
                Calendar calendarioLlegada = Calendar.getInstance();
                calendarioLlegada.set(Calendar.HOUR_OF_DAY, horaLlegadaUTC);
                calendarioLlegada.set(Calendar.MINUTE, Integer.parseInt(data[3].split(":")[1]));
                calendarioSalida .set(Calendar.YEAR, anio);
                calendarioLlegada.set(Calendar.YEAR, anio);
                calendarioSalida .set(Calendar.MONTH, mes);
                calendarioLlegada.set(Calendar.MONTH, mes);
                calendarioSalida .set(Calendar.DAY_OF_MONTH, diaModifSalida);
                calendarioLlegada.set(Calendar.DAY_OF_MONTH,diaModifLlegada);
                int tiempo_vuelo_minutos = minutos_llegada - minutos_salida;

                Vuelo vuelo = new Vuelo(); // creacion del vuelo

                if(tiempo_vuelo_minutos < 0){
                    tiempo_vuelo_minutos = 1440+tiempo_vuelo_minutos;
                }
                if(tiempo_vuelo_minutos > 1440){
                    tiempo_vuelo_minutos -= 1440;
                }
                vuelo.setTiempo_vuelo_minutos(tiempo_vuelo_minutos);
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

    public List<Vuelo> readFileToLocal() throws IOException, InterruptedException{
        List<Vuelo> result = null;
        try{
            result = new ArrayList<Vuelo>();
            Calendar defaultCalendar = Calendar.getInstance();
            defaultCalendar.set(Calendar.YEAR           , 2022);
            defaultCalendar.set(Calendar.MONTH          , Calendar.AUGUST);
            defaultCalendar.set(Calendar.DAY_OF_MONTH   , 1);
            defaultCalendar.set(Calendar.HOUR_OF_DAY    , 0);
            defaultCalendar.set(Calendar.MINUTE         , 0);
            defaultCalendar.set(Calendar.SECOND         , 0);
            Calendar calendarioSalida  = Calendar.getInstance();
            Calendar calendarioLlegada = Calendar.getInstance();
            calendarioSalida .setTime(defaultCalendar.getTime());
            calendarioLlegada.setTime(defaultCalendar.getTime());
            Aeropuerto aeroSalida = new Aeropuerto();
            Aeropuerto aeroLlegada = new Aeropuerto();
            String line="";
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/vuelosv02.txt"));
            while((line=br.readLine()) != null) {
                String [] data=line.split("-"); // separa las palabras en un array
                aeroSalida.setCod_aeropuerto(data[0]);
                aeroLlegada.setCod_aeropuerto(data[1]);
                Aeropuerto aeroSalidaDefinido = aeropuertoService.getByCodigo(aeroSalida);
                Aeropuerto aeroLlegadaDefinido = aeropuertoService.getByCodigo(aeroLlegada);
                int horaSalida  = Integer.parseInt(data[2].split(":")[0]);
                int horaLlegada = Integer.parseInt(data[3].split(":")[0]);
                int minutoSalida  = Integer.parseInt(data[2].split(":")[1]);
                int minutoLlegada = Integer.parseInt(data[3].split(":")[1]);
                int zonaHorariaSalida  =  aeroSalidaDefinido.getNum_zona_horaria().intValue();
                int zonaHorariaLlegada = aeroLlegadaDefinido.getNum_zona_horaria().intValue();
                calendarioSalida .setTime(defaultCalendar.getTime());
                calendarioLlegada.setTime(defaultCalendar.getTime());
                calendarioSalida.set (Calendar.HOUR_OF_DAY, horaSalida);
                calendarioSalida.set (Calendar.MINUTE, minutoSalida);
                calendarioLlegada.set(Calendar.HOUR_OF_DAY, horaLlegada);
                calendarioLlegada.set(Calendar.MINUTE, minutoLlegada);
                calendarioSalida.add (Calendar.HOUR_OF_DAY, -1*zonaHorariaSalida);
                calendarioLlegada.add(Calendar.HOUR_OF_DAY, -1*zonaHorariaLlegada);
                if(calendarioLlegada.before(calendarioSalida)){
                    calendarioLlegada.add(Calendar.DAY_OF_MONTH, 1);
                }
                long durationMilis = calendarioLlegada.getTimeInMillis() - calendarioSalida.getTimeInMillis();
                long tiempo_vuelo_minutos = TimeUnit.MILLISECONDS.toMinutes(durationMilis);

                Vuelo vuelo = new Vuelo(); // creacion del vuelo

                if(diffContinent(aeroSalidaDefinido,aeroLlegadaDefinido) ){
                    if(tiempo_vuelo_minutos < 360){
                        calendarioLlegada.add(Calendar.DAY_OF_MONTH, 1);
                        tiempo_vuelo_minutos+=1440;
                    }
                    vuelo.setInternacional(true);
                }else{
                    vuelo.setInternacional(false);
                }
                vuelo.setTiempo_vuelo_minutos((int)tiempo_vuelo_minutos);
                vuelo.setHora_salida(calendarioSalida.getTime());
                vuelo.setHora_llegada(calendarioLlegada.getTime());
                vuelo.setCapacidad_utilizada(0);
                vuelo.setCapacidad_total(100);
                vuelo.setAeropuerto_salida(aeroSalidaDefinido);
                vuelo.setAeropuerto_llegada(aeroLlegadaDefinido);
                result.add(vuelo);
                // System.out.println(i);
                // i++;
            }
            br.close();
                
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    public void insertfile2() throws IOException, InterruptedException{
        try{
            Calendar defaultCalendar = Calendar.getInstance();
            defaultCalendar.set(Calendar.YEAR           , 2022);
            defaultCalendar.set(Calendar.MONTH          , Calendar.AUGUST);
            defaultCalendar.set(Calendar.DAY_OF_MONTH   , 1);
            defaultCalendar.set(Calendar.HOUR_OF_DAY    , 0);
            defaultCalendar.set(Calendar.MINUTE         , 0);
            defaultCalendar.set(Calendar.SECOND         , 0);
            Calendar topCalendar = Calendar.getInstance();
            topCalendar.set(Calendar.YEAR           , 2022);
            topCalendar.set(Calendar.MONTH          , Calendar.AUGUST);
            topCalendar.set(Calendar.DAY_OF_MONTH   , 2);
            topCalendar.set(Calendar.HOUR_OF_DAY    , 0);
            topCalendar.set(Calendar.MINUTE         , 0);
            topCalendar.set(Calendar.SECOND         , 0);
            System.out.println(defaultCalendar.getTime());
            Calendar calendarioSalida  = Calendar.getInstance();
            Calendar calendarioLlegada = Calendar.getInstance();
            calendarioSalida .setTime(defaultCalendar.getTime());
            calendarioLlegada.setTime(defaultCalendar.getTime());
            Aeropuerto aeroSalida = new Aeropuerto();
            Aeropuerto aeroLlegada = new Aeropuerto();
            String line="";
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/vuelosv02.txt"));
            while((line=br.readLine()) != null) {
                String [] data=line.split("-"); // separa las palabras en un array
                aeroSalida.setCod_aeropuerto(data[0]);
                aeroLlegada.setCod_aeropuerto(data[1]);
                Aeropuerto aeroSalidaDefinido  = aeropuertoService.getByCodigo(aeroSalida);
                Aeropuerto aeroLlegadaDefinido = aeropuertoService.getByCodigo(aeroLlegada);
                int horaSalida  = Integer.parseInt(data[2].split(":")[0]);
                int horaLlegada = Integer.parseInt(data[3].split(":")[0]);
                int minutoSalida  = Integer.parseInt(data[2].split(":")[1]);
                int minutoLlegada = Integer.parseInt(data[3].split(":")[1]);
                int zonaHorariaSalida  =  aeroSalidaDefinido.getNum_zona_horaria().intValue();
                int zonaHorariaLlegada = aeroLlegadaDefinido.getNum_zona_horaria().intValue();
                calendarioSalida .setTime(defaultCalendar.getTime());
                calendarioLlegada.setTime(defaultCalendar.getTime());
                calendarioSalida.set (Calendar.HOUR_OF_DAY, horaSalida);
                calendarioSalida.set (Calendar.MINUTE, minutoSalida);
                calendarioLlegada.set(Calendar.HOUR_OF_DAY, horaLlegada);
                calendarioLlegada.set(Calendar.MINUTE, minutoLlegada);
                calendarioSalida.add (Calendar.HOUR_OF_DAY, -1*zonaHorariaSalida);
                calendarioLlegada.add(Calendar.HOUR_OF_DAY, -1*zonaHorariaLlegada);
                while(calendarioSalida.after(topCalendar) || calendarioSalida.equals(topCalendar)){
                    calendarioSalida.add(Calendar.DATE, -1);
                    calendarioLlegada.add(Calendar.DATE, -1);
                }
                while(calendarioSalida.before(defaultCalendar)){
                    calendarioSalida.add(Calendar.DATE, 1);
                }
                while(calendarioLlegada.before(calendarioSalida)){
                    calendarioLlegada.add(Calendar.DATE, 1);
                }
                long durationMilis = calendarioLlegada.getTimeInMillis() - calendarioSalida.getTimeInMillis();
                long tiempo_vuelo_minutos = TimeUnit.MILLISECONDS.toMinutes(durationMilis);

                Vuelo vuelo = new Vuelo(); // creacion del vuelo

                if(diffContinent(aeroSalidaDefinido,aeroLlegadaDefinido) ){
                    if(tiempo_vuelo_minutos < 360){
                        calendarioLlegada.add(Calendar.DAY_OF_MONTH, 1);
                        tiempo_vuelo_minutos+=1440;
                    }
                    vuelo.setInternacional(true);
                }else{
                    vuelo.setInternacional(false);
                }
                vuelo.setTiempo_vuelo_minutos((int)tiempo_vuelo_minutos);
                vuelo.setHora_salida(calendarioSalida.getTime());
                vuelo.setHora_llegada(calendarioLlegada.getTime());
                vuelo.setCapacidad_utilizada(0);
                vuelo.setCapacidad_total(100);
                vuelo.setAeropuerto_salida(aeroSalidaDefinido);
                vuelo.setAeropuerto_llegada(aeroLlegadaDefinido);
                insert(vuelo);
            }
            br.close();   
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    // public Vuelo buscarVuelo1(int idinicio,int idfin, Calendar horaSalida, int paquetes){
    //     return daoEmpresa.buscarVuelo1(idinicio,idfin,horaSalida,paquetes);
    // }

    // public Vuelo buscarVuelo2(int idinicio,int idfin, Calendar horaSalida,Calendar horaLlegada,int paquetes){
    //     return daoEmpresa.buscarVuelo2(idinicio,idfin,horaSalida,horaLlegada,paquetes);
    // }

    public Boolean diffContinent(Aeropuerto a1, Aeropuerto a2){
        try{
            if(a1.getContinente().getId() != a2.getContinente().getId()){
                return true;
            }
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return false;
    }

    public List<Vuelo> getSortedFromTime(int hora, int minuto, int horaSimul){
        List<Vuelo> result = null;
        try{
            result = daoEmpresa.getSortedFromTime(hora,minuto, horaSimul);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return result;
    }

    public VueloUtil insertUtil(VueloUtil vueloUtil){
        return daoEmpresa.insertUtil(vueloUtil);
    }
}
