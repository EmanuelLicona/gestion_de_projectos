/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.modelos;

import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Licona
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "proyecto_evaluacion")
public class ProyectoEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plantilla")
    private Plantilla idPlantilla;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto")
    private Proyectos idProyecto;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_criterio")
    private Criterio idCriterio;

    private int calificacion;
    
    private Date fecha;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;

}
