package mate.academy.bookstoreappspring.mapper;

import mate.academy.bookstoreappspring.config.MapperConfig;
import mate.academy.bookstoreappspring.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreappspring.dto.user.UserResponseDto;
import mate.academy.bookstoreappspring.dto.user.UserWithRolesDto;
import mate.academy.bookstoreappspring.model.Role;
import mate.academy.bookstoreappspring.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRegistrationRequestDto userRegistrationRequestDto);

    UserResponseDto toResponseDto(User user);

    UserWithRolesDto toUserWithRolesDto(User user);

    default String mapRole(Role role) {
        return role.getRoleName().name();
    }
}
