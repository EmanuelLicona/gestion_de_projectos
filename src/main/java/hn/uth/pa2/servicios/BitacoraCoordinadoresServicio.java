/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.servicios;

import hn.uth.pa2.modelos.BitacoraCoordinadores;
import hn.uth.pa2.repositorios.BitacoraCoordinadoresRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Buddys
 */
@Service
public class BitacoraCoordinadoresServicio {
    
    @Autowired
    private BitacoraCoordinadoresRepositorio repositorio;
    
    public BitacoraCoordinadores guardar(BitacoraCoordinadores entidad) {
        return repositorio.save(entidad);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }
    
    public Optional<BitacoraCoordinadores> getValor(Long id) {
        return repositorio.findById(id);
    }
    
    public List<BitacoraCoordinadores> getTodos() {
        return (List<BitacoraCoordinadores>) repositorio.findAll();
    }
}
