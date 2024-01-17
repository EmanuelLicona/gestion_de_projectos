/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.modelos;

import java.io.Serializable;
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
 * @author Buddys
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "proyectos_coordinadores")
public class ProyectoCoordinadores implements Serializable {
    private static final long serialVersionUID = -6833167247955613395L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto")
    private Proyectos idProyecto;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_coordinador")
    private TipoCoordinadores idTipoCoordinador;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;
    
}
