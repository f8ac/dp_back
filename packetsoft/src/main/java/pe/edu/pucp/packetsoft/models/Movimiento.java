package pe.edu.pucp.packetsoft.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class Movimiento {
    private Envio envio;
    private Date fecha;
    private Integer operacion;
    private Aeropuerto aeropuerto;
}
