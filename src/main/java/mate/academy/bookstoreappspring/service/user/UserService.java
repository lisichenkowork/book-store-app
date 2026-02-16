package mate.academy.bookstoreappspring.service.user;

import mate.academy.bookstoreappspring.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreappspring.dto.user.UserResponseDto;
import mate.academy.bookstoreappspring.model.User;

public interface UserService {

    UserResponseDto save(UserRegistrationRequestDto userRegistrationRequestDto);

    User findByUsername(String username);

    User getLoggedUser();

}
