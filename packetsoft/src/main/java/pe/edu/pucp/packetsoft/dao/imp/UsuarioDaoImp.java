package pe.edu.pucp.packetsoft.dao.imp;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pe.edu.pucp.packetsoft.dao.UsuarioDao;
import pe.edu.pucp.packetsoft.models.Usuario;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

@Transactional
@Repository
@SuppressWarnings({"unchecked", "deprecation"})
public class UsuarioDaoImp implements UsuarioDao {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @Override
    public List<Usuario> getAll(){
        List<Usuario> resultado = null;
        
        try{
            String hql = "FROM Usuario as u ";
            resultado =  entityManager.createQuery(hql).getResultList();
        }catch(Exception ex){ System.out.println(ex.getMessage()); }

        return resultado;
    }

    @Transactional
    @Override
    public Usuario get(int id){
        Usuario resultado = null;

        try{
            resultado = entityManager.find(Usuario.class, id);
        }catch(Exception ex){ System.out.println(ex.getMessage()); }

        return resultado;
    }

    @Transactional
    @Override
    public Usuario register(Usuario user){
        Usuario usuario = null;

        try{
            usuario = entityManager.merge(user);
        }
        catch(Exception ex){ System.out.println(ex.getMessage()); }

        return usuario;
    }

    @Transactional
    @Override
    public Usuario update(Usuario usuario){
        Usuario resultado = null;
        
        try{
            //Actualiza todo menos el password y proveedor
            Usuario nuevo = get(usuario.getId());
            nuevo.setUsuario(usuario.getUsuario());
            nuevo.setPersona(usuario.getPersona());
            resultado = entityManager.merge(nuevo);;
        }
        catch(Exception ex){ System.out.println(ex.getMessage()); }

        return resultado;
    }

    @Transactional
    @Override
    public boolean updatePassword(int id_usuario, String username, String old_password, String new_password){
        boolean resultado = false;
        
        try{
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            Usuario user = get(id_usuario);

            // Validar que el username y old password coincidan con el usuario actual
            if(user.getUsuario().compareTo(username) == 0 &&  argon2.verify(user.getPassword(), old_password)){
                //Hashear el nuevo password y actualizar el usuario
                user.setPassword(argon2.hash(1, 1024*1, 1, new_password));
                if(user.getFecha_modificacion() != null)
                    user.setFecha_modificacion(new Date());

                entityManager.merge(user);
                resultado = true;
            }
        }
        catch(Exception ex){ System.out.println(ex.getMessage()); }

        return resultado;
    }

    @Transactional
    @Override
    public boolean forgotPassword(String username, String generated_password){
        boolean resultado = false;

        try{
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            String hql = "FROM Usuario WHERE usuario = :usuario and proveedor = 0";
            List<Usuario> result = entityManager.createQuery(hql).setParameter("usuario", username).getResultList();

            //Si el usuario no existe, entra a excepcion y retorna falso
            Usuario user = result.get(0);
            
            //Actualizar el password
            user.setPassword(argon2.hash(1, 1024*1, 1, generated_password));
            if(user.getFecha_modificacion() != null)
                user.setFecha_modificacion(new Date());

            //Actualizar al usuario
            entityManager.merge(user);

            resultado = true;
        }
        catch(Exception ex){ System.out.println(ex.getMessage()); }

        return resultado;
    }

    @Transactional
    @Override
    public void delete(int id){
        Usuario user = get(id);

        try{
            entityManager.remove(user);
        }

        catch(Exception ex){ System.out.println(ex.getMessage()); }
    }

    @Transactional
    @Override
    public Usuario login(Usuario dto) {
        boolean isAuthenticated = false;
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        
        String hql = "FROM Usuario as u WHERE u.password is not null and u.usuario = :usuario and proveedor = 0";
        
        List<Usuario> result = entityManager.createQuery(hql).setParameter("usuario", dto.getUsuario()).getResultList();
        
        if(result.size() == 0) return null;

        Usuario user = result.get(0);
        if(!StringUtils.isEmpty(dto.getPassword())){
            isAuthenticated = argon2.verify(user.getPassword(), dto.getPassword());
        }

        if(isAuthenticated) return user;

        return null;
    }

    @Transactional
    @Override
    public Usuario getGoogleUserByUsername(String usuario) {
        Usuario user=null;

        try{
            String hql = "FROM Usuario u WHERE u.usuario = :usuario";
            List<Usuario> lista=entityManager.createQuery(hql).setParameter("usuario",usuario).getResultList();
            user=lista.get(0);
        }catch(Exception ex){ System.out.println(ex.getMessage()); }

        return user;
    }

    @Transactional
    @Override
    public boolean duplicadoExterno(String usuario, int proveedor) {
        boolean resultado = true;

        try {
            String hql = "select count(*) from Usuario where usuario = '" + usuario + "' and proveedor <> " + proveedor;
            long cantidad = (long)entityManager.createQuery(hql).getSingleResult();

            if(cantidad == 0)
                resultado = false;

        } catch (Exception ex) { System.out.println(ex.getMessage()); }

        return resultado;
    }

    @Transactional
    @Override
    public boolean duplicadoPropio(String usuario, int id) {
        boolean resultado = true;

        try {
            String hql = "select count(*) from Usuario where usuario = '" + usuario + "' and id <> " + id;
            long cantidad = (long)entityManager.createQuery(hql).getSingleResult();

            if(cantidad == 0)
                resultado = false;
                
        } catch (Exception ex) { System.out.println(ex.getMessage()); }

        return resultado;
    }

    
}
