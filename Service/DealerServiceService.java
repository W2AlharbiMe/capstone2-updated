package com.example.capstone2updated.Service;

import com.example.capstone2updated.Api.Exception.ResourceNotFoundException;
import com.example.capstone2updated.DTO.DealerServiceDTO;
import com.example.capstone2updated.Model.DealerService;
import com.example.capstone2updated.Repository.DealerServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealerServiceService {

    private final DealerServiceRepository dealerServiceRepository;


    public List<DealerService> findAll() {
        return dealerServiceRepository.findAll();
    }

    public DealerService findById(Integer id) {
        DealerService dealerService = dealerServiceRepository.findDealerServiceById(id);

        if(dealerService == null) {
            throw new ResourceNotFoundException("dealer service");
        }

        return dealerService;
    }

    public HashMap<String, Object> addService(DealerServiceDTO dealerServiceDTO) {

        DealerService dealerService = new DealerService();

        dealerService.setName(dealerServiceDTO.getName());
        dealerService.setPrice(dealerServiceDTO.getPrice());

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the dealer service have been added.");
        response.put("dealerService", dealerServiceRepository.save(dealerService));

        return response;
    }


    public HashMap<String, Object> updateService(Integer id, DealerServiceDTO dealerServiceDTO) {
        DealerService savedDealerService = findById(id);

        savedDealerService.setName(dealerServiceDTO.getName());
        savedDealerService.setPrice(dealerServiceDTO.getPrice());

        dealerServiceRepository.save(savedDealerService);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the part have been updated.");
        response.put("dealerService", savedDealerService);

        return response;
    }



    public HashMap<String, Object> deleteService(Integer id) {
        DealerService savedDealerService = findById(id);


        dealerServiceRepository.deleteById(id);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the dealer service have been deleted.");
        response.put("dealerService", savedDealerService);

        return response;
    }

}
