package com.minhnphde180174.fu.hsf301assigment1.repository;

import com.minhnphde180174.fu.hsf301assigment1.entity.CarCustomerID;
import com.minhnphde180174.fu.hsf301assigment1.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, CarCustomerID> {
}
