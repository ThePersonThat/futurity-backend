package com.alex.futurity.authorizationserver.repo;

import com.alex.futurity.authorizationserver.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    @Modifying
    @Query("delete from ConfirmationToken t where t.email = ?1")
    void deleteByEmail(String email);
    Optional<ConfirmationToken> findByEmailAndCodeAndConfirmedFalse(String email, String code);
    Optional<ConfirmationToken> findByEmailAndConfirmedTrue(String email);
}
