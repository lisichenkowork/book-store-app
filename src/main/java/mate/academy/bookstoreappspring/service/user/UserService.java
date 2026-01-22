package mate.academy.bookstoreappspring.service.user;

import mate.academy.bookstoreappspring.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreappspring.dto.user.UserResponseDto;

public interface UserService {

    UserResponseDto save(UserRegistrationRequestDto userRegistrationRequestDto);
}
