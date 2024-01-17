/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.servicios;

import hn.uth.pa2.modelos.TipoPlantilla;
import hn.uth.pa2.repositorios.TipoPlantillaRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Licona
 */
@Service
public class TipoPlantillaServicio {
    
    @Autowired
    private TipoPlantillaRepositorio repositorio;

    public TipoPlantilla guardar(TipoPlantilla entidad) {
        return repositorio.save(entidad);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }

    //Optional verifica si el metodo nos esta devolviendo algo es como un booleano
    public Optional<TipoPlantilla> getValor(Long id) {//Buscar 
        return repositorio.findById(id);
    }

    public List<TipoPlantilla> getTodos() {
        return (List<TipoPlantilla>) repositorio.findAll();
    }
}
