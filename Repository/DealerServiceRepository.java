package com.example.capstone2updated.Repository;

import com.example.capstone2updated.Model.DealerService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealerServiceRepository extends JpaRepository<DealerService, Integer> {
    DealerService findDealerServiceById(Integer id);
}
