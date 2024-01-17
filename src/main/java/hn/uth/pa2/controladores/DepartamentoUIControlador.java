/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.controladores;

import hn.uth.pa2.modelos.Departamento;
import hn.uth.pa2.modelos.Proyectos;
import hn.uth.pa2.modelos.Usuario;
import hn.uth.pa2.servicios.DepartamentoServicio;
import hn.uth.pa2.servicios.ProyectoServicios;
import hn.uth.pa2.servicios.UsuarioServicio;
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
 * @author Angel Garcia
 */
@Controller

public class DepartamentoUIControlador {

    private boolean estado_editando = false;

    @Autowired
    private DepartamentoServicio servicioDepartamento;

    @RequestMapping("/mantenimientoDepartamento")
    public String mantenimientoDepartamento(Model model) {

        setParametro(model, "lista_departamentos", servicioDepartamento.getTodos());
        return "Paginas/Departamento/mantenimiento_departamento";

    }

    @GetMapping("/crear_departamento")
    public String irCrear(Model model) {

        setParametro(model, "departamento", new Departamento());
        setParametro(model, "lista_departamentos", servicioDepartamento.getTodos());
        return "Paginas/Departamento/form_departamento";
    }

    @PostMapping("/guardar_departamento")
    public String guardar(Departamento entidad, Model model, RedirectAttributes attribute) {

        for (Departamento todo : servicioDepartamento.getTodos()) {

            if (estado_editando) {
                if (entidad.getNombre().equals(todo.getNombre()) && !todo.getIdDepartamento().equals(entidad.getIdDepartamento())) {
                    attribute.addFlashAttribute("error", "Nombre de departamento ya existente");
                    estado_editando = false;
                    return "redirect:/mantenimientoDepartamento";
                }
            } else {

                if (entidad.getNombre().equals(todo.getNombre())) {
                    attribute.addFlashAttribute("error", "Nombre de departamento ya existente");
                    estado_editando = false;
                    return "redirect:/crear_departamento";
                }
            }
        }

        servicioDepartamento.guardar(entidad);
        estado_editando = false;
        attribute.addFlashAttribute("success", "Guardado correctamente");
        return "redirect:/crear_departamento";
    }

    @GetMapping("/actualizar_departamento/{id}")
    public String irActualizar(@PathVariable("id") Long id, Model model, RedirectAttributes attribute) {

        setParametro(model, "departamento", servicioDepartamento.getValor(id));
        setParametro(model, "lista_departamentos", servicioDepartamento.getTodos());

        estado_editando = true;

        return "Paginas/Departamento/form_departamento";
    }

    @GetMapping("eliminar_departamento/{id}")
    public String eliminar(@PathVariable("id") Long id, Model modelo, RedirectAttributes attribute) {
        servicioDepartamento.eliminar(id);
        attribute.addFlashAttribute("success", "Eliminado correctamente");
        return "redirect:/mantenimientoDepartamento";
    }

    public void setParametro(Model model, String atributo, Object valor) {
        model.addAttribute(atributo, valor);
    }
}
