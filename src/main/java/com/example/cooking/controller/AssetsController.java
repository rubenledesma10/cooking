package com.example.cooking.controller;

import com.example.cooking.service.AlmacenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assets")
public class AssetsController {
    
    @Autowired
    private AlmacenService almacenServ;
    
    @GetMapping("/{nombreArchivo:.+}")
    public Resource obtenerArchivo(@PathVariable("nombreArchivo") String nombreArchivo){
        return almacenServ.cargarComoRecurso(nombreArchivo);
    }
}
