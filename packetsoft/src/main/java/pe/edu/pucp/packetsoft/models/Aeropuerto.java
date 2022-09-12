package pe.edu.pucp.packetsoft.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Aeropuerto")
@SQLDelete(sql = "UPDATE aeropuerto SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Aeropuerto extends BaseEntity {
    @Column(name = "cod_aeropuerto")
    private String cod_aeropuerto;
    @Column(name = "nombre")
    private double nombre;
    @Column(name = "pais")
    private double pais;
    @Column(name = "cod_ciudad")
    private int cod_ciudad;

    @Column(name = "longitud")
    private double longitud;
    @Column(name = "latitud")
    private double latitud;
    @Column(name = "capacidad_total")
    private int capacidad_total;
    @Column(name = "capacidad_utilizado")
    private int capacidad_utilizado;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_continente")
    private Continente continente;
}