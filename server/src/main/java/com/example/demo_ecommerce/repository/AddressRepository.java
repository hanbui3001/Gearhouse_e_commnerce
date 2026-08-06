package com.example.demo_ecommerce.repository;

import com.example.demo_ecommerce.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    boolean existsByUser_Id(String userId);

    Optional<Address> findByIdAndUser_Id(String addressId, String userId);

    Page<Address> findAllByUser_Id(String userId, Pageable pageable);

    Optional<Address> findByUser_IdAndIsDefaultTrue(String userId);

    Optional<Address> findFirstByUser_IdAndIsDefaultFalseOrderByCreatedAtAsc(String userId);
}
