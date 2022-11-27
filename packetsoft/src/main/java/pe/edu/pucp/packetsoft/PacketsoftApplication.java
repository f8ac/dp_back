package pe.edu.pucp.packetsoft;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import pe.edu.pucp.packetsoft.models.Aeropuerto;
import pe.edu.pucp.packetsoft.models.Envio;
import pe.edu.pucp.packetsoft.models.Movimiento;
import pe.edu.pucp.packetsoft.utils.PaquetesComp;

// import pe.edu.pucp.packetsoft.utils.AstarNode;
// import pe.edu.pucp.packetsoft.utils.AstarSearch;

@SpringBootApplication
@EnableCaching
public class PacketsoftApplication {

    public static List<Aeropuerto> listaAeropuertos = new ArrayList<>();
    public static Hashtable<String,Aeropuerto> aeroHash = new Hashtable<>();
    public static Comparator<Movimiento> comPaquetes = new PaquetesComp();
    public static Envio envioCol = new Envio();
    public static PriorityQueue<Movimiento>colaPaquetes = new PriorityQueue<Movimiento>(comPaquetes);
    public static List<Envio> listaEnvios = new ArrayList<>();
    public static List<Envio> neededEnvios = new ArrayList<>();
	public static void main(String[] args) {
        
		SpringApplication.run(PacketsoftApplication.class, args);

    // SAMPLE ASTAR
        // AstarNode head = new AstarNode(3);
        // head.g = 0;
        // AstarNode n1 = new AstarNode(2);
        // AstarNode n2 = new AstarNode(2);
        // AstarNode n3 = new AstarNode(2);
        // head.addBranch(1, n1);
        // head.addBranch(5, n2);
        // head.addBranch(2, n3);
        // n3.addBranch(1, n2);
        // AstarNode n4 = new AstarNode(1);
        // AstarNode n5 = new AstarNode(1);
        // AstarNode target = new AstarNode(0);
        // n1.addBranch(7, n4);
        // n2.addBranch(4, n5);
        // n3.addBranch(6, n4);
        // n4.addBranch(3, target);
        // n5.addBranch(1, n4);
        // n5.addBranch(3, target);
        // AstarNode res = AstarSearch.aStar(head, target);
        // AstarSearch.printPath(res);
	}

	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST","PUT", "DELETE");
            }
        };
    }

}
