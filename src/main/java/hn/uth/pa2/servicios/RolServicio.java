/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.servicios;

import hn.uth.pa2.modelos.Rol;
import hn.uth.pa2.repositorios.RolRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Licona
 */
@Service
public class RolServicio {
    
    @Autowired //Inyeccion para una nueva instancia, lo hace de forma implicita
    private RolRepositorio repositorio;
    
    public Rol guardar(Rol entidad) {
        return repositorio.save(entidad);
    }
    
    public void eliminar(Long id){
        repositorio.deleteById(id);
    }
    
    //Optional verifica si el metodo nos esta devolviendo algo es como un booleano
    public Optional<Rol> getValor(Long id) {//Buscar 
        return repositorio.findById(id);
    }
    
    public List<Rol> getTodos() {
        return (List<Rol>) repositorio.findAll();
    }
}
