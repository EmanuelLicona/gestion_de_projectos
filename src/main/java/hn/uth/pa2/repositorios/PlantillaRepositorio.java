/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.repositorios;

import hn.uth.pa2.modelos.Plantilla;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Licona
 */
public interface PlantillaRepositorio extends CrudRepository<Plantilla, Long>{
    @Query(value = "SELECT * FROM PLANTILLA P, TIPO_PLANTILLA TP WHERE P.ID_TIPO_PLANTILLA = TP.ID_TIPO_PLANTILLA AND TP.NOMBRE = ?1", nativeQuery = true)
    List<Plantilla> listarTipoPlantilla(String tipo);
}
