/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.modelos;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Licona
 */

@Data
@NoArgsConstructor
@Entity
public class Plantilla implements Serializable {
    private static final long serialVersionUID = -6833167247955613395L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "id_plantilla")
    private Long idPlantilla;
    private String titulo;
    private String descripcion;
    
    
    @JoinColumn(name = "id_tipoPlantilla", unique = false)
    @OneToOne(fetch = FetchType.LAZY)
    private TipoPlantilla tipoPlantilla;
    
    
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name="plantilla_criterios",
                joinColumns = {@JoinColumn(name = "id_plantilla")},
                inverseJoinColumns = {@JoinColumn(name="id_criterio")}
    )
    private Set<Criterio> criterios;
    
}
