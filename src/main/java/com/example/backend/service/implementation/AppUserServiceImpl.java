package com.example.backend.service.implementation;


import com.example.backend.dao.AppUserDAO;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.ValidationException;
import com.example.backend.model.entity.AppUser;
import com.example.backend.model.form.AppUserForm;
import com.example.backend.model.form.LoginForm;
import com.example.backend.model.form.RegisterForm;
import com.example.backend.model.validator.FormValidator;
import com.example.backend.model.view.AppUserView;
import com.example.backend.service.AppUserService;
import com.example.backend.utils.FormToEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AppUserServiceImpl implements AppUserService {

    private AppUserDAO appUserDAO;




    @Autowired
    public AppUserServiceImpl(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;

    }


    @Override
    public AppUserView getAppUserInfoById(AppUserForm form) throws ValidationException, ResourceNotFoundException {
        List<String> errorList = FormValidator.validateAppUserReadForm(form);
        if(errorList.size() > 0) {
            throw new ValidationException(FormValidator.getErrorMessages(errorList));
        }

        AppUser appUser;
        if (form.getId() != null) {
            appUser = appUserDAO.getAppUserById(form.getId());
        } else {
            appUser = appUserDAO.getAppUserByEmail(form.getEmail());
        }

        return appUserDAO.getAppUserInfoById(appUser);
    }

    @Override
    public Optional<AppUser> getUserInfo(String token) throws ValidationException, ResourceNotFoundException {

        return appUserDAO.getUserInfo(token);
    }


    @Override
    public AppUser registerUser(RegisterForm registerForm) throws ValidationException, ResourceNotFoundException {

        List<String> errorList = FormValidator.validateRegisterForm(registerForm);
        if(errorList.size() > 0) {
            throw new ValidationException(FormValidator.getErrorMessages(errorList));
        }

        AppUser appUser = FormToEntityConverter.convertRegisterFormToAppUser(registerForm);
        appUserDAO.register(appUser); // This line saves the AppUser object.
        return appUser;
    }

    @Override
    public String loginUser(LoginForm loginForm) throws ValidationException, ResourceNotFoundException {
        List<String> errorList = FormValidator.validateLoginForm(loginForm);
        if(errorList.size() > 0) {
            throw new ValidationException(FormValidator.getErrorMessages(errorList));
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(loginForm.getEmail());
        appUser.setPassword(loginForm.getPassword());

        // Return the JWT token after successful authentication
        return appUserDAO.login(appUser);
    }

    @Override
    public AppUser updateAppUser(AppUserForm form) throws ValidationException, ResourceNotFoundException {
        List<String> errorList = FormValidator.validateAppUserForm(form);
        if(errorList.size() > 0) {
            throw new ValidationException(FormValidator.getErrorMessages(errorList));
        }

        AppUser existingAppUser = appUserDAO.getAppUserById(form.getId());
        if (existingAppUser == null) {
            throw new ResourceNotFoundException("AppUser not found with id : " + form.getId());
        }

        // Update the AppUser and Enterprise details from the form
        FormToEntityConverter.updateAppUserFromForm(form, existingAppUser);
        appUserDAO.updateAppUser(existingAppUser);

        return existingAppUser;
    }



    @Override
    public AppUser getAppUserById(Long id) {
        return appUserDAO.getAppUserById(id);
    }


}
