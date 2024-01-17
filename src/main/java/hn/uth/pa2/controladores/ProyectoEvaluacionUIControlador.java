/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.controladores;

import hn.uth.pa2.modelos.BitacoraCalificacion;
import hn.uth.pa2.modelos.ProyectoCoordinadores;
import hn.uth.pa2.modelos.ProyectoEvaluacion;
import hn.uth.pa2.modelos.ProyectoSupervisiones;
import hn.uth.pa2.modelos.Proyectos;
import hn.uth.pa2.servicios.BitacoraCalificacionServicios;
import hn.uth.pa2.servicios.PlantillaServicio;
import hn.uth.pa2.servicios.ProyectoCoordinadoresServicios;
import hn.uth.pa2.servicios.ProyectoEvaluacionServicio;
import hn.uth.pa2.servicios.ProyectoServicios;
import hn.uth.pa2.servicios.ProyectoSupervisionesServ;
import hn.uth.pa2.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Licona
 */
@Controller
public class ProyectoEvaluacionUIControlador {

    private Long idProyecto;
    private Long idTabla;

    @Autowired
    private UsuarioServicio servicioUsuario;

    @Autowired
    private BitacoraCalificacionServicios servicioBitacoraCalificacion;

    @Autowired
    private ProyectoServicios servicioProyecto;

    @Autowired
    private ProyectoCoordinadoresServicios servicioProyectosCoordinadores;

    @Autowired
    private PlantillaServicio servicioPlantilla;

    @Autowired
    private ProyectoEvaluacionServicio servicioProyectoEvaluacion;

    @Autowired
    private ProyectoSupervisionesServ servicioProyectoSuperviciones;

    @GetMapping("/finalizarProyecto/{id}")
    public String buscarProyecto(@PathVariable("id") Long id, RedirectAttributes atributo) {

        for (ProyectoEvaluacion proyectoEvaluacion : servicioProyectoEvaluacion.getPorIdProyecto(id)) {

            if (proyectoEvaluacion.getCalificacion() < 0 && proyectoEvaluacion.getIdCriterio().getTipoEvaluacion().getNombre().equals("GENERAL")) {
                atributo.addFlashAttribute("error", "Error-- Faltan criterios por evaluar");
                return "redirect:/calificar/" + id + "";
            }

            if (proyectoEvaluacion.getCalificacion() < 0) {
                atributo.addFlashAttribute("error", "Error-- Coordinadores no han terminado de evaluar");
                return "redirect:/calificar/" + id + "";
            }
        }

        Proyectos tmpPro = servicioProyecto.getValor(id).get();

        if (tmpPro == null || tmpPro.getEstado().length() == 0) {
            atributo.addFlashAttribute("error", "Error-- Proyecto no encontrado");
            return "redirect:/calificar/" + id + "";
        }

        int arg_general[] = contarTotales(id, tmpPro.getIdPlantillaGeneral().getIdPlantilla());

        int arg_tecnica[] = contarTotales(id, tmpPro.getIdPlantillaTecnico().getIdPlantilla());

        int arg_profecional[] = contarTotales(id, tmpPro.getIdPlantillaProfesional().getIdPlantilla());

        double calificacion_gnrl = 0, calificacion_tcna = 0, calificacion_prfl = 0;

        calificacion_gnrl += (arg_general[2] * 100) / arg_general[0];
        calificacion_tcna += (arg_tecnica[2] * 100) / arg_tecnica[0];
        calificacion_prfl += (arg_profecional[2] * 100) / arg_profecional[0];

        //(double)Math.round(number * 100d) / 100d)
        this.idProyecto = null;
        atributo.addFlashAttribute("success", "Proyecto finalizado correctamente");
        tmpPro.setCalificacionGeneral(formatearDecimales((10 * (calificacion_gnrl / 100)), 2));
        tmpPro.setCalificacionProfesional(formatearDecimales((60 * (calificacion_prfl / 100)), 2));
        tmpPro.setCalificacionTecnico(formatearDecimales(30 * ((calificacion_tcna / 100)), 2));

        tmpPro.setEstado("Finalizado");

        servicioProyecto.guardar(tmpPro);

        return "redirect:/misProyectos";
    }

