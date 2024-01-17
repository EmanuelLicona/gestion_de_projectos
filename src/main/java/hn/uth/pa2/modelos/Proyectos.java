/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.modelos;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Buddys
 */
@Data
@NoArgsConstructor
@Entity
public class Proyectos implements Serializable {
    private static final long serialVersionUID = -6833167247955613395L;
    
    @Transient
    java.util.Date d = new java.util.Date();
    
    @Transient
    java.sql.Date fecha = new java.sql.Date(d.getTime());
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "id_proyecto")
    private Long idProyecto;
    
    @JoinColumn(name = "nombre_lider")
    private String nombreLider;
    
    @JoinColumn(name = "identidad_lider")
    private String identidadLider;
    
    private String titulo;
    
    @ManyToOne
    @JoinColumn(name = "id_departamento")
    private Departamento idDepartamento;
    
    @ManyToOne
    @JoinColumn(name = "id_plantilla_profesional")
    private Plantilla idPlantillaProfesional;
    
    @ManyToOne
    @JoinColumn(name = "id_plantilla_tecnico")
    private Plantilla idPlantillaTecnico;
    
    @ManyToOne
    @JoinColumn(name = "id_plantilla_general")
    private Plantilla idPlantillaGeneral;
    
    @JoinColumn(name = "calificacion_profesional")
    private double calificacionProfesional; 
    
    @JoinColumn(name = "calificacion_tecnico")
    private double calificacionTecnico; 
    
    @JoinColumn(name = "calificacion_general")
    private double calificacionGeneral; 
    
    private String estado;
    
    @Transient
    private Long idUsuario1; 
    
    @Transient
    private Long idUsuario2; 
    
    @Transient
    private Long idUsuario3; 
    
    @Transient
    private Long idCoordinadorProfesional; 
    @Transient
    private Long idCoordinadorTecnico; 
    
    @Transient
    private Long idCoordinadorGeneral; 
    
    @Transient
    private String justificacion;
    
    @Transient
    private Date fechaCalificacionReporte = fecha;
    

    public Proyectos(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Proyectos(String estado, Long idProyecto) {
        this.estado = estado;
        this.idProyecto = idProyecto;
    }

}
