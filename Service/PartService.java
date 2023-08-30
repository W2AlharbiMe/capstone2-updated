package com.example.capstone2updated.Service;

import com.example.capstone2updated.Api.Exception.ResourceNotFoundException;
import com.example.capstone2updated.DTO.PartDTO;
import com.example.capstone2updated.Model.Part;
import com.example.capstone2updated.Repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;


    public List<Part> findAll() {
        return partRepository.findAll();
    }

    public Part findById(Integer id) {
        Part part = partRepository.findPartById(id);

        if(part == null) {
            throw new ResourceNotFoundException("part");
        }

        return part;
    }

    public HashMap<String, Object> addPart(PartDTO partDTO) {
        Part part = new Part();

        part.setName(partDTO.getName());
        part.setDescription(partDTO.getDescription());
        part.setPurchasePrice(partDTO.getPurchasePrice());
        part.setIsUsed(partDTO.getIsUsed());


        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the part have been added.");
        response.put("part", partRepository.save(part));

        return response;
    }


    public HashMap<String, Object> updatePart(Integer id, PartDTO partDTO) {
        Part part = findById(id);

        part.setName(partDTO.getName());
        part.setDescription(partDTO.getDescription());
        part.setPurchasePrice(partDTO.getPurchasePrice());
        part.setIsUsed(partDTO.getIsUsed());

        partRepository.save(part);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the part have been updated.");
        response.put("part", part);

        return response;
    }


    public HashMap<String, Object> deletePart(Integer id) {
        Part savedPart = findById(id);

        partRepository.deleteById(id);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the part have been deleted.");
        response.put("part", savedPart);

        return response;
    }
}
