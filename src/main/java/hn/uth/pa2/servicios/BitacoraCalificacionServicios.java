/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.servicios;

import hn.uth.pa2.modelos.BitacoraCalificacion;
import hn.uth.pa2.repositorios.BitacoraCalificacionRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Buddys
 */
@Service
public class BitacoraCalificacionServicios {
    @Autowired
    private BitacoraCalificacionRepositorio repositorio;
    
    public List<BitacoraCalificacion> getTodos() {
        return (List<BitacoraCalificacion>) repositorio.findAll();
    }
    
    public BitacoraCalificacion guardar(BitacoraCalificacion entidad) {
        return repositorio.save(entidad);
    }
}
