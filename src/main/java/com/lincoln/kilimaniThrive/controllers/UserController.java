package com.lincoln.kilimaniThrive.controllers;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.models.dtos.PaginatedResponse;
import com.lincoln.kilimaniThrive.models.dtos.UserDTO;
import com.lincoln.kilimaniThrive.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<GenericResponseV2<UserDTO>> addUser(@RequestBody UserDTO userDTO) {
        GenericResponseV2<UserDTO> response = userService.add(userDTO);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponseV2<Void>> deleteUser(@PathVariable Long id) {
        GenericResponseV2<Void> response = userService.delete(id);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponseV2<UserDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        GenericResponseV2<UserDTO> response = userService.updateUser(id, userDTO);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<GenericResponseV2<PaginatedResponse<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Boolean active) {
        
        Pageable pageable = PageRequest.of(page, size);
        GenericResponseV2<PaginatedResponse<UserDTO>> response = userService.getAllUsers(pageable, search, roleName, active);
        
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponseV2<UserDTO>> getUser(@PathVariable Long id) {
        GenericResponseV2<UserDTO> response = userService.getUserById(id);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<GenericResponseV2<UserDTO>> getCurrentUser() {
        GenericResponseV2<UserDTO> response = userService.getCurrentUser();
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }
}
