package com.example.capstone2updated.Repository;

import com.example.capstone2updated.Model.SerialNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SerialNumberRepository extends JpaRepository<SerialNumber, Integer> {

    public SerialNumber findSerialNumberById(Integer id);

    public SerialNumber findCarBySerialNumber(String serialNumber);

    @Query("SELECT s FROM serial_numbers s WHERE s.car.id = ?1 AND s.isUsed = false ORDER BY s.id DESC LIMIT 1")
    SerialNumber latestUnusedSerialNumberByCarId(Integer carId);

}
