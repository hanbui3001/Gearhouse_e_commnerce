package com.example.demo_ecommerce.repository;

import com.example.demo_ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
