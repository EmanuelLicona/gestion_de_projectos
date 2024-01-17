/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.controladores;

import hn.uth.pa2.modelos.BitacoraCoordinadores;
import hn.uth.pa2.modelos.Departamento;
import hn.uth.pa2.modelos.Criterio;
import hn.uth.pa2.modelos.ProyectoCoordinadores;
import hn.uth.pa2.modelos.ProyectoEvaluacion;
import hn.uth.pa2.modelos.Proyectos;
import hn.uth.pa2.modelos.TipoCoordinadores;
import hn.uth.pa2.modelos.Usuario;
import hn.uth.pa2.servicios.BitacoraCoordinadoresServicio;
import hn.uth.pa2.servicios.DepartamentoServicio;
import hn.uth.pa2.servicios.PlantillaServicio;
import hn.uth.pa2.servicios.ProyectoCoordinadoresServicios;
import hn.uth.pa2.servicios.ProyectoEvaluacionServicio;
import hn.uth.pa2.servicios.ProyectoServicios;
import hn.uth.pa2.servicios.TipoCoordinadoresServicio;
import hn.uth.pa2.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Buddys
 */
@Controller
public class ProyectoUIControlador {

    private boolean banderin = true;
    private boolean banderinProyectoCoord = true;
    private Long idProyecto;

    @Autowired
    private ProyectoServicios servicio;

    @Autowired
    private DepartamentoServicio servicioDepartamento;

    @Autowired
    private UsuarioServicio servicioUsuario;

    @Autowired
    private TipoCoordinadoresServicio servicioCoordinador;

    @Autowired
    private ProyectoCoordinadoresServicios servicioProyectoCoord;

    @Autowired
    private BitacoraCoordinadoresServicio servicioBitacoraCoordinador;

    @Autowired
    private PlantillaServicio servicioPlantilla;

    @Autowired
    private ProyectoEvaluacionServicio servicioProyectoEvaluacion;

    @RequestMapping("/registrarProyecto")
    public String irFormulario(Model model) {
        setParametro(model, "proyecto", new Proyectos());
        setParametro(model, "listaDepartamentos", servicioDepartamento.getTodos());
        setParametro(model, "listaPlantillaProfesional", servicioPlantilla.getTipoPlantilla("PROFESIONAL"));
        setParametro(model, "listaPlantillaTecnico", servicioPlantilla.getTipoPlantilla("TECNICO"));
        setParametro(model, "listaPlantillaGeneral", servicioPlantilla.getTipoPlantilla("GENERAL"));
        this.banderin = true;
        return "paginas/proyecto/form-proyecto";
    }

