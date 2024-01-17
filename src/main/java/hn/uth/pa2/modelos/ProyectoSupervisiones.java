/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.modelos;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
@Table(name = "proyectos_supervisiones")
public class ProyectoSupervisiones implements Serializable {

    private static final long serialVersionUID = -6833167247955613395L;
    @Transient
    java.util.Date d = new java.util.Date();
    
    @Transient
    java.sql.Date fecha = new java.sql.Date(d.getTime());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto")
    private Proyectos idProyecto;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_supervision")
    private Supervisiones idSupervision;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_coordinador")
    private TipoCoordinadores idTipoCoordinador;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    
    @Transient
    private Date fechaActual = fecha;
}
