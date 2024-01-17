/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.controladores;

import hn.uth.pa2.modelos.BitacoraSupervision;
import hn.uth.pa2.modelos.ProyectoSupervisiones;
import hn.uth.pa2.modelos.Proyectos;
import hn.uth.pa2.modelos.Supervisiones;
import hn.uth.pa2.modelos.TipoCoordinadores;
import hn.uth.pa2.modelos.Usuario;
import hn.uth.pa2.servicios.BitacoraSupervisionServicios;
import hn.uth.pa2.servicios.ProyectoSupervisionesServ;
import hn.uth.pa2.servicios.SupervisionesServicios;
import hn.uth.pa2.servicios.TipoCoordinadoresServicio;
import hn.uth.pa2.servicios.UsuarioServicio;
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
public class SupervisionesUIControlador {

    private boolean banderin = true;
    private Long idProyecto = null;
    TipoCoordinadores coordinador = new TipoCoordinadores();

    @Autowired
    private SupervisionesServicios servicio;

    @Autowired
    private TipoCoordinadoresServicio servicioTipoCoordinadores;

    @Autowired
    private ProyectoSupervisionesServ servicioProyectoSuperv;

    @Autowired
    private BitacoraSupervisionServicios servicioBitacoraSupervision;

    @Autowired
    private UsuarioServicio servicioUsuario;

    @RequestMapping("/registrarSupervision")
    public String irFormulario(Model model) {
        setParametro(model, "supervisiones", new Supervisiones());
        setParametro(model, "proyectoSupervisiones", new ProyectoSupervisiones());
        return "paginas/supervision/form-supervisiones";
    }

    @GetMapping("/actualizarSupervision/{id}")
    public String irActualizar(@PathVariable("id") Long id, Model modelo, RedirectAttributes atributo) {
        setParametro(modelo, "supervisiones", servicio.getValor(id));
        setParametro(modelo, "proyectoSupervisiones", new ProyectoSupervisiones());
        return "paginas/supervision/form-supervisiones";
    }

    @GetMapping("/coordinadorProfesional/{id}")
    public String irCoordinadorProfesional(@PathVariable("id") Long id, Model modelo, RedirectAttributes atributo) {

        for (TipoCoordinadores object : servicioTipoCoordinadores.getTodos()) {
            if (object.getNombre().equalsIgnoreCase("Coordinador Profesional")) {
                this.coordinador.setIdTipoCoordinador(object.getIdTipoCoordinador());
            }
        }
        if (terceraSupervision(id)) {
            atributo.addFlashAttribute("error", "Error - Ya se hicieron las tres supervisiones");
            return "redirect:/misProyectos";
        }
        this.idProyecto = id;
        setParametro(modelo, "supervisiones", new Supervisiones());
        setParametro(modelo, "proyectoSupervisiones", new ProyectoSupervisiones());
        return "paginas/supervision/form-supervisiones";
    }

    @GetMapping("/coordinadorTecnico/{id}")
    public String idCoordinadorTecnico(@PathVariable("id") Long id, Model modelo, RedirectAttributes atributo) {

        for (TipoCoordinadores object : servicioTipoCoordinadores.getTodos()) {
            if (object.getNombre().equalsIgnoreCase("Coordinador Tecnico")) {
                this.coordinador.setIdTipoCoordinador(object.getIdTipoCoordinador());
            }
        }
        if (terceraSupervision(id)) {
            atributo.addFlashAttribute("error", "Error - Ya se hicieron las tres supervisiones");
            return "redirect:/misProyectos";
        }
        this.idProyecto = id;
        setParametro(modelo, "supervisiones", new Supervisiones());
        setParametro(modelo, "proyectoSupervisiones", new ProyectoSupervisiones());
        return "paginas/supervision/form-supervisiones";
    }

