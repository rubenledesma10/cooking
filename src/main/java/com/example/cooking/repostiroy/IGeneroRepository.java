
package com.example.cooking.repostiroy;

import com.example.cooking.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGeneroRepository extends JpaRepository<Genero, Long> {
    
}
