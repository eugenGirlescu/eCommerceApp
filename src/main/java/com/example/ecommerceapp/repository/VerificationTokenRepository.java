package com.example.ecommerceapp.repository;

import com.example.ecommerceapp.model.User;
import com.example.ecommerceapp.model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends ListCrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(User user);
}
