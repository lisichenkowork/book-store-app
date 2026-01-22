package mate.academy.bookstoreappspring.service.user;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreappspring.dto.user.UserResponseDto;
import mate.academy.bookstoreappspring.exception.RegistrationException;
import mate.academy.bookstoreappspring.mapper.UserMapper;
import mate.academy.bookstoreappspring.model.User;
import mate.academy.bookstoreappspring.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto save(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.existsUsersByEmail(userRegistrationRequestDto.getEmail())) {
            throw new RegistrationException("User with this email already exist");
        }
        User entity = userMapper.toEntity(userRegistrationRequestDto);

        User saved = userRepository.save(entity);

        return userMapper.toResponseDto(saved);
    }
}
