package mate.academy.bookstoreappspring.service.authentication;

import mate.academy.bookstoreappspring.dto.user.UserLoginRequestDto;
import mate.academy.bookstoreappspring.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto);
}
