package one.superstack.thingstack.controller;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.actor.AuthenticatedController;
import one.superstack.thingstack.model.User;
import one.superstack.thingstack.request.AdminChangeRequest;
import one.superstack.thingstack.request.PasswordChangeRequest;
import one.superstack.thingstack.request.UserAdditionRequest;
import one.superstack.thingstack.response.UserPasswordResponse;
import one.superstack.thingstack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController extends AuthenticatedController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users/me")
    public User get() throws Throwable {
        return userService.get(getActorId());
    }

    @PutMapping(value = "/users/me/password")
    public User changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest) throws Throwable {
        return userService.changePassword(getActorId(), passwordChangeRequest);
    }

    @PostMapping(value = "/users")
    public UserPasswordResponse add(@Valid @RequestBody UserAdditionRequest userAdditionRequest) {
        checkFullAccess();
        return userService.add(userAdditionRequest, getActor());
    }

    @GetMapping(value = "/users/{userId}")
    public User get(@PathVariable String userId) throws Throwable {
        return userService.get(userId, getOrganizationId());
    }

    @PutMapping(value = "/users/{userId}/admin")
    public User changeAdmin(@PathVariable String userId, @Valid @RequestBody AdminChangeRequest adminChangeRequest) throws Throwable {
        checkFullAccess();
        return userService.changeAdmin(userId, adminChangeRequest, getOrganizationId());
    }

    @PutMapping(value = "/users/{userId}/password")
    public UserPasswordResponse resetPassword(@PathVariable String userId) throws Throwable {
        checkFullAccess();
        return userService.resetPassword(userId, getOrganizationId());
    }

    @DeleteMapping(value = "/users/{userId}")
    public User delete(@PathVariable String userId) throws Throwable {
        checkFullAccess();
        return userService.delete(userId, getOrganizationId());
    }

    @GetMapping(value = "/users")
    public List<User> list(Pageable pageable) {
        return userService.list(getOrganizationId(), pageable);
    }

    @GetMapping(value = "/users/search")
    public List<User> search(@RequestParam String query, Pageable pageable) {
        return userService.search(query, getOrganizationId(), pageable);
    }
}
