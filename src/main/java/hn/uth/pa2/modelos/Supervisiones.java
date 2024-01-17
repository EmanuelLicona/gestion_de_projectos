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
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Buddys
 */
@Data
@NoArgsConstructor
@Entity
public class Supervisiones implements Serializable  {
    private static final long serialVersionUID = -6833167247955613395L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "id_supervision")
    private Long idSupervision;
    
    private String objetivo;
    
    private String observasiones;
    
    private Date fecha;
    
    @JoinColumn(name = "hora_inicio")
    private String horaInicio;
    
    @JoinColumn(name = "hora_fin")
    private String horaFin;
}
