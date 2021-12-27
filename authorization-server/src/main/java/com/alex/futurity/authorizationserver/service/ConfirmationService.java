package com.alex.futurity.authorizationserver.service;

import com.alex.futurity.authorizationserver.dto.ConfirmCodeRequestDTO;
import com.alex.futurity.authorizationserver.dto.ConfirmEmailRequestDTO;

public interface ConfirmationService {
    void confirmEmail(ConfirmEmailRequestDTO dto);
    void confirmCode(ConfirmCodeRequestDTO dto);
    boolean isEmailConfirmed(String email);
}
