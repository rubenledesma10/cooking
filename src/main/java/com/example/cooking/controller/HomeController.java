package com.example.cooking.controller;

import com.example.cooking.model.Cocina;
import com.example.cooking.repostiroy.ICocinaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class HomeController {

    @Autowired
    private ICocinaRepository cocinaRepo;

    @GetMapping("")
    public ModelAndView verPaginaInicio() {
        List<Cocina> ultimasRecetas = cocinaRepo.findAll(PageRequest.of(0, 4, Sort.by("fechaPublicacion").descending())).toList();
        return new ModelAndView("index").addObject("ultimasRecetas", ultimasRecetas);
    }

    @GetMapping("/recetas")
    public ModelAndView listarRecetas(@PageableDefault(sort = "fechaPublicacion", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Cocina> cocinas = cocinaRepo.findAll(pageable);
        return new ModelAndView("recetas").addObject("cocinas", cocinas);
    }
    
    @GetMapping("/recetas/{id_comida}")
    public ModelAndView detallesRecetas(@PathVariable Long id_comida){
        Cocina cocina = cocinaRepo.getOne(id_comida);
        return new ModelAndView("verReceta").addObject("cocina", cocina);
    }
    
}
