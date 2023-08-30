package com.example.capstone2updated.Service;

import com.example.capstone2updated.Api.Exception.ResourceNotFoundException;
import com.example.capstone2updated.Api.Exception.SimpleException;
import com.example.capstone2updated.DTO.CustomerDTO;
import com.example.capstone2updated.Model.Customer;
import com.example.capstone2updated.Model.User;
import com.example.capstone2updated.Repository.CustomerRepository;
import com.example.capstone2updated.Repository.SalesInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;


    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Integer id) throws ResourceNotFoundException {
        Customer customer = customerRepository.findCustomerById(id);

        if(customer == null) {
            throw new ResourceNotFoundException("customer");
        }

        return customer;
    }

    public void customerExists(Integer id) throws SimpleException {
        if(customerRepository.findCustomerById(id) == null) {
            throw new SimpleException("no customer found with the customer id you provided.");
        }
    }

    public Customer findByNationalId(String nationalId) throws ResourceNotFoundException {
        Customer customer = customerRepository.findCustomerByNationalIdentity(nationalId);

        if(customer == null) {
            throw new ResourceNotFoundException("customer");
        }

        return customer;
    }

    public Customer findByPhoneNumber(String phoneNumber) throws ResourceNotFoundException {
        Customer customer = customerRepository.findCustomerByPhoneNumber(phoneNumber);

        if(customer == null) {
            throw new ResourceNotFoundException("customer");
        }

        return customer;
    }

    public HashMap<String, Object> createCustomer(CustomerDTO customerDTO) throws SimpleException {
        // make sure there's no customer with the same national identity
        if(customerRepository.findCustomerByNationalIdentity(customerDTO.getNationalIdentity()) != null) {
            throw new SimpleException("the customer national identity is used.");
        }

        // make sure there's no customer with the same phone number
        if(customerRepository.findCustomerByPhoneNumber(customerDTO.getPhoneNumber()) != null) {
            throw new SimpleException("the customer phone number is used.");
        }

        Customer customer = new Customer();

        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNationalIdentity(customerDTO.getNationalIdentity());
        customer.setAddress(customerDTO.getAddress());
        customer.setCity(customerDTO.getCity());
        customer.setCountry(customerDTO.getCountry());
        customer.setPostalCode(customerDTO.getPostalCode());
        customer.setSalesInvoices(Set.of());


        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the customer have been added.");
        response.put("customer", customerRepository.save(customer));

        return response;
    }

    public HashMap<String, Object> updateCustomer(Integer id, CustomerDTO customerDTO) throws ResourceNotFoundException, SimpleException {
        Customer customer = customerRepository.findCustomerById(id);

        if(customer == null) {
            throw new ResourceNotFoundException("customer");
        }

        // make sure there's no customer with the same phone number
        Customer phoneValidation = customerRepository.findCustomerByPhoneNumber(customerDTO.getPhoneNumber());

        if(!Objects.equals(phoneValidation.getId(), customer.getId())) {
            throw new SimpleException("the customer phone number is used.");
        }

        customer.setName(customerDTO.getName());
        customer.setCity(customerDTO.getCity());
        customer.setAddress(customerDTO.getAddress());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setPostalCode(customerDTO.getPostalCode());
        customer.setCountry(customerDTO.getCountry());

        customerRepository.save(customer);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the customer have been updated.");
        response.put("customer", customer);

        return response;
    }

    public HashMap<String, Object> deleteCustomer(Integer id, User user) throws ResourceNotFoundException, SimpleException {
        if(user.getRole().equalsIgnoreCase("SALES_PERSON")) {
            throw new SimpleException("You don't have permissions to delete customers.");
        }

        Customer customer = customerRepository.findCustomerById(id);

        if(customer == null) {
            throw new ResourceNotFoundException("customer");
        }

        if(salesInvoiceRepository.atCustomerHaveLeastOneInvoice(id) != null) {
            throw new SimpleException("you cannot delete this customer because there's a registered invoice with this customer id.");
        }

        customerRepository.deleteById(id);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the customer have been deleted.");
        response.put("customer", customer);

        return response;
    }
}
