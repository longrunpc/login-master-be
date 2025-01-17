package longrun.springsecuritysessionlogin.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import longrun.springsecuritysessionlogin.domain.User;
import longrun.springsecuritysessionlogin.dto.request.SignUpRequest;
import longrun.springsecuritysessionlogin.dto.request.ForgotIdRequest;
import longrun.springsecuritysessionlogin.dto.response.SignUpResponse;
import longrun.springsecuritysessionlogin.service.RecoveryService;
import longrun.springsecuritysessionlogin.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RecoveryService recoveryService;

    public static PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        User user =userService.signUp(request);
        SignUpResponse response = new SignUpResponse(HttpStatus.OK.value(),"SignUp success", user.getUserId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-id")
    public ResponseEntity<String> createIdRecoveryCode(@RequestBody ForgotIdRequest request){
        userService.findByEmail(request.getEmail());
        recoveryService.saveVerificationCode(request);
        return ResponseEntity.ok("create recoveryCode");
    }

    @GetMapping("/find-id-verify")
    public ResponseEntity<String> sendRecoveryCode(@RequestParam("email") String email, @RequestParam("verificationCode") String verificationCode){

        return ResponseEntity.ok(recoveryService.validateVerificationCode(email,verificationCode));

    }
}
