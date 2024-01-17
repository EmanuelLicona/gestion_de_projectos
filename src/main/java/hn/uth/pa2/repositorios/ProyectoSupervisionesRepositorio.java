/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.repositorios;

import hn.uth.pa2.modelos.ProyectoSupervisiones;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Buddys
 */
public interface ProyectoSupervisionesRepositorio extends CrudRepository<ProyectoSupervisiones, Long> {
    @Query(value = "SELECT * FROM PROYECTOS_SUPERVISIONES PS, PROYECTOS P WHERE PS.ID_PROYECTO = P.ID_PROYECTO AND UPPER(P.TITULO) LIKE %?1%", nativeQuery = true)
    List<ProyectoSupervisiones> findByIdProyectoTitulo(String titulo);
    
    @Query(value = "SELECT * FROM PROYECTOS_SUPERVISIONES PS, TIPO_COORDINADORES TC WHERE PS.ID_TIPO_COORDINADOR = TC.ID_TIPO_COORDINADOR AND PS.ID_PROYECTO = ?1 AND TC.NOMBRE = ?2", nativeQuery = true)
    List<ProyectoSupervisiones> getReporteSupervision(Long idProyecto, String tipoCoordinador);
    
    @Query(value = "SELECT * FROM PROYECTOS_SUPERVISIONES PS, TIPO_COORDINADORES TC WHERE PS.ID_TIPO_COORDINADOR = TC.ID_TIPO_COORDINADOR AND PS.ID_PROYECTO = ?1", nativeQuery = true)
    List<ProyectoSupervisiones> getReporteSupervisionEvaluacion(Long idProyecto);
    
    @Query(value = "SELECT * FROM PROYECTOS_SUPERVISIONES PS WHERE PS.ID_USUARIO = ?1", nativeQuery = true)
    List<ProyectoSupervisiones> getListarSupervisiones(Long idUsuario);
}
