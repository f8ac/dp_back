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
@Table(name = "vuelo")
@SQLDelete(sql = "UPDATE vuelo SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter

public class Vuelo extends BaseEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aeropuerto_salida")
    private Aeropuerto aeropuerto_salida;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aeropuerto_llegada")
    private Aeropuerto aeropuerto_llegada;
    
    @Column(name = "hora_salida")
    private Date hora_salida;

    @Column(name = "hora_llegada")
    private Date hora_llegada;

    @Column(name = "capacidad_total")
    private Integer capacidad_total;

    @Column(name = "capacidad_utilizada")
    private Integer capacidad_utilizada;

    @Column(name = "tiempo_vuelo_minutos")
    private Integer tiempo_vuelo_minutos;

}
