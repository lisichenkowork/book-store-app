package mate.academy.bookstoreappspring.dto.user;

import java.util.Set;
import lombok.Data;

@Data
public class UserWithRolesDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String shippingAddress;
    private Set<String> roles;
}
