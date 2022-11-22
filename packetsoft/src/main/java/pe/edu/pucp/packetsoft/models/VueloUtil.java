package pe.edu.pucp.packetsoft.models;

import java.io.Serializable;
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
@Table(name = "vuelo_util")
@SQLDelete(sql = "UPDATE vuelo_util SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class VueloUtil extends BaseEntity implements Serializable{

    @Column(name = "id_aux")
    private Integer id_aux;

    @Column(name = "salida_real")
    private Date salida_real;

    @Column(name = "llegada_real")
    private Date llegada_real;

    @Column(name = "cap_tot_real")
    private Integer cap_tot_real;

    @Column(name = "cap_util_real")
    private Integer cap_util_real;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vuelo")
    private Vuelo vuelo;
}
