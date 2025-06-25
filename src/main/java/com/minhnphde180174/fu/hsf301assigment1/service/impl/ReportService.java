package com.minhnphde180174.fu.hsf301assigment1.service.impl;

import com.minhnphde180174.fu.hsf301assigment1.dto.ReportSummary;
import com.minhnphde180174.fu.hsf301assigment1.entity.CarRental;

import com.minhnphde180174.fu.hsf301assigment1.entity.CarRentalStatus;
import com.minhnphde180174.fu.hsf301assigment1.repository.CarRentalRepository;

import com.minhnphde180174.fu.hsf301assigment1.service.BaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService extends BaseService {

    private final CarRentalRepository rentalRepository;

    public ReportService(CarRentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public Map<String, Object> generateReport(LocalDate startDate, LocalDate endDate) {
        List<CarRental> rentals = rentalRepository.findByRentDateBetween(startDate, endDate);
        ReportSummary summary = buildSummary(rentals);

        Map<String, Object> data = new HashMap<>();
        data.put("reportData", summary);
        data.put("reportResults", rentals);
        return data;
    }

    private ReportSummary buildSummary(List<CarRental> rentals) {
        long totalTransactions = rentals.size();
        double totalRevenue = rentals.stream().mapToDouble(CarRental::getRentPrice).sum();
        long completed = rentals.stream().filter(r -> r.getStatus() == CarRentalStatus.RETURNED).count();
        double completionRate = totalTransactions == 0 ? 0 : (completed * 100.0 / totalTransactions);

        // most rented car
        String mostRentedCar = rentals.stream()
                .collect(Collectors.groupingBy(r -> r.getCar().getCarName(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        return new ReportSummary(totalTransactions, totalRevenue, completionRate, mostRentedCar);
    }
}