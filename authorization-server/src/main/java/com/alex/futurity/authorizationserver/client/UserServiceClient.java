package com.alex.futurity.authorizationserver.client;

import com.alex.futurity.authorizationserver.domain.LoginDomain;
import com.alex.futurity.authorizationserver.dto.ConfirmEmailRequestDto;
import com.alex.futurity.authorizationserver.dto.LoginRequestDto;
import com.alex.futurity.authorizationserver.dto.SingUpRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "user-service", url = "${user-server}")
public interface UserServiceClient {
    @PostMapping("/login")
    LoginDomain login(@RequestBody LoginRequestDto loginRequestDto);

    @PostMapping(path = "/singup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void singUp(@RequestPart(name = "avatar") MultipartFile avatar,
                @RequestPart(name = "user") SingUpRequestDto request);

    @PostMapping("/exist")
    boolean userExists(@RequestBody ConfirmEmailRequestDto requestDto);
}
