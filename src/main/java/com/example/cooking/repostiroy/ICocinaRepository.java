
package com.example.cooking.repostiroy;

import com.example.cooking.model.Cocina;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICocinaRepository extends JpaRepository <Cocina, Long> {
    
}
