package com.example.capstone2updated.Repository;

import com.example.capstone2updated.Model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, Integer> {

    Part findPartById(Integer id);

}
