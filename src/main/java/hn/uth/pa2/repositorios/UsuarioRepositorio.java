/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.repositorios;

import hn.uth.pa2.modelos.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Licona
 */
public interface UsuarioRepositorio extends CrudRepository<Usuario, Long>{
    @Query(value = "SELECT * FROM USUARIO U, USER_ROLES UR, ROL R WHERE u.id_usuario = ur.id_usuario AND r.id_rol = ur.id_rol AND UPPER(R.NOMBRE) = UPPER(?1)", nativeQuery = true)
    Iterable<Usuario> listarUsuarioConsulta(String nombreRol);
    
    @Query(value = "SELECT * FROM USUARIO U, proyectos_coordinadores PC WHERE U.ID_USUARIO = pc.id_usuario AND PC.id_proyecto = ?1", nativeQuery = true)
    Iterable<Usuario> listarUsuarioCoordinadores(Long idProyecto);
    
    public Optional<Usuario> findByUsername(String username);
}
