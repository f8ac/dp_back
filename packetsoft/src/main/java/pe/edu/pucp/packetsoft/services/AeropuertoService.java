package pe.edu.pucp.packetsoft.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.pucp.packetsoft.dao.AeropuertoDao;
import pe.edu.pucp.packetsoft.models.Aeropuerto;

@Service
public class AeropuertoService {
    @Autowired
    private AeropuertoDao daoAeropuerto;

    public Aeropuerto insert(Aeropuerto aeropuerto){
        return daoAeropuerto.insert(aeropuerto);
    }

}
