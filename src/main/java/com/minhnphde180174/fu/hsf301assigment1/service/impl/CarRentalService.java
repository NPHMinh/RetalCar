package com.minhnphde180174.fu.hsf301assigment1.service.impl;

import com.minhnphde180174.fu.hsf301assigment1.entity.*;
import com.minhnphde180174.fu.hsf301assigment1.entity.*;
import com.minhnphde180174.fu.hsf301assigment1.repository.CarRentalRepository;
import com.minhnphde180174.fu.hsf301assigment1.repository.CustomerRepository;
import com.minhnphde180174.fu.hsf301assigment1.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CarRentalService extends BaseService {
    private final CarRentalRepository carRentalRepository;
    private final CustomerRepository customerRepository;
    private final CarService carService;

    public void rentailCar(Customer customer, Car car, String pickupDate, String returnDate) {
        logger.info("Bắt đầu xử lý thuê xe: customerId={}, carId={}, pickupDate={}, returnDate={}",
                customer.getCustomerId(), car.getCarId(), pickupDate, returnDate);

        CarCustomerID id = new CarCustomerID();
        id.setCarId(car.getCarId());
        id.setCustomerId(customer.getCustomerId());

        // Kiểm tra xem khách hàng đã từng thuê xe này chưa
        CarRental existingRental = carRentalRepository.findByCustomerAndCar(customer, car);
        if (existingRental != null) {
            // Nếu trạng thái hiện tại KHÔNG phải CANCELED hoặc RETURNED thì không cho đặt
            if (existingRental.getStatus() != CarRentalStatus.CANCELED && existingRental.getStatus() != CarRentalStatus.RETURNED) {
                logger.warn("Đã tồn tại đơn thuê xe đang hoạt động với id: {}", id);
                throw new IllegalStateException("Bạn đã đặt xe này rồi.");
            }
            // Nếu đã huỷ hoặc đã trả xe, tái sử dụng bản ghi hiện có
            logger.info("Khách hàng thuê lại xe sau khi đã huỷ hoặc trả. Cập nhật bản ghi cũ");
            existingRental.setRentDate(LocalDate.parse(pickupDate));
            existingRental.setReturnDate(LocalDate.parse(returnDate));
            existingRental.setStatus(CarRentalStatus.PENDING);
            carRentalRepository.save(existingRental);
            return;
        }

        // Chưa từng thuê: tạo record mới
        CarRental carRental = new CarRental();
        carRental.setId(id);
        carRental.setCustomer(customer);
        carRental.setCar(car);
        carRental.setRentDate(LocalDate.parse(pickupDate));
        carRental.setReturnDate(LocalDate.parse(returnDate));
        carRental.setStatus(CarRentalStatus.PENDING);

        logger.debug("Tạo đối tượng CarRental: {}", carRental);
        carRentalRepository.save(carRental);
        logger.info("Lưu thông tin thuê xe thành công.");
    }

    public List<CarRental> findByCustomer(Customer customer) {
        Customer existingCustomer = customerRepository.findByEmail(customer.getEmail());
        return carRentalRepository.findByCustomer_CustomerId(existingCustomer.getCustomerId());
    }

    public List<CarRental> findAll() {
        logger.info("Lấy tất cả thông tin thuê xe.");
        return carRentalRepository.findAll();
    }

    public void updateStatus(Long customerId, Long carId, String status) {
        Customer existingCustomer = customerRepository.findByCustomerId(customerId);
        Car car = carService.findById(carId);
        CarRental carRental = carRentalRepository.findByCustomerAndCar(existingCustomer, car);

        if (carRental == null) {
            logger.error("Không tìm thấy đơn thuê xe cho khách hàng ID: {}", customerId);
            throw new IllegalArgumentException("Không tìm thấy đơn thuê xe cho khách hàng ID: " + customerId);
        }

        try {
            CarRentalStatus newStatus = CarRentalStatus.valueOf(status.toUpperCase());
            carRental.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            logger.error("Trạng thái không hợp lệ: {}", status);
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + status);
        }

        carRentalRepository.save(carRental);
        logger.info("Cập nhật trạng thái đơn thuê xe thành công: {}", carRental);
    }
}
