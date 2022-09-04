package pe.edu.pucp.packetsoft.services;
import java.util.List;
import pe.edu.pucp.packetsoft.dao.UsuarioDao;
import pe.edu.pucp.packetsoft.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioDao daoUsuario;

    @Cacheable(value = "usuarios")
    public List<Usuario> getAll(){
        return daoUsuario.getAll();
    }

    @Cacheable(value = "usuarios", key = "#id")
    public Usuario get(int id){
        return daoUsuario.get(id);
    }

    @CacheEvict(value = {"usuarios", "personas"}, allEntries = true, beforeInvocation = true)
    @CachePut(value = "usuarios", key = "#result.id", condition = "#result != null")
    public Usuario register(Usuario user){
        return daoUsuario.register(user);
    }

    @CacheEvict(value = {"usuarios", "personas"}, allEntries = true, beforeInvocation = true)
    @CachePut(value = "usuarios", key = "#result.id", condition = "#result != null")
    public Usuario update(Usuario usuario){
        return daoUsuario.update(usuario);
    }

    //Modificar el password no afecta la data almacenada en cache porque el password no es visible
    public boolean updatePassword(int id_usuario, String username, String old_password, String new_password){
        return daoUsuario.updatePassword(id_usuario, username, old_password, new_password);
    }

    public boolean forgotPassword(String username, String generated_password){
        return daoUsuario.forgotPassword(username, generated_password);
    }

    @CacheEvict(value = {"usuarios", "personas"}, allEntries = true, beforeInvocation = true)
    public void delete(int id){
        daoUsuario.delete(id);
    }
    
    // Jamas almacenar en cache datos de inicio de sesion
    @CachePut(value = "usuarios", key = "#result.id", condition = "#result != null")
    public Usuario login(Usuario user){
        return daoUsuario.login(user);
    }

    public boolean duplicadoExterno(String usuario, int proveedor){
        return daoUsuario.duplicadoExterno(usuario, proveedor);
    }

    public boolean duplicadoPropio(String usuario, int id){
        return daoUsuario.duplicadoPropio(usuario, id);
    }

    //Esto si es seguro pues el password lo tiene el proveedor third party
    @Cacheable(value = "usuarios", key = "'Username' + #usuario")
    public Usuario getGoogleUserByUsername(String usuario){
        return daoUsuario.getGoogleUserByUsername(usuario);
    }

}
