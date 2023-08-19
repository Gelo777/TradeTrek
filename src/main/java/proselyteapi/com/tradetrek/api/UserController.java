package proselyteapi.com.tradetrek.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proselyteapi.com.tradetrek.model.dto.UserDto;
import proselyteapi.com.tradetrek.security.CustomPrincipal;
import proselyteapi.com.tradetrek.security.SecurityService;
import proselyteapi.com.tradetrek.security.TokenDetails;
import proselyteapi.com.tradetrek.service.UserService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final SecurityService securityService;
    private final UserService userService;


    @PostMapping("/register")
    public Mono<String> register(@RequestBody UserDto dto) {
        return userService.registerUser(dto);
    }

    @PostMapping("/login")
    public Mono<TokenDetails> login(@RequestBody UserDto dto) {
        return securityService.authenticate(dto.getUsername(), dto.getPassword());

    }

    @GetMapping("/api-key")
    public Mono<String> getUserInfo(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getApiKey(customPrincipal.getId());

    }
}


