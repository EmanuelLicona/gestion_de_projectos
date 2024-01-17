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
@Table(name = "bitacora_supervision")
public class BitacoraSupervision implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "id_bitacora_supervision")
    private Long idBitacoraSupervision;
    
    @JoinColumn(name = "fecha_ejecucion")
    private Date fechaEjecucion;
    
    private String usuario;
}
