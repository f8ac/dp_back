package pe.edu.pucp.packetsoft.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "envio_ret")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class EnvioRet {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "fecha_hora")
    private Date fecha_hora;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aero_origen")
    private AeropuertoRet aero_origen;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aero_destino")
    private AeropuertoRet aero_destino;
    @Column(name = "cant_paquetes_total")
    private Integer cant_paquetes_total;

    public void getAttributesFromEnvio(Envio envio){
        try{
            this.setId(envio.getCodigo_envio());
            this.setFecha_hora(envio.getFecha_hora());
            //proceso especial con aeropuertos
            AeropuertoRet aeroOrigen = new AeropuertoRet();
            AeropuertoRet aeroDestino = new AeropuertoRet();
            aeroOrigen.getAttributesFromAeropuerto(envio.getAero_origen());
            aeroDestino.getAttributesFromAeropuerto(envio.getAero_destino());
            this.setAero_origen(aeroOrigen);
            this.setAero_destino(aeroDestino);
            this.setCant_paquetes_total(envio.getCant_paquetes_total());
            
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
}
