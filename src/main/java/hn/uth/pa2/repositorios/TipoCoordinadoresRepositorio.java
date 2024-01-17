/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.repositorios;

import hn.uth.pa2.modelos.TipoCoordinadores;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Buddys
 */
public interface TipoCoordinadoresRepositorio extends CrudRepository<TipoCoordinadores, Long> {    
    @Query(value = "SELECT * FROM TIPO_COORDINADORES R WHERE UPPER(R.NOMBRE) = UPPER(?1)", nativeQuery = true)
    Iterable<TipoCoordinadores> listarTipoCoordinador(String nombreCoordinador);
}
