package pe.edu.pucp.packetsoft.models;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "continente")
@SQLDelete(sql = "UPDATE continente SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter 
@Setter

public class Continente extends BaseEntity implements Serializable{
    
    @Column(name = "nombre")
    private String nombre;

}
