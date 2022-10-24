package pe.edu.pucp.packetsoft.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class VueloInv extends Vuelo{
    private List<Envio> inventario;
}
