/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.modelos;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Buddys
 */
@Data
@NoArgsConstructor
@Entity
public class Departamento implements Serializable {
    private static final long serialVersionUID = -6833167247955613395L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "id_departamento")
    private Long idDepartamento;
    private String nombre;
    private String descripcion;
    private String estado;
}
