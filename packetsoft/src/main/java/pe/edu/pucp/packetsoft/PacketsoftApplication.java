package pe.edu.pucp.packetsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import pe.edu.pucp.packetsoft.utils.AstarNode;

@SpringBootApplication
@EnableCaching
public class PacketsoftApplication {

	public static void main(String[] args) {
        System.out.println(">antes");
        int numero = 5;
		SpringApplication.run(PacketsoftApplication.class, args);
        System.out.println("<despues");
        System.out.println(numero);

        AstarNode head = new AstarNode(3);
        
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
