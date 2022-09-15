package pe.edu.pucp.packetsoft.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.dao.EnvioDao;
import pe.edu.pucp.packetsoft.models.Envio;


@Service
public class EnvioService {
    @Autowired
    private EnvioDao daoEnvio;

    public List<Envio> getAll(){
        return daoEnvio.getAll();
    }

    public Envio insert(Envio envio){
        return daoEnvio.insert(envio);
    }

    public void insertfile() throws IOException, InterruptedException {
    }

}
