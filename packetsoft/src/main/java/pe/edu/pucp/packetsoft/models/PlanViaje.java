package pe.edu.pucp.packetsoft.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class PlanViaje implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;
    
    @Column(name = "estado")
    private String estado;

    @Column(name = "id_envio_ret")
    private String id_envio_ret;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vuelo_util")
    private VueloUtil vuelo_util;



}
