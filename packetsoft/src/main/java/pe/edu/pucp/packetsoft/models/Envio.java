package pe.edu.pucp.packetsoft.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class Envio extends BaseEntity {
    @Column(name = "codigo_envio")
    private String codigo_envio;

    @Column(name = "fecha_hora")
    private Date fecha_hora;

    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aero_origen")
    private Aeropuerto aero_origen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aero_destino")
    private Aeropuerto aero_destino;


    @Column(name = "cant_paquetes_total")
    private Integer cant_paquetes_total;
}