    @GetMapping("/mantenimientoProyecto")
    public String irServicios(Model model) {
        try {
            model.addAttribute("buscarTitulo", new Proyectos());
            model.addAttribute("listaProyecto", servicio.getTodos());
            this.banderin = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "paginas/proyecto/mantenimiento-proyecto";
    }

    @GetMapping("/busquedaProyecto")
    public String buscarProyecto(Model model, @ModelAttribute("buscarTitulo") Proyectos entidad) {
        String busqueda = entidad.getTitulo().replaceAll("^\\s*", "");
        if (busqueda.equals("")) {
            model.addAttribute("listaProyecto", servicio.getTodos());
        } else {
            model.addAttribute("listaProyecto", servicio.getResultadoBusqueda(busqueda.toUpperCase()));
        }
        return "paginas/proyecto/mantenimiento-proyecto";
    }

    @RequestMapping("/misProyectos")
    public String irMisProyectos(Model model) throws Exception {
        Long idUsuario = servicioUsuario.getLoggedUser().getId_usuario();
        setParametro(model, "listaProyectos", servicioProyectoCoord.seleccionarProyectoCoordinador(idUsuario));
        return "paginas/proyecto/proyectos_usuario";
    }

    @RequestMapping("/mantenimientoProyectoCoord")
    public String irCoordinadoresProyecto(Model model) {
        setParametro(model, "listaProyecto", servicio.getTodos());
        return "paginas/proyecto/mant-proyecto-coordinadores";
    }

    @GetMapping("/actualizarProyecto/{id}")
    public String irActualizar(@PathVariable("id") Long id, Model modelo, RedirectAttributes atributo) {
        for (Proyectos item : servicio.getTodos()) {
            if (item.getIdProyecto().equals(id) && item.getEstado().equals("Finalizado")) {
                atributo.addFlashAttribute("error", "Error - El proyecto esta finalizado no se puede actualizar");
                return "redirect:/mantenimientoProyecto";
            }
        }
        setParametro(modelo, "listaDepartamentos", servicioDepartamento.getTodos());
        setParametro(modelo, "listaPlantillaProfesional", servicioPlantilla.getTipoPlantilla("PROFESIONAL"));
        setParametro(modelo, "listaPlantillaTecnico", servicioPlantilla.getTipoPlantilla("TECNICO"));
        setParametro(modelo, "listaPlantillaGeneral", servicioPlantilla.getTipoPlantilla("GENERAL"));
        setParametro(modelo, "proyecto", servicio.getValor(id));
        this.banderin = false;
        return "paginas/proyecto/form-proyecto";
    }

    @GetMapping("/agregarCordinadores/{id}")
    public String agregarCoordinadores(@PathVariable("id") Long id, Model modelo, RedirectAttributes atributo, Proyectos entidad) {
        for (ProyectoCoordinadores item : servicioProyectoCoord.getTodos()) {
            if (item.getIdProyecto().getIdProyecto().equals(id)) {
                atributo.addFlashAttribute("error", "El proyecto ya tiene agregado sus 3 coordinadores");
                return "redirect:/mantenimientoProyectoCoord";
            }
        }
        if (servicioCoordinador.getTodos().size() == 0) {
            atributo.addFlashAttribute("error", "Error el sistema aun no tiene coordinadores");
            return "redirect:/mantenimientoProyectoCoord";
        }
        this.idProyecto = id;
        this.banderinProyectoCoord = true;
        setParametro(modelo, "proyecto", new Proyectos());
        setParametro(modelo, "listaUsuario", servicioUsuario.getUsuariosConsulta("consulta"));
        setParametro(modelo, "listaCoordinadorP", servicioCoordinador.getTipoCoordinador("Coordinador Profesional"));
        setParametro(modelo, "listaCoordinadorT", servicioCoordinador.getTipoCoordinador("Coordinador Tecnico"));
        setParametro(modelo, "listaCoordinadorG", servicioCoordinador.getTipoCoordinador("Coordinador General"));
        return "paginas/proyecto/form-proyecto-coordinadores";
    }

    @PostMapping("/guardarProyecto")
    public String guardar(Proyectos proyecto, Model model, RedirectAttributes atributo) {
        try {
            if (proyecto.getIdDepartamento() == null) {
                atributo.addFlashAttribute("error", "Error el departamento esta vacio");
                return "redirect:/registrarProyecto";
            }
            for (Departamento todo : servicioDepartamento.getTodos()) {
                if (todo.getEstado().equals(proyecto.getIdDepartamento().getEstado())) {
                    if (todo.getEstado().equalsIgnoreCase("Inactivo")) {
                        atributo.addFlashAttribute("error", "Error - El Departamento esta Inactivo");
                        return "redirect:/registrarProyecto";
                    }
                }
            } 
            String capturador = this.existeCriterioPlantilla(proyecto);
            if (!capturador.equals("OK")) {
                atributo.addFlashAttribute("error", capturador);
                return "redirect:/registrarProyecto";
            }
            proyecto.setEstado("Activo");
            if (banderin) {
                for (Proyectos item : servicio.getTodos()) {
                    if (item.getTitulo().equals(proyecto.getTitulo())) {
                        atributo.addFlashAttribute("error", "Error el nombre del proyecto ya existe");
                        return "redirect:/registrarProyecto";
                    }
                }
                if (proyecto.getEstado().equalsIgnoreCase("Finalizado")) {
                    atributo.addFlashAttribute("error", "Error - El estado del proyecto debe estar activo");
                    return "redirect:/registrarProyecto";
                }
                proyecto.setCalificacionProfesional(-1);
                proyecto.setCalificacionTecnico(-1);
                proyecto.setCalificacionGeneral(-1);
                servicio.guardar(proyecto);
                atributo.addFlashAttribute("success", "Guardado Correctamente");
            } else {
                servicio.guardar(proyecto);
                atributo.addFlashAttribute("success", "Actualizado Correctamente");
            }
            this.banderin = true;
        } catch (Exception e) {
            System.out.println("ERROR AQUI: " + e.getMessage());
        }

        /**
         * **************************************************************************
         */
        //Guardando las plantillas en el historial
        if (eliminarEvaluacionesProyectos(proyecto.getIdProyecto()).equalsIgnoreCase("OK")) {
            for (Criterio criterio : proyecto.getIdPlantillaTecnico().getCriterios()) {
                ProyectoEvaluacion temp = new ProyectoEvaluacion();
                temp.setIdCriterio(criterio);
                temp.setIdPlantilla(proyecto.getIdPlantillaTecnico());
                temp.setIdProyecto(proyecto);
                temp.setCalificacion(-1);

                servicioProyectoEvaluacion.guardar(temp);
            }

            for (Criterio criterio : proyecto.getIdPlantillaProfesional().getCriterios()) {
                ProyectoEvaluacion temp = new ProyectoEvaluacion();
                temp.setIdCriterio(criterio);
                temp.setIdPlantilla(proyecto.getIdPlantillaProfesional());
                temp.setIdProyecto(proyecto);
                temp.setCalificacion(-1);

                servicioProyectoEvaluacion.guardar(temp);
            }

            for (Criterio criterio : proyecto.getIdPlantillaGeneral().getCriterios()) {
                ProyectoEvaluacion temp = new ProyectoEvaluacion();
                temp.setIdCriterio(criterio);
                temp.setIdPlantilla(proyecto.getIdPlantillaGeneral());
                temp.setIdProyecto(proyecto);
                temp.setCalificacion(-1);

                servicioProyectoEvaluacion.guardar(temp);
            }
        }else{
            atributo.addFlashAttribute("success", "Actualizado Correctamente | Plantillas no se puede editar ya que el proyecto esta siendo evaluado");
        }

        /**
         * ***************************************************************************
         */
        return "redirect:/mantenimientoProyecto";
    }

    @PostMapping("/guardarCoordinadores")
    public String guardarCoordinadores(Proyectos proyecto, Model model, RedirectAttributes atributo) {
        try {
            proyecto.setIdProyecto(this.idProyecto);
            if (proyecto.getIdProyecto() == null) {
                atributo.addFlashAttribute("error", "Selecciona un proyecto antes de guardar");
                return "redirect:/formProyectoCoordinadores";
            }
            if (proyecto.getIdUsuario1() == 0 || proyecto.getIdUsuario2() == 0 || proyecto.getIdUsuario3() == 0) {
                atributo.addFlashAttribute("error", "Error la opcion del coordinador esta vacia");
                return "redirect:/formProyectoCoordinadores";
            }
            //----------------------------------Validando y despues guardando
            if (proyecto.getIdUsuario1().equals(proyecto.getIdUsuario2())) {
                atributo.addFlashAttribute("error", "El coordinador profesional debe ser diferente del coordinador tenico");
                if (this.banderinProyectoCoord) {
                    return "redirect:/formProyectoCoordinadores";
                } else {
                    return "redirect:/formCoordinadoresActualizar";
                }
            } else {
                if (proyecto.getIdUsuario1().equals(proyecto.getIdUsuario3())) {
                    atributo.addFlashAttribute("error", "El coordinador profesional debe ser diferente del coordinador general");
                    if (this.banderinProyectoCoord) {
                        return "redirect:/formProyectoCoordinadores";
                    } else {
                        return "redirect:/formCoordinadoresActualizar";
                    }
                }
                if (proyecto.getIdUsuario3().equals(proyecto.getIdUsuario2())) {
                    atributo.addFlashAttribute("error", "El coordinador general debe ser diferente del coordinador tecnico");
                    if (this.banderinProyectoCoord) {
                        return "redirect:/formProyectoCoordinadores";
                    } else {
                        return "redirect:/formCoordinadoresActualizar";
                    }
                }
                if (!banderinProyectoCoord) {
                    servicioProyectoCoord.eliminarProyectoCoordinadores(proyecto.getIdProyecto());
                    this.banderin = false;
                }
                //------------------------------------------------------------ Guardando el primero
                ProyectoCoordinadores proyectoSup = new ProyectoCoordinadores();
                TipoCoordinadores coordinador = new TipoCoordinadores(proyecto.getIdCoordinadorProfesional());
                Usuario usuario = new Usuario(proyecto.getIdUsuario1());
                proyectoSup.setIdProyecto(proyecto);
                proyectoSup.setIdTipoCoordinador(coordinador);
                proyectoSup.setIdUsuario(usuario);

                //------------------------------------------------------------ Guardando el segundo
                ProyectoCoordinadores proyectoSup2 = new ProyectoCoordinadores();
                TipoCoordinadores coordinador2 = new TipoCoordinadores(proyecto.getIdCoordinadorTecnico());
                Usuario usuario2 = new Usuario(proyecto.getIdUsuario2());
                proyectoSup2.setIdProyecto(proyecto);
                proyectoSup2.setIdTipoCoordinador(coordinador2);
                proyectoSup2.setIdUsuario(usuario2);

                //------------------------------------------------------------ Guardando el tercero
                ProyectoCoordinadores proyectoSup3 = new ProyectoCoordinadores();
                TipoCoordinadores coordinador3 = new TipoCoordinadores(proyecto.getIdCoordinadorGeneral());
                Usuario usuario3 = new Usuario(proyecto.getIdUsuario3());
                proyectoSup3.setIdProyecto(proyecto);
                proyectoSup3.setIdTipoCoordinador(coordinador3);
                proyectoSup3.setIdUsuario(usuario3);

                //-----------------------------------------
                servicioProyectoCoord.guardar(proyectoSup);
                servicioProyectoCoord.guardar(proyectoSup2);
                servicioProyectoCoord.guardar(proyectoSup3);
                if (banderinProyectoCoord) {
                    atributo.addFlashAttribute("success", "Guardado Correctamente");
                } else {
                    java.util.Date d = new java.util.Date();
                    java.sql.Date date2 = new java.sql.Date(d.getTime());
                    servicioBitacoraCoordinador.guardar(new BitacoraCoordinadores(date2, proyecto.getJustificacion()));
                    atributo.addFlashAttribute("success", "Actualizado Correctamente");
                }
            }

            this.banderin = true;
            this.idProyecto = null;
        } catch (Exception e) {
            System.out.println("ERROR AQUI: " + e.getMessage());
        }
        return "redirect:/mantenimientoProyectoCoord";
    }

    @RequestMapping("/formProyectoCoordinadores")
    public String irFormularioProyectoCoord(Model modelo) {
        this.banderinProyectoCoord = true;
        setParametro(modelo, "proyecto", new Proyectos());
        setParametro(modelo, "listaUsuario", servicioUsuario.getUsuariosConsulta("consulta"));
        setParametro(modelo, "listaCoordinadorP", servicioCoordinador.getTipoCoordinador("Coordinador Profesional"));
        setParametro(modelo, "listaCoordinadorT", servicioCoordinador.getTipoCoordinador("Coordinador Tecnico"));
        setParametro(modelo, "listaCoordinadorG", servicioCoordinador.getTipoCoordinador("Coordinador General"));
        return "paginas/proyecto/form-proyecto-coordinadores";
    }

    @RequestMapping("/formCoordinadoresActualizar")
    public String irFormularioCoordActualizar(Model modelo) {
        setParametro(modelo, "proyecto", new Proyectos());
        modelo.addAttribute("editMode", "true");
        this.banderinProyectoCoord = false;
        setParametro(modelo, "listaUsuario", servicioUsuario.getUsuariosConsulta("consulta"));
        setParametro(modelo, "listaCoordinadorP", servicioCoordinador.getTipoCoordinador("Coordinador Profesional"));
        setParametro(modelo, "listaCoordinadorT", servicioCoordinador.getTipoCoordinador("Coordinador Tecnico"));
        setParametro(modelo, "listaCoordinadorG", servicioCoordinador.getTipoCoordinador("Coordinador General"));
        return "paginas/proyecto/form-proyecto-coordinadores";
    }

    @GetMapping("/actualizarCordinadores/{id}")
    public String actualizarCoordinadores(@PathVariable("id") Long id, Model modelo, RedirectAttributes atributo) {
        this.idProyecto = id;
        this.banderinProyectoCoord = false;
        if (servicioCoordinador.getTodos().size() == 0) {
            atributo.addFlashAttribute("error", "Error el sistema aun no tiene coordinadores");
            return "redirect:/mantenimientoProyectoCoord";
        }
        if (servicioProyectoCoord.getObtenerPorId(id).isEmpty()) {
            atributo.addFlashAttribute("error", "Error el proyecto no tiene agregado coordinadores, no se puede actualizar");
            return "redirect:/mantenimientoProyectoCoord";
        }
        modelo.addAttribute("editMode", "true");
        setParametro(modelo, "proyecto", servicio.getValor(id));
        setParametro(modelo, "listaUsuario", servicioUsuario.getUsuariosConsulta("consulta"));
        setParametro(modelo, "listaCoordinadorP", servicioCoordinador.getTipoCoordinador("Coordinador Profesional"));
        setParametro(modelo, "listaCoordinadorT", servicioCoordinador.getTipoCoordinador("Coordinador Tecnico"));
        setParametro(modelo, "listaCoordinadorG", servicioCoordinador.getTipoCoordinador("Coordinador General"));

        return "paginas/proyecto/form-proyecto-coordinadores";
    }

    public void setParametro(Model model, String atributo, Object valor) {
        model.addAttribute(atributo, valor);
    }

    /**
     * ****************************************************************************************************
     */
    private String eliminarEvaluacionesProyectos(Long idProyecto) {

        List<ProyectoEvaluacion> temp = servicioProyectoEvaluacion.getPorIdProyecto(idProyecto);

        for (ProyectoEvaluacion proyectoEvaluacion : temp) {
            if (proyectoEvaluacion.getFecha() != null) {
                return "NO OK";
            }
        }

        for (ProyectoEvaluacion proyectoEvaluacion : temp) {
            servicioProyectoEvaluacion.eliminar(proyectoEvaluacion.getId());
        }

        return "OK";
    }
   
    private String existeCriterioPlantilla(Proyectos proyecto) {
        boolean existe = false;
        if (servicio.existeCriterioPlantilla(proyecto.getIdPlantillaProfesional().getIdPlantilla()).isEmpty()) {
            return "Error - La plantilla del coordinador profesional no tiene criterios";
        }
        if (servicio.existeCriterioPlantilla(proyecto.getIdPlantillaTecnico().getIdPlantilla()).isEmpty()) {
            return "Error - La plantilla del coordinador tecnico no tiene criterios";
        }
        if (servicio.existeCriterioPlantilla(proyecto.getIdPlantillaGeneral().getIdPlantilla()).isEmpty()) {
            return "Error - La plantilla del coordinador general no tiene criterios";
        }
        return "OK";
    }
}
