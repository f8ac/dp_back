package pe.edu.pucp.packetsoft.models;

public class Aeropuerto extends BaseEntity {
    private String nombre;
    private double longitud;
    private double latitud;
    private int capacidad_total;
    private int capacidad_utilizado;
    private Pais pais;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public int getCapacidad_total() {
        return capacidad_total;
    }

    public void setCapacidad_total(int capacidad_total) {
        this.capacidad_total = capacidad_total;
    }

    public int getCapacidad_utilizado() {
        return capacidad_utilizado;
    }

    public void setCapacidad_utilizado(int capacidad_utilizado) {
        this.capacidad_utilizado = capacidad_utilizado;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }
}