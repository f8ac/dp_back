package pe.edu.pucp.packetsoft.models;

public class Pais extends BaseEntity{

    private String nombre;
    private int zona_horaria;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getZona_horaria() {
        return zona_horaria;
    }

    public void setZona_horaria(int zona_horaria) {
        this.zona_horaria = zona_horaria;
    }
}
