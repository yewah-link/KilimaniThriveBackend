package com.lincoln.kilimaniThrive.mappers;

import com.lincoln.kilimaniThrive.models.dtos.UserDTO;
import com.lincoln.kilimaniThrive.models.entity.RoleEntity;
import com.lincoln.kilimaniThrive.models.entity.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    UserDTO userToUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User userDtoToUser(UserDTO userDto);

    default List<String> mapRoles(Set<RoleEntity> roles) {
        if (roles == null) return List.of();
        return roles.stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toList());
    }
}
