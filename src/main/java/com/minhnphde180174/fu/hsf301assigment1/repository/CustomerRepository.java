package com.minhnphde180174.fu.hsf301assigment1.repository;

import com.minhnphde180174.fu.hsf301assigment1.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);

    Customer findByCustomerId(Long id);
}
