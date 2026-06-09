package mate.academy.bookstoreappspring.controller;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.user.UserWithRolesDto;
import mate.academy.bookstoreappspring.mapper.UserMapper;
import mate.academy.bookstoreappspring.model.User;
import mate.academy.bookstoreappspring.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public UserWithRolesDto getCurrentUser() {
        User loggedUser = userService.getLoggedUser();
        return userMapper.toUserWithRolesDto(loggedUser);
    }
}
