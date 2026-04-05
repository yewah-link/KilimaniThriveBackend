package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.mappers.UserMapper;
import com.lincoln.kilimaniThrive.models.dtos.PaginatedResponse;
import com.lincoln.kilimaniThrive.models.dtos.UserDTO;
import com.lincoln.kilimaniThrive.models.entity.RoleEntity;
import com.lincoln.kilimaniThrive.models.entity.User;
import com.lincoln.kilimaniThrive.repositories.RoleEntityRepository;
import com.lincoln.kilimaniThrive.repositories.UserRepository;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleEntityRepository roleEntityRepository;

    @Override
    public GenericResponseV2<PaginatedResponse<UserDTO>> getAllUsers(Pageable pageable, String search, String roleName, Boolean active) {
        Specification<User> spec = (root, query, cb) -> cb.conjunction();

        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("email")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("firstName")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("lastName")), "%" + search.toLowerCase() + "%")
            ));
        }

        if (roleName != null && !roleName.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                Join<User, RoleEntity> rolesJoin = root.join("roles");
                return cb.equal(rolesJoin.get("name"), roleName);
            });
        }

        if (active != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), active));
        }

        Page<User> userPage = userRepository.findAll(spec, pageable);

        List<UserDTO> userDtos = userPage.getContent().stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());

        PaginatedResponse<UserDTO> paginatedResponse = PaginatedResponse.<UserDTO>builder()
                .content(userDtos)
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .currentPage(userPage.getNumber())
                .size(userPage.getSize())
                .build();

        return GenericResponseV2.<PaginatedResponse<UserDTO>>builder()
                .status(ResponseStatusEnum.SUCCESS)
                .message("Users retrieved successfully")
                ._embedded(paginatedResponse)
                .build();
    }

    @Override
    @Transactional
    public GenericResponseV2<UserDTO> updateUser(Long id, UserDTO userDTO) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update allowed fields
            if (userDTO.getFirstName() != null) {
                user.setFirstName(userDTO.getFirstName());
            }
            if (userDTO.getLastName() != null) {
                user.setLastName(userDTO.getLastName());
            }
            if (userDTO.getPhoneNumber() != null) {
                user.setPhoneNumber(userDTO.getPhoneNumber());
            }

            // Role Update Logic (Admin only)
            if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("MANAGE_USERS"));

                if (isAdmin) {
                    Set<RoleEntity> newRoles = new HashSet<>();
                    for (String rName : userDTO.getRoles()) {
                        roleEntityRepository.findByName(rName).ifPresent(newRoles::add);
                    }
                    if (!newRoles.isEmpty()) {
                        user.setRoles(newRoles);
                    }
                }
            }

            User updatedUser = userRepository.save(user);
            UserDTO responseDto = userMapper.userToUserDto(updatedUser);

            return GenericResponseV2.<UserDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("User updated successfully")
                    ._embedded(responseDto)
                    .build();

        } catch (Exception e) {
            log.error("Failed to update user", e);
            return GenericResponseV2.<UserDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Failed to update user: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public GenericResponseV2<UserDTO> getUserById(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            return GenericResponseV2.<UserDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("User retrieved successfully")
                    ._embedded(userMapper.userToUserDto(user))
                    .build();
        } catch (Exception e) {
            log.error("User not found", e);
            return GenericResponseV2.<UserDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("User not found")
                    .build();
        }
    }

    @Override
    public GenericResponseV2<UserDTO> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new RuntimeException("User not authenticated");
            }
            
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Current user not found"));
            
            return GenericResponseV2.<UserDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Current user profile retrieved")
                    ._embedded(userMapper.userToUserDto(user))
                    .build();
        } catch (Exception e) {
            log.error("Failed to fetch current user profile", e);
            return GenericResponseV2.<UserDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unauthorized: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public GenericResponseV2<UserDTO> add(UserDTO userDTO) {
        try {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                return GenericResponseV2.<UserDTO>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("User with this email already exists")
                        .build();
            }

            User user = userMapper.userDtoToUser(userDTO);
            User savedUser = userRepository.save(user);
            
            return GenericResponseV2.<UserDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("User added successfully")
                    ._embedded(userMapper.userToUserDto(savedUser))
                    .build();
        } catch (Exception e) {
            log.error("Failed to add user", e);
            return GenericResponseV2.<UserDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to add user")
                    .build();
        }
    }

    @Override
    public GenericResponseV2<Void> delete(Long id) {
        try {
            if (!userRepository.existsById(id)) {
                return GenericResponseV2.<Void>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("User not found")
                        .build();
            }

            userRepository.deleteById(id);
            return GenericResponseV2.<Void>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("User deleted successfully")
                    .build();
        } catch (Exception e) {
            log.error("Failed to delete user", e);
            return GenericResponseV2.<Void>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to delete user")
                    .build();
        }
    }
}
