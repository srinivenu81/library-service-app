package com.tech.springbootlibraryservice.dao;

import com.tech.springbootlibraryservice.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout,Long> {

Checkout findByUserEmailAndBookId(String userEmail,long bookId);

List<Checkout> findBooksByUserEmail(String userEmail);
    
}
