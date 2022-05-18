package com.alex.futurity.userserver.controller;

import com.alex.futurity.userserver.entity.User;
import com.alex.futurity.userserver.repo.UserRepository;
import com.alex.futurity.userserver.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@AllArgsConstructor
@Log4j2
public class UserController {
    private final UserService service;

    @GetMapping(value = "/{id}/avatar", produces = {
            MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE
    })
    public ResponseEntity<Resource> getAvatar(@PathVariable long id) {
        return ResponseEntity.ok(service.findUserAvatar(id));
    }

    @GetMapping(value = "/{id}/id")
    public ResponseEntity<?> getId(@PathVariable long id) {
        HashMap<String, Object> answer = new HashMap<>();
        answer.put("userId", id);
        User user = service.findById(id);
        answer.put("username", user.getNickname());
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }
}
