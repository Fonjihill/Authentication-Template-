package com.example.backend.controller;


import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.ValidationException;
import com.example.backend.model.entity.AppUser;
import com.example.backend.model.entity.EntitiesRoleName;
import com.example.backend.model.form.AppUserForm;
import com.example.backend.model.form.LoginForm;
import com.example.backend.model.form.RegisterForm;
import com.example.backend.model.response.JWTLoginResponse;
import com.example.backend.model.view.AppUserView;
import com.example.backend.repository.AppUserRepository;
import com.example.backend.security.JwtTokenProvider;
import com.example.backend.service.AppUserService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Api(value="AppUserController", description="Rest API for App User operations.")
@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping(value = "/v1/user")
public class AppUserController {

    private AppUserService appUserService;

    private AppUserRepository appUserRepository;
    PasswordEncoder passwordEncoder;
    JwtTokenProvider jwtTokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);



    @ApiOperation(value = "Get App user profile by ID.", response = AppUserView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Validation Exception"),
            @ApiResponse(code = 404, message = "Resource Not Found Exception"),
            @ApiResponse(code = 500, message = "Internal Exception")

    })


    @GetMapping
    public Optional<AppUser> getUserInfo(
            final String appUserId,
            Authentication authentication,
            HttpServletRequest request) throws ValidationException, ResourceNotFoundException {

        String jwtToken = request.getHeader("Authorization").substring(7);
        logger.debug(jwtToken);


        AppUserForm form = new AppUserForm();
        boolean useId = false;

        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(EntitiesRoleName.ROLE_ADMIN)
                        || a.getAuthority().equals(EntitiesRoleName.ROLE_ADMIN) )) {
            useId = true;
        }

        if(useId){
            try {
                form.setId(Long.parseLong(appUserId));
            } catch(NumberFormatException e) {
                form.setEmail(appUserId); // If not a long, assume it's an email
            }
        } else {
            try {
                form.setId(Long.parseLong(authentication.getName()));
            } catch(NumberFormatException e) {
                form.setEmail(authentication.getName()); // If not a long, assume it's an email
            }
        }

        return appUserService.getUserInfo(jwtToken);
    }




    @ApiOperation(value = "Get App user profile by ID.", response = AppUserView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Validation Exception"),
            @ApiResponse(code = 404, message = "Resource Not Found Exception"),
            @ApiResponse(code = 500, message = "Internal Exception")

    })

    @GetMapping(value="/{appUserId}")
    public AppUserView getUserInfoById(
            @ApiParam(value = "AppUser ID.", name = "appUserId", required = true)
            @PathVariable("appUserId") final String appUserId,
            Authentication authentication,  HttpServletRequest request) throws ValidationException, ResourceNotFoundException {

        String jwtToken = request.getHeader("Authorization").substring(7);
        logger.debug(jwtToken);

        // Vérifier que l'utilisateur demandé est le même que l'utilisateur authentifié
        AppUserForm form = new AppUserForm();

        boolean useId = false;

        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(EntitiesRoleName.ROLE_ADMIN)
                        || a.getAuthority().equals(EntitiesRoleName.ROLE_ADMIN) )) {
            useId = true;
        }

        if(useId){
            try {
                form.setId(Long.parseLong(appUserId));
            } catch(NumberFormatException e) {
                form.setEmail(appUserId); // If not a long, assume it's an email
            }
        } else {
            try {
                form.setId(Long.parseLong(authentication.getName()));
            } catch(NumberFormatException e) {
                form.setEmail(authentication.getName()); // If not a long, assume it's an email
            }
        }

        return appUserService.getAppUserInfoById(form);
    }

    @ApiOperation(value = "Register User.", response = AppUserView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Validation Exception"),
            @ApiResponse(code = 404, message = "Resource Not Found Exception"),
            @ApiResponse(code = 500, message = "Internal Exception")

    })
    @PostMapping(value = "/register")
    public ResponseEntity<AppUser> registerUser(@Valid @RequestBody RegisterForm form) throws ValidationException, ResourceNotFoundException {
        AppUser appUser = appUserService.registerUser(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
    }


    @ApiOperation(value = "Login User.", response = AppUserView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Validation Exception"),
            @ApiResponse(code = 404, message = "Resource Not Found Exception"),
            @ApiResponse(code = 500, message = "Internal Exception")

    })
    @PostMapping(value = "/login")
    public ResponseEntity<JWTLoginResponse> loginUser(@RequestBody LoginForm form) throws ValidationException, ResourceNotFoundException {
        String token = appUserService.loginUser(form);

        JWTLoginResponse response = new JWTLoginResponse();
        response.setAccessToken(token);

        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Update User.", response = AppUserView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Validation Exception"),
            @ApiResponse(code = 404, message = "Resource Not Found Exception"),
            @ApiResponse(code = 500, message = "Internal Exception")
    })
    @PutMapping(value = "/{appUserId}")
    public ResponseEntity<AppUser> updateUser(@PathVariable("appUserId") final String appUserId,
                                                  @Valid @RequestBody AppUserForm form)
            throws ValidationException, ResourceNotFoundException {
        form.setId(Long.parseLong(appUserId));
        AppUser updatedUser = appUserService.updateAppUser(form);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }







}
