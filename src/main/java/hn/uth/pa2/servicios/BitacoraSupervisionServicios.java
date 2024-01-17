/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.servicios;

import hn.uth.pa2.modelos.BitacoraSupervision;
import hn.uth.pa2.repositorios.BitacoraSupervisionRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Buddys
 */
@Service
public class BitacoraSupervisionServicios {
    @Autowired
    private BitacoraSupervisionRepositorio repositorio;
    
    public List<BitacoraSupervision> getTodos() {
        return (List<BitacoraSupervision>) repositorio.findAll();
    }
    
    public BitacoraSupervision guardar(BitacoraSupervision entidad) {
        return repositorio.save(entidad);
    }
}
