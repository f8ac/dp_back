package pe.edu.pucp.packetsoft.utils;

import java.util.Comparator;

import pe.edu.pucp.packetsoft.models.Movimiento;

public class PaquetesComp implements Comparator<Movimiento>{

    @Override
    public int compare(Movimiento arg0, Movimiento arg1) {
        int result = 0;
        try{
            if(arg0.getFecha().before(arg1.getFecha())){
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
