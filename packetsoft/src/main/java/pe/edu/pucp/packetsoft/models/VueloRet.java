package pe.edu.pucp.packetsoft.models;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class VueloRet{
    private VueloUtil vuelo_util;
    private List<Envio> inventario;
    private Boolean colapso;
}
