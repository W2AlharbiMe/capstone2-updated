package com.example.capstone2updated.Service;

import com.example.capstone2updated.Api.Exception.ResourceNotFoundException;
import com.example.capstone2updated.Api.Exception.SimpleException;
import com.example.capstone2updated.DTO.CarDTO;
import com.example.capstone2updated.Model.Car;
import com.example.capstone2updated.Model.Manufacturer;
import com.example.capstone2updated.Model.SerialNumber;
import com.example.capstone2updated.Repository.CarRepository;
import com.example.capstone2updated.Repository.ManufacturerRepository;
import com.example.capstone2updated.Repository.SalesInvoiceRepository;
import com.example.capstone2updated.Repository.SerialNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;


    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public Car findCarById(Integer id) throws ResourceNotFoundException {
        Car car = carRepository.findCarById(id);

        if(car == null) {
            throw new ResourceNotFoundException("car");
        }

        return car;
    }

    public void carExists(Integer id) throws SimpleException {
        if(carRepository.findCarById(id) == null) {
            throw new SimpleException("no car found with the car id you provided.");
        }
    }

    public List<Car> findCarsByManufacturerId(Integer manufacturerId) throws SimpleException {
        List<Car> cars = carRepository.findAllManufacturerCars(manufacturerId);

        if(cars.isEmpty()) {
            throw new SimpleException("no cars found with the manufacturer id you provided.");
        }

        return cars;
    }

    public HashMap<String, Object> addCar(CarDTO carDTO) throws SimpleException {
        Manufacturer manufacturer = manufacturerRepository.findManufacturerById(carDTO.getManufacturerId());

        if(manufacturer == null) {
            throw new SimpleException("manufacturer not found.");
        }

        Car car = new Car();

        car.setType(carDTO.getType().toLowerCase()); // ensure stored type not like SaDeN etc...
        car.setColor(carDTO.getColor().toLowerCase()); // ensure stored color not like ReD, GrEen etc...
        car.setModel(carDTO.getModel());
        car.setReleaseYear(carDTO.getReleaseYear());
        car.setSeatsCount(carDTO.getSeatsCount());

        car.setManufacturer(manufacturer);

        Car saved_car = carRepository.save(car);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the car have been added.");
        response.put("car", saved_car);


        return response;
    }

    public HashMap<String, Object> updateCar(Integer id, CarDTO carDTO) throws ResourceNotFoundException, SimpleException {
        Car car = carRepository.findCarById(id);

        if(car == null) {
            throw new ResourceNotFoundException("car");
        }

        Manufacturer manufacturer = manufacturerRepository.findManufacturerById(carDTO.getManufacturerId());
        if(manufacturer == null) {
            throw new SimpleException("manufacturer not found.");
        }


        car.setModel(carDTO.getModel());
        car.setType(carDTO.getType());
        car.setColor(carDTO.getColor());
        car.setSeatsCount(carDTO.getSeatsCount());
        car.setReleaseYear(carDTO.getReleaseYear());
        car.setManufacturer(manufacturer);

        carRepository.save(car);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the car have been updated.");
        response.put("car", car);

        return response;
    }

    public HashMap<String, Object> deleteCar(Integer id) throws ResourceNotFoundException, SimpleException {
        Car car = carRepository.findCarById(id);

        if(car == null) {
            throw new ResourceNotFoundException("car");
        }

        if(salesInvoiceRepository.lookForSalesByCarId(id) != null) {
            throw new SimpleException("you cannot delete this car because there's a registered invoice with this car id.");
        }

        carRepository.deleteById(id);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the car have been deleted.");
        response.put("car", car);

        return response;
    }
}
