package mate.academy.bookstoreappspring.service;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.model.User;
import mate.academy.bookstoreappspring.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "Can`t find user with this username: " + username));

        String[] roles = user.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .toArray(String[]::new);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(roles)
                .build();

    }
}
