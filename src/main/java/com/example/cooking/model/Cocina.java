
package com.example.cooking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cocina {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_comida;
    @NotBlank
    private String titulo;
    @NotBlank
    private String descripcion;
    @NotBlank
    private String ingredientes;
    @NotNull
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate fechaPublicacion;
    @NotBlank
    private String videoComida;
    private String rutaComida;
    @NotEmpty //para que no permita datos vacios
    @ManyToMany (fetch = FetchType.LAZY)//una comida va a poder tener muchos generos y un genero puede tener muchas peliculas, y va a ser lazy para que se cargue cuando lo necesitemos
    @JoinTable(name="genero_comida", //vamos a unir tablas, a traves de esta tabla (genero_comida)
              joinColumns = @JoinColumn(name="id_peli"), //en las columnas de esta tabla va a tener id_pelicula y el id_genero
              inverseJoinColumns = @JoinColumn(name="id_gene"))
    private List<Genero> listaGeneros;
    
    @Transient //usamos esto para que sea un dato temporal, para que no se guarde en la BD
    private MultipartFile portada;
    
}
