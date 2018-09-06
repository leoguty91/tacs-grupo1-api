package ar.com.tacsutn.grupo1.eventapp.web;

import ar.com.tacsutn.grupo1.eventapp.domain.Role;
import ar.com.tacsutn.grupo1.eventapp.domain.User;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "Users", description = "user resources")
public class UserController {

    /**
     * Create a new user account.
     * @return the created user.
     */
    @PostMapping("/users")
    public User createUser(/* @RequestBody User user */) {
        return new User(1L, "sample user 1", "hidden password", Role.NORMAL);
    }

    /**
     * Returns a single user by identifier.
     * The administrator account is required.
     *
     * @param userId requested user's identifier.
     * @return the requested user.
     */
    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId) {
        return new User(userId, "sample user", "hidden password", Role.NORMAL);
    }
}
