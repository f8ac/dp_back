package pe.edu.pucp.packetsoft.services;

import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.utils.SimulatedAnnealing.Travel;

@Service
public class SimulatedAnnealingService {
    // @Autowired
    // private AeropuertoService aeropuertoService;
    // @Autowired
    // private VueloService vueloService;

    private static Travel travel = new Travel(10);

    

    
    public void ejecutarAlgoritmo(int startingTemperature, int numberOfIterations, int coolingRate) {
        System.out.println("Starting SA with temperature: " + startingTemperature + ", # of iterations: " + numberOfIterations + " and colling rate: " + coolingRate);
        double t = startingTemperature;
        travel.generateInitialTravel();
        double bestDistance = travel.getDistance();
        System.out.println("Initial distance of travel: " + bestDistance);
        Travel bestSolution = travel;
        Travel currentSolution = bestSolution;


        for (int i = 0; i < numberOfIterations; i++) {
            if (t > 0.1) {
                currentSolution.swapCities();
                double currentDistance = currentSolution.getDistance();
                if (currentDistance < bestDistance) {
                    bestDistance = currentDistance;
                } else if (Math.exp((bestDistance - currentDistance) / t) < Math.random()) {
                    //currentSolution.revertSwap();
                }
                t *= coolingRate;
            } else {
                continue;
            }
            if (i % 100 == 0) {
                System.out.println("Iteration #" + i);
            }
        }


        // List<Aeropuerto> aeropuertos = aeropuertoService.getAll();
        



    }


}
