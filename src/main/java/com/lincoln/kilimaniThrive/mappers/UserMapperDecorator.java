package com.lincoln.kilimaniThrive.mappers;

import com.lincoln.kilimaniThrive.models.dtos.UserDTO;
import com.lincoln.kilimaniThrive.models.entity.User;
import com.lincoln.kilimaniThrive.models.entity.RoleEntity;
import com.lincoln.kilimaniThrive.repositories.RoleEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.stream.Collectors;
import java.util.Collections;

public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    @Qualifier("delegate")
    private UserMapper delegate;

    @Autowired
    private RoleEntityRepository roleRepository;

    @Override
    public UserDTO userToUserDto(User user) {
        UserDTO dto = delegate.userToUserDto(user);
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    @Override
    public User userDtoToUser(UserDTO userDto) {
        User user = delegate.userDtoToUser(userDto);
        if (userDto.getRoles() != null) {
            user.setRoles(userDto.getRoles().stream()
                    .map(name -> roleRepository.findByName(name)
                            .orElseGet(() -> roleRepository.save(RoleEntity.builder().name(name).build())))
                    .collect(Collectors.toSet()));
        }
        return user;
    }
}
