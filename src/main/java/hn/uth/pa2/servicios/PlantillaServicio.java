/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.servicios;

import hn.uth.pa2.modelos.Plantilla;
import hn.uth.pa2.repositorios.PlantillaRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Licona
 */
@Service
public class PlantillaServicio {
    
    @Autowired
    private PlantillaRepositorio repositorio;

    public Plantilla guardar(Plantilla entidad) {
        return repositorio.save(entidad);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }

    //Optional verifica si el metodo nos esta devolviendo algo es como un booleano
    public Optional<Plantilla> getValor(Long id) {//Buscar 
        return repositorio.findById(id);
    }

    public List<Plantilla> getTodos() {
        return (List<Plantilla>) repositorio.findAll();
    }
    
    public List<Plantilla> getTipoPlantilla(String tipo) {
        return repositorio.listarTipoPlantilla(tipo);
    }
}
