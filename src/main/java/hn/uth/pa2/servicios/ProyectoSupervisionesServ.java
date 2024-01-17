/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.servicios;

import hn.uth.pa2.modelos.ProyectoSupervisiones;
import hn.uth.pa2.repositorios.ProyectoSupervisionesRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Buddys
 */
@Service
public class ProyectoSupervisionesServ {

    @Autowired
    private ProyectoSupervisionesRepositorio repositorio;

    public ProyectoSupervisiones guardar(ProyectoSupervisiones entidad) {
        return repositorio.save(entidad);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }

    public Optional<ProyectoSupervisiones> getValor(Long id) {
        return repositorio.findById(id);
    }

    public List<ProyectoSupervisiones> getTodos() {
        return (List<ProyectoSupervisiones>) repositorio.findAll();
    }
    
    public List<ProyectoSupervisiones> getListarSupervisiones(Long idUsuario) {
        return repositorio.getListarSupervisiones(idUsuario);
    }

    public List<ProyectoSupervisiones> getResultadoBusqueda(String titulo) {
        return repositorio.findByIdProyectoTitulo(titulo);
    }

    public List<ProyectoSupervisiones> getReporteProyecto(Long idProyecto, String tipoCoordinador) {
        return repositorio.getReporteSupervision(idProyecto, tipoCoordinador);
    }

    public List<ProyectoSupervisiones> getReporteProyectoEvaluaciones(Long idProyecto) {
        return repositorio.getReporteSupervisionEvaluacion(idProyecto);
    }
}
