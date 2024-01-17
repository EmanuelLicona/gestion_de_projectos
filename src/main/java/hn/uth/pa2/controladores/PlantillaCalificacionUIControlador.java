/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.controladores;

import hn.uth.pa2.modelos.ProyectoCoordinadores;
import hn.uth.pa2.modelos.Proyectos;
import hn.uth.pa2.modelos.TipoEvaluacion;
import hn.uth.pa2.servicios.ProyectoCoordinadoresServicios;
import hn.uth.pa2.servicios.ProyectoEvaluacionServicio;
import hn.uth.pa2.servicios.ProyectoServicios;
import hn.uth.pa2.servicios.TipoEvaluacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Buddys
 */
@Controller
public class PlantillaCalificacionUIControlador {

    @Autowired
    private ProyectoServicios servicioProyecto;
    
    @Autowired
    private ProyectoEvaluacionServicio servicioEvaluacion;
    
    @Autowired
    private ProyectoCoordinadoresServicios servicioCoordinadores;
    
    @Autowired
    private TipoEvaluacionServicio servicioTipoEvaluacion;
    

    @GetMapping("/calificacionCoordinadorProfesional/{id}")
    public String irCoordinadorProfesional(@PathVariable("id") Long id, Model modelo, RedirectAttributes attribute) {
        Long idUsuarioProfesional = 0L;
        for (ProyectoCoordinadores item : servicioCoordinadores.getUsuarioCoordindor(id, "Coordinador Profesional")) {
            idUsuarioProfesional = item.getIdUsuario().getId_usuario();            
            setParametro(modelo, "coordinador", item.getIdTipoCoordinador());
             setParametro(modelo, "coordinadorP", item.getIdUsuario());
        }
        setParametro(modelo, "valoresProyecto", servicioProyecto.getValor(id).get());
        //setParametro(modelo, "listadoCalificacion", servicioEvaluacion.getCalificacionPorCoordinador(idUsuarioProfesional, id));
        
        for (TipoEvaluacion item : servicioTipoEvaluacion.getTodos()) {
            if(item.getNombre().equalsIgnoreCase("PROFESIONAL")){
                setParametro(modelo, "listadoCalificacion", servicioEvaluacion.getCalificacionPorTipoEvaluacion(item.getIdEvaluacion(), id));
                break;
            }
        }
        
        return "paginas/plantillas/plantilla-calificacion";
    }
    
    @GetMapping("/calificacionCoordinadorTecnico/{id}")
    public String irCoordinadorTecnico(@PathVariable("id") Long id, Model modelo, RedirectAttributes attribute) {
        Long idUsuarioProfesional = 0L;
        for (ProyectoCoordinadores item : servicioCoordinadores.getUsuarioCoordindor(id, "Coordinador Tecnico")) {
            idUsuarioProfesional = item.getIdUsuario().getId_usuario();            
            setParametro(modelo, "coordinador", item.getIdTipoCoordinador());
             setParametro(modelo, "coordinadorP", item.getIdUsuario());
        }
        setParametro(modelo, "valoresProyecto", servicioProyecto.getValor(id).get());
        //setParametro(modelo, "listadoCalificacion", servicioEvaluacion.getCalificacionPorCoordinador(idUsuarioProfesional, id));
        
        for (TipoEvaluacion item : servicioTipoEvaluacion.getTodos()) {
            if(item.getNombre().equalsIgnoreCase("TECNICO")){
                setParametro(modelo, "listadoCalificacion", servicioEvaluacion.getCalificacionPorTipoEvaluacion(item.getIdEvaluacion(), id));
                break;
            }
        }
        
        return "paginas/plantillas/plantilla-calificacion";
    }
    
    @GetMapping("/calificacionCoordinadorGeneral/{id}")
    public String irCoordinadorGeneral(@PathVariable("id") Long id, Model modelo, RedirectAttributes attribute) {
        Long idUsuarioProfesional = 0L;
        for (ProyectoCoordinadores item : servicioCoordinadores.getUsuarioCoordindor(id, "Coordinador General")) {
            idUsuarioProfesional = item.getIdUsuario().getId_usuario();      
            setParametro(modelo, "coordinador", item.getIdTipoCoordinador());
            setParametro(modelo, "coordinadorP", item.getIdUsuario());
        }
        setParametro(modelo, "valoresProyecto", servicioProyecto.getValor(id).get());
        //setParametro(modelo, "listadoCalificacion", servicioEvaluacion.getCalificacionPorCoordinador(idUsuarioProfesional, id));
        
        for (TipoEvaluacion item : servicioTipoEvaluacion.getTodos()) {
            if(item.getNombre().equalsIgnoreCase("GENERAL")){
                setParametro(modelo, "listadoCalificacion", servicioEvaluacion.getCalificacionPorTipoEvaluacion(item.getIdEvaluacion(), id));
                break;
            }
        }
        return "paginas/plantillas/plantilla-calificacion";
    }


    @GetMapping("/irReporteCalificaciones")
    public String irServicios(Model model) {
        setParametro(model, "buscarProyectoC", new Proyectos());
        setParametro(model, "proyectosCalificados", servicioProyecto.getProyectosFinalizados());
        return "paginas/reportes/reporte-calificacion";
    }

    @GetMapping("/busquedaProyectosCalificados")
    public String buscarProyecto(Model model, @ModelAttribute("buscarProyectoC") Proyectos entidad) {
        String busqueda = entidad.getTitulo().replaceAll("^\\s*", "");
        if (busqueda.equals("")) {
            model.addAttribute("proyectosCalificados", servicioProyecto.getProyectosFinalizados());
        } else {
            model.addAttribute("proyectosCalificados", servicioProyecto.getProyectosCalificados(busqueda.toUpperCase()));
        }
        return "paginas/reportes/reporte-calificacion";
    }

    public void setParametro(Model model, String atributo, Object valor) {
        model.addAttribute(atributo, valor);
    }
}
