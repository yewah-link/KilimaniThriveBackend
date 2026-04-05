package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.models.dtos.PaginatedResponse;
import com.lincoln.kilimaniThrive.models.dtos.UserDTO;
import org.springframework.data.domain.Pageable;

public interface UserService {
    GenericResponseV2<UserDTO> add(UserDTO userDTO);
    GenericResponseV2<Void> delete(Long id);
    GenericResponseV2<UserDTO> updateUser(Long id, UserDTO userDTO);
    GenericResponseV2<UserDTO> getUserById(Long id);
    GenericResponseV2<UserDTO> getCurrentUser();
    GenericResponseV2<PaginatedResponse<UserDTO>> getAllUsers(Pageable pageable, String search, String roleName, Boolean active);
}
