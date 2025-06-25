package com.minhnphde180174.fu.hsf301assigment1.controller.customer;

import com.minhnphde180174.fu.hsf301assigment1.config.CustomerSession;
import com.minhnphde180174.fu.hsf301assigment1.entity.CarRental;
import com.minhnphde180174.fu.hsf301assigment1.entity.CarRentalStatus;
import com.minhnphde180174.fu.hsf301assigment1.entity.Customer;
import com.minhnphde180174.fu.hsf301assigment1.service.impl.CarRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class BookingHistoryController {

    private final CarRentalService carRentalService;
    private final CustomerSession customerSession;

    @GetMapping("/bookings")
    public String viewBookingHistory(Model model) {
        Customer customer = customerSession.getCustomer();
        if (customer == null) {
            return "redirect:/api/v1/public/login";
        }

        List<CarRental> bookings = carRentalService.findByCustomer(customer);

        long total = bookings.size();
        long pending = bookings.stream().filter(b -> b.getStatus() == CarRentalStatus.PENDING).count();
        long active = bookings.stream().filter(b -> b.getStatus() == CarRentalStatus.BOOKED || b.getStatus() == CarRentalStatus.PICKED_UP).count();
        long completed = bookings.stream().filter(b -> b.getStatus() == CarRentalStatus.RETURNED).count();
        long canceled = bookings.stream().filter(b -> b.getStatus() == CarRentalStatus.CANCELED).count();

        // Prepare filtered lists for UI tabs (avoid Thymeleaf lambda)
        List<CarRental> pendingList = bookings.stream().filter(b -> b.getStatus() == CarRentalStatus.PENDING).toList();
        List<CarRental> activeList = bookings.stream().filter(b -> b.getStatus() == CarRentalStatus.BOOKED || b.getStatus() == CarRentalStatus.PICKED_UP).toList();
        List<CarRental> completedList = bookings.stream().filter(b -> b.getStatus() == CarRentalStatus.RETURNED).toList();
        List<CarRental> canceledList = bookings.stream().filter(b -> b.getStatus() == CarRentalStatus.CANCELED).toList();

        model.addAttribute("bookings", bookings);
        model.addAttribute("totalBookings", total);
        model.addAttribute("pendingBookings", pending);
        model.addAttribute("activeBookings", active);
        model.addAttribute("completedBookings", completed);
        model.addAttribute("cancelledBookings", canceled);

        model.addAttribute("pendingList", pendingList);
        model.addAttribute("activeList", activeList);
        model.addAttribute("completedList", completedList);
        model.addAttribute("canceledList", canceledList);

        return "customer/bookings";
    }
}
