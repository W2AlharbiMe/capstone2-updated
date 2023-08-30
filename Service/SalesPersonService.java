package com.example.capstone2updated.Service;

import com.example.capstone2updated.Api.Exception.ResourceNotFoundException;
import com.example.capstone2updated.Api.Exception.SimpleException;
import com.example.capstone2updated.DTO.UpdateSalesPersonDTO;
import com.example.capstone2updated.Model.SalesInvoice;
import com.example.capstone2updated.Model.SalesPerson;
import com.example.capstone2updated.Model.User;
import com.example.capstone2updated.Repository.AuthRepository;
import com.example.capstone2updated.Repository.SalesInvoiceRepository;
import com.example.capstone2updated.Repository.SalesPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesPersonService {

    private final SalesPersonRepository salesPersonRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;
    private final AuthRepository authRepository;

    public List<SalesPerson> findAll() {
        return salesPersonRepository.findAll();
    }

    public List<SalesPerson> searchByName(String name) throws ResourceNotFoundException {
        List<SalesPerson> salesPeople = salesPersonRepository.lookByName(name);

        if(salesPeople.isEmpty()) {
            throw new ResourceNotFoundException("sales persons");
        }

        return salesPeople;
    }

    public SalesPerson findById(Integer id) throws ResourceNotFoundException {
        SalesPerson salesPerson = salesPersonRepository.findSalesPersonById(id);

        if(salesPerson == null) {
            throw new ResourceNotFoundException("sales person");
        }

        return salesPerson;
    }

    public void salesPersonExists(Integer id) throws SimpleException {
        if(salesPersonRepository.findSalesPersonById(id) == null) {
            throw new SimpleException("no sales person found with the sales person id you provided.");
        }
    }

    public HashMap<String, Object> updateSalesPerson(Integer id, UpdateSalesPersonDTO updateSalesPersonDTO) throws ResourceNotFoundException, SimpleException {
        SalesPerson salesPerson = salesPersonRepository.findSalesPersonById(id);

        if(salesPerson == null) {
            throw new ResourceNotFoundException("sales person");
        }

        salesPerson.setName(updateSalesPersonDTO.getName());
        salesPerson.setSalary(updateSalesPersonDTO.getSalary());
        salesPerson.setActive(updateSalesPersonDTO.getActive());

        salesPersonRepository.save(salesPerson);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the sales person have been updated.");
        response.put("salesPerson", salesPerson);

        return response;
    }

    public HashMap<String, Object> deleteSalesPerson(Integer id) throws ResourceNotFoundException {
        SalesPerson salesPerson = salesPersonRepository.findSalesPersonById(id);

        if(salesPerson == null) {
            throw new ResourceNotFoundException("sales person");
        }

        // you can not delete sales person if they have sales invoice
        if(salesInvoiceRepository.atLeastOneSalesBySalesPersonId(id) != null) {
            // instead deactivate their accounts.
            salesPerson.setActive(false);
            salesPersonRepository.save(salesPerson);

            HashMap<String, Object> response = new HashMap<>();
            response.put("message", "the sales person have been deactivated because there are invoices registered with their IDs.");
            response.put("salesPerson", salesPerson);

            return response;
        }

        salesPersonRepository.deleteById(id);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the sales person have been deleted.");
        response.put("salesPerson", salesPerson);

        return response;
    }


    public HashMap<String, Object> viewMyDetails(User user) throws ResourceNotFoundException {
        SalesPerson salesPerson = salesPersonRepository.findSalesPersonById(user.getId());

        Optional<Double> sumSalesPersonBonus = salesInvoiceRepository.sumSalesPersonBonus(user.getId());
        List<SalesInvoice> salesInvoices = salesInvoiceRepository.lookForSalesInvoicesBySalesPersonId(user.getId());

        HashMap<String, Object> response = new HashMap<>();
        response.put("salesPerson", salesPerson);

        response.put("Bonus", sumSalesPersonBonus.orElse(0.0));
        response.put("salesInvoices", salesInvoices);

        return response;
    }
}
