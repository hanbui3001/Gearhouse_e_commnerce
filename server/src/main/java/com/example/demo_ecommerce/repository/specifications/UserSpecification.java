package com.example.demo_ecommerce.repository.specifications;

import com.example.demo_ecommerce.model.User;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.PredicateSpecification;

public class UserSpecification {
    public static PredicateSpecification<User> hasEmail(String email) {
        return ((from, criteriaBuilder) -> {
            if(StringUtils.isBlank(email)){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(from.get("email"), email);
        });
    }
    public static PredicateSpecification<User> hasFullName(String fullName) {
        return ((from, criteriaBuilder) -> {
            if(StringUtils.isBlank(fullName)){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(from.get("fullName"), "%" + fullName + "%");
        });
    }
    public static PredicateSpecification<User> hasPhoneNumber(String phoneNumber) {
        return ((from, criteriaBuilder) ->  {
            if(StringUtils.isBlank(phoneNumber)){
                return  criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(from.get("phoneNumber"), phoneNumber);
        });
    }
}
