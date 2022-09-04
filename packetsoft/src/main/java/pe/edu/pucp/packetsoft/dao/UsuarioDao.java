package pe.edu.pucp.packetsoft.dao;

import java.util.List;

import pe.edu.pucp.packetsoft.models.Usuario;

public interface UsuarioDao{
    public List<Usuario> getAll();
    public Usuario get(int id);
    public Usuario register(Usuario usuario);
    public Usuario update(Usuario usuario);
    public boolean updatePassword(int id_usuario, String username, String old_password, String new_password); 
    public boolean forgotPassword(String username, String generated_password);
    public void delete(int id);
    public Usuario login(Usuario dto);
    public Usuario getGoogleUserByUsername(String usuario);
    public boolean duplicadoExterno(String usuario, int proveedor);
    public boolean duplicadoPropio(String usuario, int id);
}
