/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.servicios;

import hn.uth.pa2.modelos.Usuario;
import hn.uth.pa2.repositorios.UsuarioRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author Licona
 */
@Service
public class UsuarioServicio {

    @Autowired //Inyeccion para una nueva instancia, lo hace de forma implicita
    private UsuarioRepositorio repositorio;

    public Usuario guardar(Usuario entidad) {
        return repositorio.save(entidad);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }

    //Optional verifica si el metodo nos esta devolviendo algo es como un booleano
    public Optional<Usuario> getValor(Long id) {//Buscar 
        return repositorio.findById(id);
    }

    public List<Usuario> getTodos() {
        return (List<Usuario>) repositorio.findAll();
    }

    public List<Usuario> getUsuariosConsulta(String nombreRol) {
        return (List<Usuario>) repositorio.listarUsuarioConsulta(nombreRol);
    }
    
    public List<Usuario> getUsuariosCoordinadores(Long idProyecto) {
        return (List<Usuario>) repositorio.listarUsuarioCoordinadores(idProyecto);
    }

    public Usuario getLoggedUser() throws Exception {
        //Obtener el usuario logeado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDetails loggedUser = null;

        //Verificar que ese objeto traido de sesion es el usuario
        if (principal instanceof UserDetails) {
            loggedUser = (UserDetails) principal;
        }
        
        Usuario myUser = repositorio
                .findByUsername(loggedUser.getUsername()).orElseThrow(() -> new Exception(""));
        return myUser;
    }
}
