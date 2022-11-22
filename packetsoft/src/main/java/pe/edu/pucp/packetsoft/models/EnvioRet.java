package pe.edu.pucp.packetsoft.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class EnvioRet {
    private String codigo_envio;
    private Date fecha_hora;
    private AeropuertoRet aero_origen;
    private AeropuertoRet aero_destino;
    private Integer cant_paquetes_total;
}
