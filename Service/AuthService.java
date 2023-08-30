package com.example.capstone2updated.Service;

import com.example.capstone2updated.Api.Exception.SimpleException;
import com.example.capstone2updated.DTO.SalesPersonDTO;
import com.example.capstone2updated.Model.SalesPerson;
import com.example.capstone2updated.Model.User;
import com.example.capstone2updated.Repository.AuthRepository;
import com.example.capstone2updated.Repository.CustomerRepository;
import com.example.capstone2updated.Repository.SalesPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final SalesPersonRepository salesPersonRepository;
    private final CustomerRepository customerRepository;

    public SalesPerson createSalesPerson(SalesPersonDTO salesPersonDTO) {

        if(authRepository.findUserByUsername(salesPersonDTO.getUsername()) != null) {
            throw new SimpleException("use another username.");
        }

        if(authRepository.findUserByEmail(salesPersonDTO.getEmail()) != null) {
            throw new SimpleException("use another email.");
        }



        User user = new User();

        user.setUsername(salesPersonDTO.getUsername());

        user.setPassword((new BCryptPasswordEncoder()).encode(salesPersonDTO.getPassword()));

        user.setRole("SALES_PERSON");

        // first account
//        user.setRole("ADMIN");


        user.setEmail(salesPersonDTO.getEmail());

        User user1 = authRepository.save(user);


        SalesPerson salesPerson = new SalesPerson();

        salesPerson.setActive(true);
        salesPerson.setName(salesPersonDTO.getName());
        salesPerson.setSalary(salesPersonDTO.getSalary());
        salesPerson.setUser(user1);

        return salesPersonRepository.save(salesPerson);
    }


}
