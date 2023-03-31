package one.superstack.thingstack.controller;


import jakarta.validation.Valid;
import one.superstack.thingstack.model.User;
import one.superstack.thingstack.request.AuthenticationRequest;
import one.superstack.thingstack.request.SignUpRequest;
import one.superstack.thingstack.response.AuthenticationResponse;
import one.superstack.thingstack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class AccountController {

    private final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/accounts/signup")
    public User signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.signUp(signUpRequest);
    }

    @PostMapping(value = "/accounts/authenticate")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) throws Throwable {
        return userService.authenticate(authenticationRequest);
    }
}