    @PostMapping("/guardarSupervision")
    public String guardar(Supervisiones supervision, Model model, RedirectAttributes atributo) throws Exception {
        String mensaje = "";
        if (this.idProyecto != null) {
            mensaje = this.validarFechaAnterior(this.idProyecto, supervision, "Guardar");
            if (!mensaje.equals("OK")) {
                atributo.addFlashAttribute("error", mensaje);
                return "redirect:/misProyectos";
            }
            ProyectoSupervisiones proyectoSup = new ProyectoSupervisiones();
            Proyectos proyecto = new Proyectos(this.idProyecto);
            proyectoSup.setIdProyecto(proyecto);
            proyectoSup.setIdTipoCoordinador(this.coordinador);
            proyectoSup.setUsuario(new Usuario(servicioUsuario.getLoggedUser().getId_usuario()));
            servicio.guardar(supervision);
            proyectoSup.setIdSupervision(supervision);
            servicioProyectoSuperv.guardar(proyectoSup);
            if (banderin) {
                atributo.addFlashAttribute("success", "Guardado Correctamente");
            }
            this.idProyecto = null;
        } else {
            this.banderin = false;
            mensaje = this.validarFechaAnterior(supervision.getIdSupervision(), supervision, "Actualizar");
            if (!mensaje.equals("OK")) {
                atributo.addFlashAttribute("error", mensaje);
                return "redirect:/tituloProyecto";
            }
            servicio.guardar(supervision);
            if (banderin == false) {
                this.bitacoraSupervision();
                atributo.addFlashAttribute("success", "Actualizado Correctamente");
                this.banderin = true;
            }
        }
        return "redirect:/misProyectos";
    }

    @GetMapping("/tituloProyecto")
    public String getValorBusqueda(Model model) throws Exception {
        model.addAttribute("valorTitulo", new ProyectoSupervisiones());
        model.addAttribute("listaServicio", servicioProyectoSuperv.getListarSupervisiones(servicioUsuario.getLoggedUser().getId_usuario()));
        return "paginas/supervision/mantenimiento-servicio";
    }

    @GetMapping("/busqueda")
    public String getBuscarTitulo(Model model, @ModelAttribute("valorTitulo") ProyectoSupervisiones entidad) throws Exception {
        String busqueda = entidad.getIdProyecto().getTitulo().replaceAll("^\\s*", "");
        if (busqueda.equals("")) {
            model.addAttribute("listaServicio", servicioProyectoSuperv.getListarSupervisiones(servicioUsuario.getLoggedUser().getId_usuario()));
        } else {
            model.addAttribute("listaServicio", servicioProyectoSuperv.getResultadoBusqueda(busqueda.toUpperCase()));
        }
        return "paginas/supervision/mantenimiento-servicio";
    }

    private void bitacoraSupervision() throws Exception {
        BitacoraSupervision bitacora = new BitacoraSupervision();
        java.util.Date d = new java.util.Date();
        java.sql.Date date2 = new java.sql.Date(d.getTime());
        bitacora.setUsuario(servicioUsuario.getLoggedUser().getUsername());
        bitacora.setFechaEjecucion(date2);
        this.servicioBitacoraSupervision.guardar(bitacora);
    }

    public void setParametro(Model model, String atributo, Object valor) {
        model.addAttribute(atributo, valor);
    }

    private boolean terceraSupervision(Long idProyecto) {
        boolean existe = false;
        int contador = 0;
        for (ProyectoSupervisiones item : servicioProyectoSuperv.getTodos()) {
            if (item.getIdProyecto().getIdProyecto().equals(idProyecto) && item.getIdTipoCoordinador().getIdTipoCoordinador().equals(this.coordinador.getIdTipoCoordinador())) {
                contador++;
            }
        }
        if (contador == 3) {
            return existe = true;
        }
        return existe;
    }

    private String validarFechaAnterior(Long id, Supervisiones supervision, String accion) throws Exception {
        if (accion.equals("Guardar")) {
            for (ProyectoSupervisiones item : servicioProyectoSuperv.getTodos()) {
                if (item.getIdProyecto().getIdProyecto().equals(id) && item.getUsuario().getId_usuario().equals(servicioUsuario.getLoggedUser().getId_usuario())) {
                    if (supervision.getFecha().before(item.getIdSupervision().getFecha())) {
                        return "Error - La fecha debe ser posterior a la fecha de la ultima supervision";
                    }
                }
            }
        } else {
            for (Supervisiones objeto : servicio.getTodos()) {
                if (objeto.getIdSupervision().equals(id) && supervision.getFecha().before(objeto.getFecha())) {
                    return "Error - La fecha debe ser posterior a la fecha de la ultima supervision";
                }
            }
        }
        return "OK";
    }
}
