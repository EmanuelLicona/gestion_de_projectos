/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.servicios;

import hn.uth.pa2.modelos.ProyectoCoordinadores;
import hn.uth.pa2.repositorios.ProyectoCoordinadoresRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Buddys
 */
@Service
public class ProyectoCoordinadoresServicios {
    
    @Autowired
    private ProyectoCoordinadoresRepositorio repositorio;
    
    public ProyectoCoordinadores guardar(ProyectoCoordinadores entidad) {
        return repositorio.save(entidad);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }
    
    public Optional<ProyectoCoordinadores> getValor(Long id) {
        return repositorio.findById(id);
    }
    
    public List<ProyectoCoordinadores> getTodos() {
        return (List<ProyectoCoordinadores>) repositorio.findAll();
    }
    
    public List<ProyectoCoordinadores> getObtenerPorId(Long id) {
        return  repositorio.getIdProyecto(id);
    }
    
    public void eliminarProyectoCoordinadores(Long idProyecto){
        repositorio.eliminarProyectoCoordinador(idProyecto);
    }
    
    public List<ProyectoCoordinadores> seleccionarProyectoCoordinador(Long idUsuario){
        return repositorio.selectProyectoCoordinador(idUsuario);
    }
    
    public List<ProyectoCoordinadores> getUsuarioCoordindor(Long idUsuario, String coordinador){
        return repositorio.getUsuarioCoordinador(idUsuario, coordinador);
    }
    
    public void actualizarProyectoCoordinadores(Long idCoordinador, Long idUsuario ,Long idProyecto){
        repositorio.actualizarProyectoCoordinador(idCoordinador, idUsuario, idProyecto);
    }
    
    
}
