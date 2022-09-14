package pe.edu.pucp.packetsoft.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Continente;
import pe.edu.pucp.packetsoft.services.AeropuertoService;
import pe.edu.pucp.packetsoft.services.ContinenteService;
import pe.edu.pucp.packetsoft.services.UsuarioService;

@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {
    
    @Autowired
    private AeropuertoService aeropuertoService;

    @Autowired
    private ContinenteService continenteService;

    @PostMapping(value = "/")
    String main(@RequestParam("file") MultipartFile file){
        List<Continente> listaContinente = null;
        
        try{
            String fileBuffer = new String(file.getBytes());
            // FileInputStream inputStream = new FileInputStream(file);
            InputStream targeStream = new ByteArrayInputStream(file.getBytes());
            BufferedInputStream buffer = new BufferedInputStream(targeStream);

            BufferedReader reader = new BufferedReader(new InputStreamReader(buffer));
            String line;
            reader.readLine();
            reader.readLine();
            reader.readLine();
        
            Scanner scan;
            

            while(reader.ready()){
                line = reader.readLine();
                scan = new Scanner(line);

                String nombreContinente = line.trim();

                Continente continente = new Continente();
                continente.setNombre(nombreContinente);
                Continente result = continenteService.insert(continente);

                while(true){

                    line = reader.readLine();
                    scan = new Scanner(line);
                    if(line == null) break;
                    if(line == null || line.isEmpty() ){
                        break;
                    }

                    String numAerpuertoStr;
                    Integer numAeropuerto;
                    String codAeropuerto;
                    String nomCiudad;
                    String nomPais;
                    String codCiudad;

                    numAerpuertoStr = line.substring(0, 5).trim();
                    codAeropuerto = line.substring(4, 11).trim();
                    nomCiudad = line.substring(12, 31).trim();
                    nomPais = line.substring(32, 47).trim();
                    codCiudad = line.substring(48, 52).trim();

                    Aeropuerto aeropuerto = new Aeropuerto();
                    aeropuerto.setCod_aeropuerto(codAeropuerto);
                    aeropuerto.setCod_ciudad(codCiudad);
                    aeropuerto.setPais(nomPais);
                    aeropuerto.setContinente(result);

                    aeropuertoService.insert(aeropuerto);


                }
            }



            // for (Continente continente : listaContinente) {
            //     System.out.println(continente.getNombre());
            // }
            // System.out.println(fileBuffer);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        return "main";
    }


    // private void lineToStr(String line, String result, Integer start, Integer end){
    //     result = new String();
    //     for (int i = start, j = 0; i < end + 1; i++,j++) {
    //         result.charAt(j) = line.charAt(i);
    //     }
    // }
}
