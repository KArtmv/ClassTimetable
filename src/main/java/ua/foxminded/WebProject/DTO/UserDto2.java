package ua.foxminded.WebProject.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.User;

/**
 * DTO for {@link User}
 */

@Getter
@Setter
@Builder
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Group group;
}