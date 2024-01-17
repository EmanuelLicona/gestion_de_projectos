/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.controladores;

import hn.uth.pa2.servicios.BitacoraCalificacionServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Buddys
 */
@Controller
public class BtcCalificacionUIControlador {
    
    @Autowired
    private BitacoraCalificacionServicios servicio;

    @RequestMapping("/mantenimientoBitacoraCalificacion")
    public String irServicios(Model model) {
        setParametro(model, "listaBitacoraCalificacion", servicio.getTodos());
        return "paginas/bitacoras/mantenimiento-bitacora-calificacion";
    }
   
    public void setParametro(Model model, String atributo, Object valor) {
        model.addAttribute(atributo, valor);
    }
}
