package pe.edu.pucp.packetsoft.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.dao.AeropuertoDao;
import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Continente;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AeropuertoService {
    @Autowired
    private AeropuertoDao daoAeropuerto;

    public List<Aeropuerto> getAll(){
        return daoAeropuerto.getAll();
    }


    public Aeropuerto get(int id){
        Aeropuerto aeropuerto;
        aeropuerto = daoAeropuerto.get(id);
        return aeropuerto;
    }




    public Aeropuerto insert(Aeropuerto aeropuerto){
        return daoAeropuerto.insert(aeropuerto);
    }



    public String getResultFromMapsApi(String query) throws IOException, InterruptedException {
        String GEOCODING_RESOURCE = "https://maps.googleapis.com/maps/api/geocode/json?"; //ruta
        String API_KEY = "AIzaSyAfGFPnuTw9MPYxkg3rcI2X9PIHq9AA1Dk"; //clave secreta
        HttpClient httpClient = HttpClient.newHttpClient();
        
        String requestUri = GEOCODING_RESOURCE + "address=" + query + "&key=" + API_KEY; //url

        HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(2000)).build();
        HttpResponse geocodingResponse = httpClient.send(geocodingRequest, HttpResponse.BodyHandlers.ofString());
        return (String) geocodingResponse.body();
    }


    public String getResultFromZoneApi(String query) throws IOException, InterruptedException {
        String GEOCODING_RESOURCE = "https://maps.googleapis.com/maps/api/timezone/json?"; //ruta
        String API_KEY = "AIzaSyAfGFPnuTw9MPYxkg3rcI2X9PIHq9AA1Dk"; //clave secreta
        HttpClient httpClient = HttpClient.newHttpClient();
        
        String requestUri = GEOCODING_RESOURCE + "location=" + query + "&timestamp=0&key=" + API_KEY; //url

        HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(2000)).build();
        HttpResponse geocodingResponse = httpClient.send(geocodingRequest, HttpResponse.BodyHandlers.ofString());
        return (String) geocodingResponse.body();
    }


    public void insertfile() throws IOException, InterruptedException {
        String line="";
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/aeropuertosv01.txt"));
        while((line=br.readLine()) != null) {
            String linea=line.trim().replaceAll(" +", " "); // limpia multiples espacios en blanco
            String [] data=linea.split(" "); // separa las palabras en un array

            Aeropuerto aero = new Aeropuerto(); // crea aeropuerto

            
            // set de latitud, longitud, zona horaria, horas zona horaria
            String responseMap = getResultFromMapsApi(data[1] + "+" + data[3] + "+" + data[2]); // busa por: COD + PAIS + nombre
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseJsonNode = mapper.readTree(responseMap);
            JsonNode resultsArray = (JsonNode) responseJsonNode.get("results");

            for(JsonNode results : resultsArray){ // toma el primer elemento del array resultsArray
                Double latitud = results.at("/geometry/location/lat").asDouble();
                Double longitud = results.at("/geometry/location/lng").asDouble();
                aero.setLatitud(latitud);
                aero.setLongitud(longitud);

                String responseZone = getResultFromZoneApi(latitud + "," + longitud); // busca por: lat y lng de aeropuerto zona horaria
                ObjectMapper mapperZone = new ObjectMapper();
                JsonNode responseJsonNodeZone = mapperZone.readTree(responseZone);
                Double horasZonaHoraria = responseJsonNodeZone.get("rawOffset").asDouble()/60/60;
                aero.setZona_horaria("UTC (" + Double.toString(horasZonaHoraria) + ")");
                aero.setNum_zona_horaria(horasZonaHoraria);
                break;
            }

            aero.setCod_aeropuerto(data[1]);
            aero.setNombre(data[2]);
            aero.setPais(data[3]);
            aero.setCod_ciudad(data[4]);            
            aero.setCapacidad_total(100);
            aero.setCapacidad_utilizado(0);

            Continente con = new Continente();
            con.setId(Integer.parseInt(data[5]));
            // System.out.println(data[5]+data[4]);
            aero.setContinente(con);

            daoAeropuerto.insert(aero); // inserta cada aeropuerto
        }
        br.close();
    }

    public Aeropuerto getByCodigo(Aeropuerto aeropuerto){
        return daoAeropuerto.getByCodigo(aeropuerto);
    }

    public Aeropuerto getByCodigoString(String codigo){
        return daoAeropuerto.getByCodigoString(codigo);
    }

}
