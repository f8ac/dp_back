package pe.edu.pucp.packetsoft.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class AeropuertoRet {
    private Integer id;
    private String cod_aeropuerto; 
    private String nombre;
    private String pais;

    public void getAttributesFromAeropuerto(Aeropuerto aeropuerto){
        try{
            this.setId(aeropuerto.getId());
            this.setCod_aeropuerto(aeropuerto.getCod_aeropuerto());
            this.setNombre(aeropuerto.getNombre());
            this.setPais(aeropuerto.getPais());
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
}
