package com.bug_tracker.app_user;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/appUsers", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUserResource {

    private final AppUserService appUserService;

    public AppUserResource(final AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<List<AppUserDTO>> getAllAppUsers() {
        return ResponseEntity.ok(appUserService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getAppUser(@PathVariable final Long id) {
        return ResponseEntity.ok(appUserService.get(id));
    }



    @GetMapping("/fromToken")
    public ResponseEntity<AppUserDTO> getAppUserByToken(HttpServletRequest request) {
        
        var appUser = (AppUser) request.getAttribute("appUser");
        
        return ResponseEntity.ok(appUserService.mapToDTO(appUser, new AppUserDTO()));
    }

  
    @GetMapping("/stats")
    public ResponseEntity<AppUserStatsDTO> getAppUserStats(HttpServletRequest request) {
        
        var appUser = (AppUser) request.getAttribute("appUser");
        
        return ResponseEntity.ok(appUserService.AppUserStats(  appUser.getId()  ));
    }



    @PostMapping("/register")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAppUser(@RequestBody @Valid final AppUserDTO appUserDTO) {
        return new ResponseEntity<>(appUserService.create(appUserDTO), HttpStatus.CREATED);
    }


    record LoginRequest(String email, String password) {}
    record LoginResponse(String token) {}
    @PostMapping("/login")
    @CrossOrigin(maxAge = 3600)
    @ApiResponse(responseCode = "200")
    public ResponseEntity<LoginResponse> loginAppUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        var login = appUserService.login(loginRequest.email(),loginRequest.password());

        Cookie cookie = new Cookie("refresh_token", login.getRefreshJwt().getToken());
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);

        return ResponseEntity.ok(new LoginResponse(login.getAccessJwt().getToken()));
    }

    @PostMapping("/refresh")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<LoginResponse> loginAppUser(@CookieValue("refresh_token") String refreshToken) {
        var login = appUserService.refreshAccess(refreshToken);

        return ResponseEntity.ok(new LoginResponse(login.getAccessJwt().getToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        Cookie cookie = new Cookie("refresh_token",null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);

        return ResponseEntity.ok("success");
    }


    record ForgotRequest(String email) {}
    @PostMapping("/forgot")
    public ResponseEntity<String> forgot(@RequestBody ForgotRequest forgotRequest, HttpServletRequest request) {
        var originUrl = request.getHeader("Origin");
        appUserService.forget(forgotRequest.email, originUrl);

        return ResponseEntity.ok("success" );
    }

    record ResetRequest(String token, String newPassword) {}
    @PostMapping("/reset")
    public ResponseEntity<String> forgot(@RequestBody ResetRequest resetRequest) {

        appUserService.reset(resetRequest.token, resetRequest.newPassword );

        return ResponseEntity.ok("success" );
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAppUser(@PathVariable final Long id,
            @RequestBody @Valid final AppUserDTO appUserDTO) {
        appUserService.update(id, appUserDTO);
        return ResponseEntity.ok().build(); 
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAppUser(@PathVariable final Long id) {
        appUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
