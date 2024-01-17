/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.repositorios;

import hn.uth.pa2.modelos.Proyectos;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Buddys
 */
public interface ProyectoRepositorio extends CrudRepository<Proyectos, Long> {

    @Query(value = "SELECT * FROM PROYECTOS P WHERE UPPER(P.TITULO) LIKE %?1%", nativeQuery = true)
    List<Proyectos> buscarProyecto(String titulo);
    
    @Query(value = "SELECT * FROM PROYECTOS P WHERE UPPER(P.TITULO) LIKE %?1% AND P.ESTADO = ?2", nativeQuery = true)
    List<Proyectos> buscarProyectosCalificados(String titulo, String estado);

    @Query(value = "SELECT * FROM PLANTILLA_CRITERIOS PC WHERE PC.ID_PLANTILLA = ?1", nativeQuery = true)
    List existeCriterioPlantilla(Long idPlantilla);

    @Modifying
    @Transactional
    @Query(value = "UPDATE PROYECTOS SET ESTADO = ?1 WHERE ID_PROYECTO = ?2", nativeQuery = true)
    public void finalizarProyecto(String estado, Long idProyecto);
    
    @Query(value = "SELECT * FROM PROYECTOS P WHERE P.ESTADO LIKE %?1%", nativeQuery = true)
    List<Proyectos> getProyectosFinalizados(String estado);
}
