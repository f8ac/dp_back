package pe.edu.pucp.packetsoft.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "paquete")
@SQLDelete(sql = "UPDATE paquete SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Paquete extends BaseEntity{

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cantidad")
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_plan_viaje")
    private PlanViaje planViaje;

    @Column(name = "fecha_entrada")
    private Date fecha_entrada;

    @Column(name = "fecha_salida")
    private Date fecha_salida;

    @Column(name = "active")
    private Boolean active;

    private Integer idAeropuerto;
    

}
