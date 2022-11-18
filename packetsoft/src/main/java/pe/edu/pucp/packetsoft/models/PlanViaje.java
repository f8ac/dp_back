package pe.edu.pucp.packetsoft.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "plan_viaje")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlanViaje extends BaseEntity{
    
    @Column(name = "estado")
    private String estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_envio")
    private Envio envio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vuelo_util")
    private VueloUtil vuelo_util;

}
