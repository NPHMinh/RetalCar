package com.minhnphde180174.fu.hsf301assigment1.repository;

import com.minhnphde180174.fu.hsf301assigment1.entity.*;
import com.minhnphde180174.fu.hsf301assigment1.entity.Car;
import com.minhnphde180174.fu.hsf301assigment1.entity.CarCustomerID;
import com.minhnphde180174.fu.hsf301assigment1.entity.CarRental;
import com.minhnphde180174.fu.hsf301assigment1.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarRentalRepository extends JpaRepository<CarRental, CarCustomerID> {


    List<CarRental> findByCustomer_CustomerId(Long customerId);

    List<CarRental> findByRentDateBetween(LocalDate startDate, LocalDate endDate);



    CarRental findByCustomerAndCar(Customer existingCustomer, Car car);
}
