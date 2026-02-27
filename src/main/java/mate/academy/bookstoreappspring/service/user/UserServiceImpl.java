package mate.academy.bookstoreappspring.service.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreappspring.dto.user.UserResponseDto;
import mate.academy.bookstoreappspring.exception.EntityNotFoundException;
import mate.academy.bookstoreappspring.exception.RegistrationException;
import mate.academy.bookstoreappspring.mapper.UserMapper;
import mate.academy.bookstoreappspring.model.Role;
import mate.academy.bookstoreappspring.model.User;
import mate.academy.bookstoreappspring.repository.role.RoleRepository;
import mate.academy.bookstoreappspring.repository.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto save(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new RegistrationException("User with this email already exist");
        }

        Role userRole = roleRepository
                .getRoleByRoleName(Role.RoleName.USER);

        User entity = userMapper.toEntity(userRegistrationRequestDto);

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setRoles(Set.of(userRole));

        User saved = userRepository.save(entity);

        return userMapper.toResponseDto(saved);
    }

    @Override
    public User findByUsername(String username) {

        return userRepository.findUserByEmail(username)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not exist with this username: "
                                + username));
    }

    @Override
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.getUserByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User not found with this email: " + email));
    }
}
