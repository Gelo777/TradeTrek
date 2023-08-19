package proselyteapi.com.tradetrek.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationRequestDto {
    private String email;
    private String password;
}

