package pe.edu.pucp.packetsoft.models;

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
@Table(name = "vuelo_x_envio")
@SQLDelete(sql = "UPDATE vuelo_x_envio SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class VueloXEnvio extends BaseEntity{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vuelo")
    private Vuelo vuelo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "envio")
    private Envio envio;
}
