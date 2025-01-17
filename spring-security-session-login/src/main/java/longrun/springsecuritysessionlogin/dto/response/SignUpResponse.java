package longrun.springsecuritysessionlogin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {
    private int status;
    private String message;
    private String value;

}
