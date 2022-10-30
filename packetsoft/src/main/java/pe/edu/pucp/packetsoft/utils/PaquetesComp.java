package pe.edu.pucp.packetsoft.utils;

import java.util.Comparator;

import pe.edu.pucp.packetsoft.models.Paquete;

public class PaquetesComp implements Comparator<Paquete>{

    @Override
    public int compare(Paquete arg0, Paquete arg1) {
        int result = 0;
        try{
            if(arg0.getFecha_salida().before(arg1.getFecha_salida())){
                result = 1;
            }else{
                result = -1;
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return result;
    }
    
}
