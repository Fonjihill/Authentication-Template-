package com.example.backend.service;



import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.ValidationException;
import com.example.backend.model.entity.AppUser;
import com.example.backend.model.form.AppUserForm;
import com.example.backend.model.form.LoginForm;
import com.example.backend.model.form.RegisterForm;
import com.example.backend.model.view.AppUserView;

import java.util.Optional;

public interface AppUserService {

    AppUserView getAppUserInfoById(AppUserForm form) throws ValidationException, ResourceNotFoundException;

    Optional<AppUser> getUserInfo(String token) throws ValidationException, ResourceNotFoundException;

    AppUser registerUser(RegisterForm registerForm) throws ValidationException, ResourceNotFoundException;

    String loginUser(LoginForm loginForm) throws ValidationException, ResourceNotFoundException;

    AppUser updateAppUser(AppUserForm form) throws ValidationException, ResourceNotFoundException;

    AppUser getAppUserById(Long id);
}
