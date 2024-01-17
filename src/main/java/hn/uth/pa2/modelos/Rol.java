/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.modelos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Licona
 */
@Data 
@NoArgsConstructor 
@Entity 
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_rol;
    private String nombre;
    private String descripcion;
}
