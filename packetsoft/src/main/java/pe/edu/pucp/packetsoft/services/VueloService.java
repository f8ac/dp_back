package pe.edu.pucp.packetsoft.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

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

    public List<Vuelo> listVecinos(Aeropuerto aeropuerto, List<Integer> aeropuertosId){
        return daoEmpresa.listVecinos(aeropuerto, aeropuertosId);
    }

    public List<Vuelo> listVecinosLlegada(Aeropuerto aeropuerto, List<Integer> aeropuertosId){
        return daoEmpresa.listVecinosLlegada(aeropuerto,aeropuertosId);
    }
    

    public void insertfile() throws IOException, InterruptedException {
        int mes = Calendar.AUGUST, dia = 2, anio = 2022;
        for (int i = 1; i < 10; i++) {
            String line="";
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/vuelosv01.txt"));
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


    public void insertTenDays() throws IOException, InterruptedException{

    }

    public Vuelo buscarVuelo1(int idinicio,int idfin, Calendar horaSalida, int paquetes){
        return daoEmpresa.buscarVuelo1(idinicio,idfin,horaSalida,paquetes);
    }

    public Vuelo buscarVuelo2(int idinicio,int idfin, Calendar horaSalida,Calendar horaLlegada,int paquetes){
        return daoEmpresa.buscarVuelo2(idinicio,idfin,horaSalida,horaLlegada,paquetes);
    }
}
