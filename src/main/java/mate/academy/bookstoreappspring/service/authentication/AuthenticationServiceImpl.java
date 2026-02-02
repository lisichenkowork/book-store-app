package mate.academy.bookstoreappspring.service.authentication;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.user.UserLoginRequestDto;
import mate.academy.bookstoreappspring.dto.user.UserLoginResponseDto;
import mate.academy.bookstoreappspring.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto) {

        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        userLoginRequestDto.email(),
                        userLoginRequestDto.password()
                ));

        String token = jwtUtil.generateToken(authenticate.getName());

        return new UserLoginResponseDto(token);
    }
}
