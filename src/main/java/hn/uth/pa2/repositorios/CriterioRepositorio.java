/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.repositorios;

import hn.uth.pa2.modelos.Criterio;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Licona
 */
public interface CriterioRepositorio extends CrudRepository<Criterio, Long>{
    
    @Query(value = "SELECT * FROM criterio WHERE id_evaluacion = ?1", nativeQuery = true)
    List<Criterio> getCriteriosPorTipo(Long idEvaluacion);
}
