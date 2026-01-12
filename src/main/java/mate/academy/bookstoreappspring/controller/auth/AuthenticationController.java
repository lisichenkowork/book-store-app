package mate.academy.bookstoreappspring.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreappspring.dto.user.UserResponseDto;
import mate.academy.bookstoreappspring.exception.RegistrationException;
import mate.academy.bookstoreappspring.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/registration")
    public UserResponseDto register(
            @Valid @RequestBody UserRegistrationRequestDto request) throws RegistrationException {
        return userService.save(request);
    }
}
