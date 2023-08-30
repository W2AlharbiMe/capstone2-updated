package com.example.capstone2updated.Service;

import com.example.capstone2updated.Api.Exception.ResourceNotFoundException;
import com.example.capstone2updated.Api.Exception.SimpleException;
import com.example.capstone2updated.DTO.SalesInvoiceDTO;
import com.example.capstone2updated.Model.*;
import com.example.capstone2updated.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SalesInvoiceService {

    private final SalesInvoiceRepository salesInvoiceRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final SalesPersonRepository salesPersonRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryRepository inventoryRepository;
    private final SerialNumberRepository serialNumberRepository;


    public List<SalesInvoice> findAll() {
        return salesInvoiceRepository.findAll();
    }

    public SalesInvoice findById(Integer id) throws ResourceNotFoundException {
        SalesInvoice salesInvoice = salesInvoiceRepository.findSalesInvoiceById(id);

        if(salesInvoice == null) {
            throw new ResourceNotFoundException("sales invoice");
        }

        return salesInvoice;
    }

    public SalesInvoice findByInvoiceUUID(String invoiceUUID) throws ResourceNotFoundException {
        SalesInvoice salesInvoice = salesInvoiceRepository.findSalesInvoiceByInvoiceUUID(invoiceUUID);

        if(salesInvoice == null) {
            throw new ResourceNotFoundException("sales invoice");
        }

        return salesInvoice;
    }

    public List<SalesInvoice> findByCustomerId(Integer customerId) throws ResourceNotFoundException {
        List<SalesInvoice> salesInvoice = salesInvoiceRepository.findSalesInvoicesByCustomerId(customerId);

        if(salesInvoice.isEmpty()) {
            throw new ResourceNotFoundException("sales invoice");
        }

        return salesInvoice;
    }

    public List<SalesInvoice> findBySalesPersonId(Integer salesPersonId) throws ResourceNotFoundException {
        List<SalesInvoice> salesInvoice = salesInvoiceRepository.findSalesInvoicesBySalespersonId(salesPersonId);

        if(salesInvoice.isEmpty()) {
            throw new ResourceNotFoundException("sales invoice");
        }

        return salesInvoice;
    }


    public HashMap<String, Object> addSalesInvoice(SalesInvoiceDTO salesInvoiceDTO, User user) throws SimpleException, ResourceNotFoundException {

        Customer customer = customerRepository.findCustomerById(salesInvoiceDTO.getCustomerId());

        if(customer == null) {
            throw new ResourceNotFoundException("customer");
        }

        SalesInvoice salesInvoice = new SalesInvoice();
        HashMap<String, Object> response = new HashMap<>();



        Car car = carRepository.findCarById(salesInvoiceDTO.getCarId());

        if(car == null) {
            throw new ResourceNotFoundException("car");
        }


        SerialNumber serialNumber;
        Double instalmentTotal = 0.0;
        InventoryItem inventoryItem;

        // what is the type?
        switch (salesInvoiceDTO.getType().toLowerCase()) {
            case "full_car_payment":
                inventoryItem = inventoryItemRepository.findInventoryItemById(salesInvoiceDTO.getInventoryItemId());

                if(inventoryItem == null) {
                    throw new ResourceNotFoundException("inventoryItem");
                }

                if(inventoryItem.getType().equalsIgnoreCase("part")) {
                    throw new SimpleException("you can not instalment part.");
                }

                if(inventoryItem.getQuantity() == 0) {
                    throw new ResourceNotFoundException("car out of stock.");
                }

                // generate serial number
                serialNumber = generateSerialNumber(car);

                salesInvoice.setCar(car);
                salesInvoice.setSerialNumber(serialNumber);

                inventoryItem.setQuantity(inventoryItem.getQuantity() - 1);
                inventoryItemRepository.save(inventoryItem);
                break;

            case "instalment_car_payment":
                if(salesInvoiceDTO.getInstalmentPerMonth() < (salesInvoiceDTO.getSubPrice() * 0.20)) {
                    throw new SimpleException("the instalment per month must be at least equal to ("+ (salesInvoiceDTO.getSubPrice() * 0.20) +") SAR. in other words the instalment per month must be 20% of the sub price which is ("+ salesInvoiceDTO.getSubPrice() +") SAR.");
                }

                inventoryItem = inventoryItemRepository.findInventoryItemById(salesInvoiceDTO.getInventoryItemId());

                if(inventoryItem == null) {
                    throw new ResourceNotFoundException("inventoryItem");
                }

                if(inventoryItem.getType().equalsIgnoreCase("part")) {
                    throw new SimpleException("you can not instalment part.");
                }

                if(inventoryItem.getQuantity() == 0) {
                    throw new ResourceNotFoundException("car out of stock.");
                }

                // is this is a new instalment invoice ?
                List<SalesInvoice> salesInvoicesByCustomerId = salesInvoiceRepository.findAllInstalmentByCustomerIdAndCarId(customer.getId(), car.getId());

                if(salesInvoicesByCustomerId.isEmpty()) {
                    // new instalment
                    salesInvoice.setMonthNumber(1);

                    // generate serial number
                    serialNumber = generateSerialNumber(car);

                    salesInvoice.setCar(car);
                    salesInvoice.setSerialNumber(serialNumber);

                    inventoryItem.setQuantity(inventoryItem.getQuantity() - 1);
                    inventoryItemRepository.save(inventoryItem);
                } else {
                    salesInvoicesByCustomerId.sort((o1, o2) -> o2.getMonthNumber().compareTo(o1.getMonthNumber()));

                    // check if the instalment finished.
                    instalmentTotal = salesInvoiceRepository.sumAllInstalmentByCustomerIdAndCarId(customer.getId(), car.getId());

                    response.put("instalmentTotal", instalmentTotal);
                    Double vat = salesInvoiceDTO.getVat() == null ? 0.15 : salesInvoiceDTO.getVat();
                    Double totalPrice = ((salesInvoiceDTO.getSubPrice() * vat) + salesInvoiceDTO.getSubPrice());

                    if(totalPrice > (totalPrice - instalmentTotal)) {
                        salesInvoice.setMonthNumber(salesInvoicesByCustomerId.get(0).getMonthNumber() + 1);

                        SerialNumber serialNumber1 = salesInvoiceRepository.findByCustomerAndCar(customer.getId(), car.getId()).getSerialNumber();
                        SerialNumber sameSerialNumber = new SerialNumber();
                        sameSerialNumber.setSerialNumber(serialNumber1.getSerialNumber());
                        sameSerialNumber.setCar(car);

                        salesInvoice.setSerialNumber(sameSerialNumber);


                        inventoryItem.setQuantity(inventoryItem.getQuantity() - 1);
                        inventoryItemRepository.save(inventoryItem);
                    }

                    if(totalPrice <= (totalPrice - instalmentTotal)) {
                        throw new SimpleException("this customer have already bought this car.");
                    }
                }


                break;
        }




        // defaults
        salesInvoice.setInvoiceUUID(UUID.randomUUID().toString());
        salesInvoice.setSalesPersonBonus(salesInvoice.getSalesPersonBonusRate() * salesInvoiceDTO.getSubPrice());

        // from DTO
        salesInvoice.setStatus(salesInvoiceDTO.getStatus());
        salesInvoice.setType(salesInvoiceDTO.getType());
        salesInvoice.setInstalmentPerMonth(salesInvoiceDTO.getInstalmentPerMonth());
        salesInvoice.setVat(salesInvoiceDTO.getVat() == null ? 0.15 : salesInvoiceDTO.getVat());
        salesInvoice.setSubPrice(salesInvoiceDTO.getSubPrice());



        // relations
        salesInvoice.setCustomer(customer);
        salesInvoice.setSalesperson(salesPersonRepository.findSalesPersonById(user.getId()));

        // save
        salesInvoice = salesInvoiceRepository.save(salesInvoice);



        response.put("message", "the invoice have been created.");
        response.put("salesInvoice", salesInvoice);

        if(salesInvoiceDTO.getType().equalsIgnoreCase("instalment_car_payment")) {
            response.put("totalPriceAfterCalculations", salesInvoice.getTotalPrice() - instalmentTotal);
        }

        return response;
    }

    private SerialNumber generateSerialNumber(Car car) {
        SerialNumber serialNumber = new SerialNumber();
        serialNumber.setCar(car);
        serialNumber.generateSerialNumber();
        serialNumber.setIsUsed(true);

        return serialNumberRepository.save(serialNumber);
    }

}
