/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.servicios;

import hn.uth.pa2.modelos.ProyectoEvaluacion;
import hn.uth.pa2.repositorios.ProyectoEvaluacionRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Licona
 */

@Service
public class ProyectoEvaluacionServicio {
    @Autowired
    private ProyectoEvaluacionRepositorio repositorio;
    
    public ProyectoEvaluacion guardar(ProyectoEvaluacion entidad) {
        return repositorio.save(entidad);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }
    
    public Optional<ProyectoEvaluacion> getValor(Long id) {
        return repositorio.findById(id);
    }
    
    public List<ProyectoEvaluacion> getTodos() {
        return (List<ProyectoEvaluacion>) repositorio.findAll();
    }
    
    public List<ProyectoEvaluacion> getPorIdProyecto(Long idProyecto) {
        return (List<ProyectoEvaluacion>) repositorio.getEvaluacioenesPorProyecto(idProyecto);
    }
    
    public List<ProyectoEvaluacion> getPorIdProyectoAndPlantilla(Long idProyecto, Long idPlantilla) {
        return (List<ProyectoEvaluacion>) repositorio.getEvaluacioenesPorProyectoAndPlantila(idProyecto, idPlantilla);
    }
    
    public List<ProyectoEvaluacion> getCalificacionPorCoordinador(Long idUsuario, Long idProyecto) {
        return repositorio.getCalificacionCoordinador(idUsuario, idProyecto);
    }
    
    public List<ProyectoEvaluacion> getCalificacionPorTipoEvaluacion(Long idTipoCalificacion, Long idProyecto) {
        return repositorio.getCalificacionCoordinador2(idTipoCalificacion, idProyecto);
    }
    
}
