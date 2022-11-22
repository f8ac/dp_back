package pe.edu.pucp.packetsoft.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "aeropuerto_ret")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class AeropuertoRet {
    @Id
    @Column(name = "id",updatable = false, nullable = false)
    private Integer id;
    @Column(name = "cod_aeropuerto")
    private String cod_aeropuerto; 
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "pais")
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
