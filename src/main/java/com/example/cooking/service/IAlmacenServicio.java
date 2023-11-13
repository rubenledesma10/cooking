
package com.example.cooking.service;

import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IAlmacenServicio {

    public void iniciarAlmacenDeArchivos();
    
    public String almacenarArchivo(MultipartFile archivo);
    
    public Path cargarArchivo(String nombreArchivo);
    
    public Resource cargarComoRecurso(String nombreArchivo);
    
    public void eliminarArchivo(String nombreArchivo);
    
}
