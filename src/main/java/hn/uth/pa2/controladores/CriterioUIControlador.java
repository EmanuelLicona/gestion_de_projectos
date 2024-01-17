/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.controladores;

import hn.uth.pa2.modelos.Criterio;
import hn.uth.pa2.modelos.Plantilla;
import hn.uth.pa2.modelos.ProyectoEvaluacion;
import hn.uth.pa2.modelos.Usuario;
import hn.uth.pa2.servicios.CriterioServicio;
import hn.uth.pa2.servicios.PlantillaServicio;
import hn.uth.pa2.servicios.ProyectoEvaluacionServicio;
import hn.uth.pa2.servicios.TipoEvaluacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Licona
 */
@Controller
public class CriterioUIControlador {

    @Autowired
    private TipoEvaluacionServicio tipoEvaluacionServicio;

    @Autowired
    private CriterioServicio servicioCriterio;

    @Autowired
    private ProyectoEvaluacionServicio servicioProyectoEvaluacion;

    @Autowired
    private PlantillaServicio servicioPlantilla;

    @RequestMapping({"/mantenimiento_criterio"})
    public String index(Model model) {
        setParametro(model, "lista_criterios", servicioCriterio.getTodos());

        return "Paginas/Criterios/mantenimiento_criterios";
    }

    @GetMapping("/crear_criterio")
    public String irCrear(Model model, RedirectAttributes attribute) throws Exception {
        setParametro(model, "listaTipoEvaluacion", tipoEvaluacionServicio.getTodos());
        setParametro(model, "criterio", new Criterio());
        return "Paginas/Criterios/form_criterios";
    }

    @PostMapping("/guardar_criterio")
    public String guardar(Criterio entidad, Model model, RedirectAttributes attribute) {

        if (entidad.getPunt_maximo() <= 0) {
            attribute.addFlashAttribute("error", "Puntaje maximo no valido");
            return "redirect:/crear_criterio";
        }

        if (entidad.getPunt_minimo() < 0) {
            attribute.addFlashAttribute("error", "Puntaje minimo no valido");
            return "redirect:/crear_criterio";
        }

        servicioCriterio.guardar(entidad);
        attribute.addFlashAttribute("success", "Guardado correctamente");

        return "redirect:/mantenimiento_criterio";
    }

    @GetMapping("/actualizar_criterio/{id}")
    public String irActualizar(@PathVariable("id") Long id, Model model, RedirectAttributes attribute) {

        for (ProyectoEvaluacion object : servicioProyectoEvaluacion.getTodos()) {
            if (object.getIdCriterio().getIdCriterio() == id) {
                attribute.addFlashAttribute("error", "El criterio no se puede editar ya que esta siendo utilizado");
                return "redirect:/mantenimiento_criterio";
            }
        }

        setParametro(model, "listaTipoEvaluacion", tipoEvaluacionServicio.getTodos());
        setParametro(model, "criterio", servicioCriterio.getValor(id));

        return "Paginas/Criterios/form_criterios";
    }

    @GetMapping("eliminar_criterio/{id}")
    public String eliminar(@PathVariable("id") Long id, Model modelo, RedirectAttributes attribute) {

        for (ProyectoEvaluacion object : servicioProyectoEvaluacion.getTodos()) {
            if (object.getIdCriterio().getIdCriterio().equals(id)) {
                attribute.addFlashAttribute("error", "El criterio no se puede eliminar ya que esta siendo utilizado");
                return "redirect:/mantenimiento_criterio";
            }
        }

        for (Plantilla plantilla : servicioPlantilla.getTodos()) {
            for (Criterio cri : plantilla.getCriterios()) {
                if (cri.getIdCriterio().equals(id)) {
                    attribute.addFlashAttribute("error", "El criterio no se puede eliminar ya que esta siendo utilizado");
                    return "redirect:/mantenimiento_criterio";
                }
            }

        }

        attribute.addFlashAttribute("success", "eliminado correctamente");
        servicioCriterio.eliminar(id);
        return "redirect:/mantenimiento_criterio";
    }

    public void setParametro(Model model, String atributo, Object valor) {
        model.addAttribute(atributo, valor);
    }
}
