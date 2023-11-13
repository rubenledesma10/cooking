package com.example.cooking.controller;

import com.example.cooking.model.Cocina;
import com.example.cooking.model.Genero;
import com.example.cooking.repostiroy.ICocinaRepository;
import com.example.cooking.repostiroy.IGeneroRepository;
import com.example.cooking.service.AlmacenService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class GestionController {

    @Autowired
    private ICocinaRepository cocinaRepo;

    @Autowired
    private IGeneroRepository generoRepo;

    @Autowired
    private AlmacenService almacenServ;

    @GetMapping("")
    public ModelAndView verInicio(@PageableDefault(sort = "titulo") Pageable pageable) { //con esto le decimos que se va a ordenar por el titulo 
        Page<Cocina> cocinas = cocinaRepo.findAll(pageable); //tramos todas las recetas y la vamos a ordenar por el titulo
        return new ModelAndView("/admin/index").addObject("cocinas", cocinas);
    }

    @GetMapping("/cocina/nuevo")
    public ModelAndView nuevaCocina() { //para agregar una nueva receta
        List<Genero> listaGeneros = generoRepo.findAll(Sort.by("nombre"));
        return new ModelAndView("/admin/nueva-cocina").addObject("cocina", new Cocina()).addObject("listaGeneros", listaGeneros);
    }

    @PostMapping("/cocina/nuevo")
    public ModelAndView registrarReceta(@Validated Cocina cocina, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || cocina.getPortada().isEmpty()) {
            if (cocina.getPortada().isEmpty()) {
                bindingResult.rejectValue("portada", "MultipartNotEmpty");
            }
            List<Genero> listaGeneros = generoRepo.findAll(Sort.by("nombre"));
            return new ModelAndView("/admin/nueva-cocina").addObject("cocina", cocina).addObject("listaGeneros", listaGeneros);
        }
        String rutaPortada = almacenServ.almacenarArchivo(cocina.getPortada());
        cocina.setRutaComida(rutaPortada);
        cocinaRepo.save(cocina);
        return new ModelAndView("redirect:/admin");

    }

    @GetMapping("/recetas/{id_comida}/editar")
    public ModelAndView verDatosReceta(@PathVariable Long id_comida) {
        Cocina cocina = cocinaRepo.getOne(id_comida);
        List<Genero> listaGeneros = generoRepo.findAll(Sort.by("nombre"));
        return new ModelAndView("/admin/editar-cocina").addObject("cocina", cocina).addObject("listaGeneros", listaGeneros);
    }

    @PostMapping("/recetas/{id_comida}/editar")
    public ModelAndView editarRecetar(@PathVariable Long id_comida, @Validated Cocina cocina, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Genero> listaGeneros = generoRepo.findAll(Sort.by("nombre"));
            return new ModelAndView("/admin/editar-cocina")
                    .addObject("cocina", cocina)
                    .addObject("listaGeneros", listaGeneros);
        }

        Cocina cocina2 = cocinaRepo.getOne(id_comida);
        cocina2.setTitulo(cocina.getTitulo());
        cocina2.setDescripcion(cocina.getDescripcion());
        cocina2.setFechaPublicacion(cocina.getFechaPublicacion());
        cocina2.setIngredientes(cocina.getIngredientes());
        cocina2.setPortada(cocina.getPortada());
        cocina2.setRutaComida(cocina.getRutaComida());
        cocina2.setVideoComida(cocina.getVideoComida());
        cocina2.setListaGeneros(cocina.getListaGeneros());

        String rutaComida = almacenServ.almacenarArchivo(cocina.getPortada());
        cocina2.setRutaComida(rutaComida);
        cocinaRepo.save(cocina2);
        return new ModelAndView("redirect:/admin");
    }
    
    @GetMapping("/recetas/{id_comida}/eliminar")
    public ModelAndView eliminarReceta (@PathVariable Long id_comida){
        Cocina cocina = cocinaRepo.getOne(id_comida);
        cocinaRepo.delete(cocina);
        almacenServ.eliminarArchivo(cocina.getRutaComida());
        return new ModelAndView("redirect:/admin");
    }

}
