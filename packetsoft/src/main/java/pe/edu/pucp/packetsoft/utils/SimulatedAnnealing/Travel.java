package pe.edu.pucp.packetsoft.utils.SimulatedAnnealing;

import java.util.ArrayList;
import java.util.Collections;

import lombok.Data;
import pe.edu.pucp.packetsoft.models.Aeropuerto;

@Data
public class Travel {
    private ArrayList<Aeropuerto> travel = new ArrayList<>();
    private ArrayList<Aeropuerto> previousTravel = new ArrayList<>();

    public Travel(int numberOfCities) {
        for (int i = 0; i < numberOfCities; i++) {
            travel.add(new Aeropuerto());
        }
    }

    public void generateInitialTravel() {
        if (travel.isEmpty()) {
            new Travel(10);
        }
        Collections.shuffle(travel);
    }

    public void swapCities() {
        int a = generateRandomIndex();
        int b = generateRandomIndex();
        previousTravel = new ArrayList<>(travel);
        Aeropuerto x = travel.get(a);
        Aeropuerto y = travel.get(b);
        travel.set(a, y);
        travel.set(b, x);
    }

    private int generateRandomIndex() {
        return (int) (Math.random() * travel.size());
    }

    public Aeropuerto getAeropuerto(int index) {
        return travel.get(index);
    }

    public int getDistance() {
        int distance = 0;
        for (int index = 0; index < travel.size(); index++) {
            Aeropuerto starting = getAeropuerto(index);
            Aeropuerto destination;
            if (index + 1 < travel.size()) {
                destination = getAeropuerto(index + 1);
            } else {
                destination = getAeropuerto(0);
            }
            distance += starting.getAeropuerto(destination);
        }
        return distance;
    }

}
