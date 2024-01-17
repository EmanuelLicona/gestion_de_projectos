/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.controladores;

import hn.uth.pa2.modelos.Criterio;
import hn.uth.pa2.modelos.Plantilla;
import hn.uth.pa2.modelos.ProyectoEvaluacion;
import hn.uth.pa2.modelos.TipoEvaluacion;
import hn.uth.pa2.servicios.CriterioServicio;
import hn.uth.pa2.servicios.PlantillaServicio;
import hn.uth.pa2.servicios.ProyectoEvaluacionServicio;
import hn.uth.pa2.servicios.TipoEvaluacionServicio;
import hn.uth.pa2.servicios.TipoPlantillaServicio;
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
public class PlantillaUIControlador {

    /**
     * ****************************************
     */
    @Autowired
    private PlantillaServicio servicioPlantilla;

    @Autowired
    private TipoPlantillaServicio servicioTipoPlantilla;

    @Autowired
    private TipoEvaluacionServicio tipoEvaluacionServicio;

    @Autowired
    private CriterioServicio criterioServicio;

    @Autowired
    private ProyectoEvaluacionServicio servicioProyectoEvaluacion;

    @RequestMapping("/mantenimientoPlantilla")
    public String mantenimientoUsuario(Model model, RedirectAttributes attribute) {

        setParametro(model, "listaPlantillas", servicioPlantilla.getTodos());

        return "paginas/Plantillas/mantenimiento_plantilla";

    }

    @GetMapping("/crear_plantilla")
    public String irCrear(Model model) throws Exception {

        setParametro(model, "listaTipoPlantilla", servicioTipoPlantilla.getTodos());
        setParametro(model, "plantilla", new Plantilla());

        return "paginas/Plantillas/form_plantilla";
    }

    @PostMapping("/guardar_plantilla")
    public String guardar(Plantilla entidad, Model model, RedirectAttributes attribute) {
        

        for (Plantilla object : servicioPlantilla.getTodos()) {
            if (entidad.getTitulo().equalsIgnoreCase(object.getTitulo()) && !object.getIdPlantilla().equals(entidad.getIdPlantilla())) {
                attribute.addFlashAttribute("error", "La plantilla no puede ser guardada, el titulo ya existe");
                return "redirect:/mantenimientoPlantilla";
            }
        }

        for (ProyectoEvaluacion object : servicioProyectoEvaluacion.getTodos()) {
            if (object.getIdPlantilla().getIdPlantilla() == entidad.getIdPlantilla()) {
                attribute.addFlashAttribute("error", "La plantilla no puede ser editada, la plantilla se esta utilizando");
                return "redirect:/mantenimientoPlantilla";
            }
        }

        servicioPlantilla.guardar(entidad);
        attribute.addFlashAttribute("success", "Guardado correctamente");
        return "redirect:/mantenimientoPlantilla";
    }

    @GetMapping("/actualizar_plantilla/{id}")
    public String irActualizar(@PathVariable("id") Long id, Model model, RedirectAttributes attribute) {

        if (servicioPlantilla.getValor(id).get().getCriterios().isEmpty() == false) {
            //attribute.addFlashAttribute("error", "No se puede editar ya que contiene");
            //return "redirect:/mantenimientoPlantilla";
        }

        for (ProyectoEvaluacion object : servicioProyectoEvaluacion.getTodos()) {
            if (object.getIdPlantilla().getIdPlantilla() == id) {
                attribute.addFlashAttribute("error", "La plantilla no puede ser editada, la plantilla se esta utilizando");
                return "redirect:/mantenimientoPlantilla";
            }
        }

        setParametro(model, "listaTipoPlantilla", servicioTipoPlantilla.getTodos());
        setParametro(model, "plantilla", servicioPlantilla.getValor(id));

        return "paginas/Plantillas/form_plantilla";
    }

    @GetMapping("eliminar_plantilla/{id}")
    public String eliminar(@PathVariable("id") Long id, Model modelo, RedirectAttributes attribute) {

        if (!servicioPlantilla.getValor(id).get().getCriterios().isEmpty()) {

            attribute.addFlashAttribute("error", "La plantilla no puede ser eliminada, la plantilla tiene criterios añadidos");
            return "redirect:/mantenimientoPlantilla";
        }

        for (ProyectoEvaluacion object : servicioProyectoEvaluacion.getTodos()) {
            if (object.getIdPlantilla().getIdPlantilla() == id) {
                attribute.addFlashAttribute("error", "La plantilla no puede ser eliminada, la plantilla se esta utilizando");
                return "redirect:/mantenimientoPlantilla";
            }

        }

        attribute.addFlashAttribute("success", "eliminado correctamente");
        servicioPlantilla.eliminar(id);
        return "redirect:/mantenimientoPlantilla";
    }

    @GetMapping("/anadirPlantillaCriterio/{id}")
    public String irAnadir(@PathVariable("id") Long id, Model model, RedirectAttributes attribute) throws Exception {

        setParametro(model, "plantilla", servicioPlantilla.getValor(id).get());

        for (TipoEvaluacion object : tipoEvaluacionServicio.getTodos()) {
            if (object.getNombre().equalsIgnoreCase(servicioPlantilla.getValor(id).get().getTipoPlantilla().getNombre())) {
                setParametro(model, "listaCriterios", criterioServicio.getCriteriosPorTipo(object.getIdEvaluacion()));
                break;
            }
        }

        return "paginas/Plantillas/form_plantilla_criterio";
    }

    @PostMapping("/guardar_plantillaCriterio")
    public String guardarCriterios(Plantilla entidad, Model model, RedirectAttributes attribute) {

        servicioPlantilla.guardar(entidad);
        attribute.addFlashAttribute("success", "Guardado correctamente");

        System.out.println("Paso hasta aqui");

        return "redirect:/mantenimientoPlantilla";
    }

    public void setParametro(Model model, String atributo, Object valor) {
        model.addAttribute(atributo, valor);
    }

}
