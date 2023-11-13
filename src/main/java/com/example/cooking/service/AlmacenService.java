package com.example.cooking.service;

import com.example.cooking.excepciones.AlmacenException;
import com.example.cooking.excepciones.FileNotFoundException;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AlmacenService implements IAlmacenServicio {

    @Value("${storage.location}")
    private String storageLocation;
    
    //esta anotacion sirve para indicar que este metodo se va a ejecutar cada vez que haya una nueva instancia de esta clase
    @PostConstruct
    
    //con este metodo indicamos donde se van a guardar nuestras fotos (el directorio)
    @Override
    public void iniciarAlmacenDeArchivos() {
        try {
            Files.createDirectories(Paths.get(storageLocation)); //con el file vamos a crear un directorio, con el storage vamos a obtener el nombre donde vamos a guardar el directorio
        } catch (IOException exception) {
            throw new AlmacenException("Error a iniciar la ubicación");
        }
    }
    
    //
    @Override
    public String almacenarArchivo(MultipartFile archivo) {
        String nombreArchivo = archivo.getOriginalFilename(); //obtenemos el archivo
        if(archivo.isEmpty()){ //nos fijamos si el archivo esta vacio
            throw new AlmacenException("No se puede almacenar un archivo vacio");
        }
        try {
            InputStream inputStream = archivo.getInputStream();
            Files.copy(inputStream, Paths.get(storageLocation).resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new AlmacenException("Error al almacenar el archivo " +nombreArchivo,exception);
        }
        return nombreArchivo;
    }

    @Override
    public Path cargarArchivo(String nombreArchivo) {
        return Paths.get(storageLocation).resolve(nombreArchivo);
    }

    @Override
    public Resource cargarComoRecurso(String nombreArchivo) {
        try {
            Path archivo = cargarArchivo(nombreArchivo);
            Resource recurso = new UrlResource(archivo.toUri());
            
            if (recurso.exists()||recurso.isReadable()){ //si el recurso existe o si es leible
                return recurso;
            } else {
                throw new FileNotFoundException("No se pudo encontrar el archivo " +nombreArchivo);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("No se pudo encontrar el archivo " +nombreArchivo, e);
        }
    }

    @Override
    public void eliminarArchivo(String nombreArchivo) {
        Path archivo = cargarArchivo(nombreArchivo);
        try {
            FileSystemUtils.deleteRecursively(archivo);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
