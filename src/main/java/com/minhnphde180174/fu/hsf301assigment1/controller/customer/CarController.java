package com.minhnphde180174.fu.hsf301assigment1.controller.customer;

import com.minhnphde180174.fu.hsf301assigment1.config.CustomerSession;
import com.minhnphde180174.fu.hsf301assigment1.controller.BaseController;
import com.minhnphde180174.fu.hsf301assigment1.entity.*;
import com.minhnphde180174.fu.hsf301assigment1.entity.Car;
import com.minhnphde180174.fu.hsf301assigment1.entity.CarStatus;
import com.minhnphde180174.fu.hsf301assigment1.entity.Customer;
import com.minhnphde180174.fu.hsf301assigment1.service.impl.CarRentalService;
import com.minhnphde180174.fu.hsf301assigment1.service.impl.CarService;
import com.minhnphde180174.fu.hsf301assigment1.service.impl.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/customers")
public class CarController extends BaseController {

    private final CarService carService;
    private final CarRentalService carRentalService;
    private final CustomerService customerService;
    private final CustomerSession customerSession;

    @Autowired
    public CarController(CarService carService,
                         CarRentalService carRentalService,
                         CustomerService customerService,
                         CustomerSession customerSession) {
        this.carService = carService;
        this.carRentalService = carRentalService;
        this.customerService = customerService;
        this.customerSession = customerSession;
    }

    /**
     * Hiển thị danh sách xe với các bộ lọc: số chỗ, khoảng giá, ngày/giờ.
     */
    @GetMapping("/cars")
    public String listCars(
            @RequestParam(required = false) String pickupDate,
            @RequestParam(required = false) String pickupTime,
            @RequestParam(required = false) String returnDate,
            @RequestParam(required = false) String returnTime,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) String priceRange,
            Model model) {

        List<Car> cars = carService.findAll();

        // Lọc theo số chỗ
        if (capacity != null) {
            cars = cars.stream()
                    .filter(car -> car.getCapacity() == capacity)
                    .collect(Collectors.toList());
        }

        // Lọc theo khoảng giá
        if (priceRange != null && priceRange.contains("-")) {
            String[] parts = priceRange.split("-");
            try {
                long min = Long.parseLong(parts[0]);
                long max = Long.parseLong(parts[1]);
                cars = cars.stream()
                        .filter(car -> car.getRentPrice() >= min && car.getRentPrice() <= max)
                        .collect(Collectors.toList());
            } catch (NumberFormatException ignored) {
            }
        }

        // Truyền lại giá trị đã lọc để hiển thị trong form
        model.addAttribute("pickupDate", pickupDate);
        model.addAttribute("pickupTime", pickupTime);
        model.addAttribute("returnDate", returnDate);
        model.addAttribute("returnTime", returnTime);
        model.addAttribute("capacity", capacity);
        model.addAttribute("priceRange", priceRange);
        model.addAttribute("cars", cars);

        return "customer/cars";
    }

    /**
     * Xem chi tiết thông tin xe.
     */
    @GetMapping("/cars/{id}")
    public String viewCarDetail(@PathVariable("id") Long id, Model model) {
        Car car = carService.findById(id);
        if (car == null) {
            model.addAttribute("error", "Xe không tồn tại.");
            return "redirect:/api/v1/customers/cars";
        }

        model.addAttribute("car", car);
        return "customer/car-detail";
    }

    /**
     * Đặt xe (gửi form POST).
     */

    @PostMapping("/cars/rental")
    public String rentCar(
            @RequestParam("carId") Long carId,
            @RequestParam("pickupDate") String pickupDate,
            @RequestParam("returnDate") String returnDate,
            RedirectAttributes redirectAttributes) {

        logger.info("Bắt đầu xử lý đặt xe: carId={}, pickupDate={}, returnDate={}", carId, pickupDate, returnDate);

        Customer sessionCustomer = customerSession.getCustomer();

        if (sessionCustomer == null) {
            logger.warn("Không tìm thấy khách hàng trong session.");
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thuê xe.");
            return "redirect:/api/v1/public/login";
        }

        Customer customer = customerService.findById(sessionCustomer.getCustomerId());
        if (customer == null) {
            logger.error("Không tìm thấy thông tin khách hàng trong database với ID={}", sessionCustomer.getCustomerId());
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin khách hàng.");
            return "redirect:/api/v1/public/login";
        }

        Car car = carService.findById(carId);
        if (car == null || car.getStatus() != CarStatus.AVAILABLE) {
            logger.warn("Xe không khả dụng hoặc không tồn tại. carId={}, status={}", carId, (car != null ? car.getStatus() : "null"));
            redirectAttributes.addFlashAttribute("error", "Xe không khả dụng hoặc không tồn tại.");
            return "redirect:/api/v1/customers/cars";
        }

        try {
            carRentalService.rentailCar(customer, car, pickupDate, returnDate);
            logger.info("Đặt xe thành công cho khách hàng ID={}, xe ID={}", customer.getCustomerId(), carId);
            redirectAttributes.addFlashAttribute("success", "Đặt xe thành công!");
        } catch (Exception e) {
            logger.error("Lỗi khi đặt xe: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Đặt xe thất bại: " + e.getMessage());
        }

        return "redirect:/api/v1/customers/cars";
    }




}
