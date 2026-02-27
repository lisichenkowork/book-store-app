package mate.academy.bookstoreappspring.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mate.academy.bookstoreappspring.dto.user.UserLoginRequestDto;
import mate.academy.bookstoreappspring.dto.user.UserLoginResponseDto;
import mate.academy.bookstoreappspring.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreappspring.dto.user.UserResponseDto;
import mate.academy.bookstoreappspring.exception.RegistrationException;
import mate.academy.bookstoreappspring.service.authentication.AuthenticationService;
import mate.academy.bookstoreappspring.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public UserResponseDto register(
            @Valid @RequestBody UserRegistrationRequestDto request) throws RegistrationException {
        return userService.save(request);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto request) {
        log.info("Trying to login");
        return authenticationService.authenticate(request);
    }
}
