package com.minhnphde180174.fu.hsf301assigment1.repository;

import com.minhnphde180174.fu.hsf301assigment1.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
