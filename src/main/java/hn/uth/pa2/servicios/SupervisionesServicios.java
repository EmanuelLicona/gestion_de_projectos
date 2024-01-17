/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.servicios;

import hn.uth.pa2.modelos.Supervisiones;
import hn.uth.pa2.repositorios.SupervisionRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Buddys
 */
@Service
public class SupervisionesServicios {
    
    @Autowired
    private SupervisionRepositorio repositorio;
    
    public Supervisiones guardar(Supervisiones entidad) {
        return repositorio.save(entidad);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }
    
    public Optional<Supervisiones> getValor(Long id) {
        return repositorio.findById(id);
    }
    
    public List<Supervisiones> getTodos() {
        return (List<Supervisiones>) repositorio.findAll();
    }
    
}