    @GetMapping("/calificar/{id}")
    public String irFormulario(@PathVariable("id") Long id, Model model, RedirectAttributes atributo) throws Exception {
        this.idProyecto = id;
        for (Proyectos item : servicioProyecto.getTodos()) {
            if (item.getIdProyecto().equals(id) && item.getEstado().equals("Finalizado")) {
                atributo.addFlashAttribute("error", "Error - El proyecto esta finalizado, no puede calificarlo");
                return "redirect:/misProyectos";
            }
        }
        int cont_superviciones_pro = 0;
        int cont_superviciones_tec = 0;
        int cont_superviciones_gnr = 0;

        if (!servicioProyectoSuperviciones.getReporteProyectoEvaluaciones(id).isEmpty()) {
            for (ProyectoSupervisiones object : servicioProyectoSuperviciones.getReporteProyectoEvaluaciones(id)) {
                if (object.getIdTipoCoordinador().getNombre().equals("Coordinador Tecnico")) {
                    cont_superviciones_tec++;
                }

                if (object.getIdTipoCoordinador().getNombre().equals("Coordinador Profesional")) {
                    cont_superviciones_pro++;
                }
            }
        } else {
            atributo.addFlashAttribute("error", "Error - El proyecto no tiene superviones");
            return "redirect:/misProyectos";
        }

        cont_superviciones_gnr = cont_superviciones_pro + cont_superviciones_tec;

        Long idUsuario = servicioUsuario.getLoggedUser().getId_usuario();

        for (ProyectoCoordinadores proyectoCoordinadores : servicioProyectosCoordinadores.seleccionarProyectoCoordinador(idUsuario)) {

            if (proyectoCoordinadores.getIdProyecto().getIdProyecto().equals(id)) {
                setParametro(model, "proyecto", proyectoCoordinadores.getIdProyecto());
                setParametro(model, "tipoCoordinador", proyectoCoordinadores.getIdTipoCoordinador());

                if (proyectoCoordinadores.getIdTipoCoordinador().getNombre().equalsIgnoreCase("Coordinador Tecnico")) {

                    if (cont_superviciones_tec < 3) {
                        atributo.addFlashAttribute("error", "Tiene " + cont_superviciones_tec + " superviciones de 3");
                        return "redirect:/misProyectos";
                    }

                    setParametro(model, "plantilla", proyectoCoordinadores.getIdProyecto().getIdPlantillaTecnico());

                    setParametro(model, "listaProyectoEvaluacion", getProjectoEvaluacionPorPlantilla(proyectoCoordinadores.getIdProyecto().getIdProyecto(),
                            proyectoCoordinadores.getIdProyecto().getIdPlantillaTecnico().getIdPlantilla()));

                    setParametro(model, "contadorMax", contarTotales(proyectoCoordinadores.getIdProyecto().getIdProyecto(),
                            proyectoCoordinadores.getIdProyecto().getIdPlantillaTecnico().getIdPlantilla())[0]);

                    setParametro(model, "contadorMin", contarTotales(proyectoCoordinadores.getIdProyecto().getIdProyecto(),
                            proyectoCoordinadores.getIdProyecto().getIdPlantillaTecnico().getIdPlantilla())[1]);

                    setParametro(model, "contadorCalificacion", contarTotales(proyectoCoordinadores.getIdProyecto().getIdProyecto(),
                            proyectoCoordinadores.getIdProyecto().getIdPlantillaTecnico().getIdPlantilla())[2]);
                }

                if (proyectoCoordinadores.getIdTipoCoordinador().getNombre().equalsIgnoreCase("Coordinador General")) {

                    if (cont_superviciones_gnr < 6) {
                        atributo.addFlashAttribute("error", "El proyecto tiene " + cont_superviciones_gnr + " superviciones de 6");
                        return "redirect:/misProyectos";
                    }
                    for (ProyectoEvaluacion proyectoEvaluacion : servicioProyectoEvaluacion.getPorIdProyecto(id)) {
                        if (proyectoEvaluacion.getFecha() == null) {

                            if (proyectoEvaluacion.getIdCriterio().getTipoEvaluacion().getNombre().equalsIgnoreCase("GENERAL")) {
                            } else {
                                atributo.addFlashAttribute("error", "Faltan calificaciones de los coordinadores tecnico y/o general");
                                return "redirect:/misProyectos";
                            }
                        }
                    }

                    setParametro(model, "plantilla", proyectoCoordinadores.getIdProyecto().getIdPlantillaGeneral());

                    setParametro(model, "listaProyectoEvaluacion", getProjectoEvaluacionPorPlantilla(proyectoCoordinadores.getIdProyecto().getIdProyecto(),
                            proyectoCoordinadores.getIdProyecto().getIdPlantillaGeneral().getIdPlantilla()));

                    setParametro(model, "contadorMax", contarTotales(proyectoCoordinadores.getIdProyecto().getIdProyecto(),
                            proyectoCoordinadores.getIdProyecto().getIdPlantillaGeneral().getIdPlantilla())[0]);

                    setParametro(model, "contadorMin", contarTotales(proyectoCoordinadores.getIdProyecto().getIdProyecto(),
                            proyectoCoordinadores.getIdProyecto().getIdPlantillaGeneral().getIdPlantilla())[1]);

                    setParametro(model, "contadorCalificacion", contarTotales(proyectoCoordinadores.getIdProyecto().getIdProyecto(),
                            proyectoCoordinadores.getIdProyecto().getIdPlantillaGeneral().getIdPlantilla())[2]);

                    System.out.println("Entro");
                }

                if (proyectoCoordinadores.getIdTipoCoordinador().getNombre().equalsIgnoreCase("Coordinador Profesional")) {

                    if (cont_superviciones_pro < 3) {
                        atributo.addFlashAttribute("error", "Tiene " + cont_superviciones_pro + " superviciones de 3");
                        return "redirect:/misProyectos";
                    }

                    setParametro(model, "plantilla", proyectoCoordinadores.getIdProyecto().getIdPlantillaProfesional());

                    setParametro(model, "listaProyectoEvaluacion", getProjectoEvaluacionPorPlantilla(proyectoCoordinadores.getIdProyecto().getIdProyecto(),
                            proyectoCoordinadores.getIdProyecto().getIdPlantillaProfesional().getIdPlantilla()));

                    setParametro(model, "contadorMax", contarTotales(proyectoCoordinadores.getIdProyecto().getIdProyecto(),
                            proyectoCoordinadores.getIdProyecto().getIdPlantillaProfesional().getIdPlantilla())[0]);

                    setParametro(model, "contadorMin", contarTotales(proyectoCoordinadores.getIdProyecto().getIdProyecto(),
                            proyectoCoordinadores.getIdProyecto().getIdPlantillaProfesional().getIdPlantilla())[1]);

                    setParametro(model, "contadorCalificacion", contarTotales(proyectoCoordinadores.getIdProyecto().getIdProyecto(),
                            proyectoCoordinadores.getIdProyecto().getIdPlantillaProfesional().getIdPlantilla())[2]);
                }

                break;
            }
        }

        if (servicioProyectosCoordinadores.seleccionarProyectoCoordinador(idUsuario).isEmpty()) {
            atributo.addFlashAttribute("error", "error");
            return "redirect:/misProyectos";
        }
        setParametro(model, "usuario", servicioUsuario.getLoggedUser().getId_usuario());

        return "paginas/calificacion/form_calificacion";
    }

    @GetMapping("/calificar_criterio/{id}")
    public String irCalificar(@PathVariable("id") Long id, Model model, RedirectAttributes atributo) throws Exception {
        this.idTabla = id;
        setParametro(model, "evaluacionCriterio", servicioProyectoEvaluacion.getValor(id).get());
        atributo.addFlashAttribute("error", "Correo ya existente");

        return "paginas/calificacion/form_nota";
    }

    public void setParametro(Model model, String atributo, Object valor) {
        model.addAttribute(atributo, valor);
    }

    public List<ProyectoEvaluacion> getProjectoEvaluacionPorPlantilla(Long idProyecto, Long idPlantilla) {
        return servicioProyectoEvaluacion.getPorIdProyectoAndPlantilla(idProyecto, idPlantilla);
    }

    @PostMapping("/guardar_evaluacionCriterio")
    public String guardar(ProyectoEvaluacion entidad, Model model, RedirectAttributes attribute) throws Exception {

        if (entidad.getCalificacion() < 0 || entidad.getCalificacion() > entidad.getIdCriterio().getPunt_maximo()
                || entidad.getCalificacion() < entidad.getIdCriterio().getPunt_minimo()) {

            attribute.addFlashAttribute("error", "Calificacion no valida");
            return "redirect:/calificar_criterio/" + entidad.getId() + "";
        }

        entidad.setIdUsuario(servicioUsuario.getLoggedUser());

        java.util.Date d = new java.util.Date();
        java.sql.Date date2 = new java.sql.Date(d.getTime());

        entidad.setFecha(date2);
        for (ProyectoEvaluacion item : servicioProyectoEvaluacion.getPorIdProyecto(this.idProyecto)) {
            if (item.getId().equals(this.idTabla) && item.getCalificacion() >= 0) {
                this.bitacoraCalificacion();
            }
        }

        servicioProyectoEvaluacion.guardar(entidad);

        setParametro(model, "evaluacionCriterio", entidad);

        attribute.addFlashAttribute("success", "Guardado correctamente");

        return "redirect:/calificar_criterio/" + entidad.getId() + "";
    }

    public int[] contarTotales(Long idProyecto, Long idPlantilla) {

        int temp[] = new int[3];
        temp[0] = 0;
        temp[1] = 0;
        temp[2] = 0;

        for (ProyectoEvaluacion item : getProjectoEvaluacionPorPlantilla(idProyecto, idPlantilla)) {
            temp[0] += item.getIdCriterio().getPunt_maximo();
            temp[1] += item.getIdCriterio().getPunt_minimo();
            temp[2] += item.getCalificacion();
        }

        return temp;
    }

    public static Double formatearDecimales(Double numero, Integer numeroDecimales) {
        return Math.round(numero * Math.pow(10, numeroDecimales)) / Math.pow(10, numeroDecimales);
    }

    private void bitacoraCalificacion() throws Exception {
        BitacoraCalificacion bitacora = new BitacoraCalificacion();
        java.util.Date d = new java.util.Date();
        java.sql.Date date2 = new java.sql.Date(d.getTime());
        bitacora.setUsuario(servicioUsuario.getLoggedUser().getUsername());
        bitacora.setFechaEjecucion(date2);
        this.servicioBitacoraCalificacion.guardar(bitacora);
    }
}
