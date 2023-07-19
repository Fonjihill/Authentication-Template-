package com.example.backend.utils;


import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.entity.AppUser;
import com.example.backend.model.form.AppUserForm;
import com.example.backend.model.form.LoginForm;
import com.example.backend.model.form.RegisterForm;
import org.springframework.stereotype.Component;


@Component
public class FormToEntityConverter {



    public static AppUser convertFormToAppUser(AppUserForm appUserForm) {

        AppUser appUser  = new AppUser();

        appUser.setId(appUserForm.getId());

        if(appUserForm.getFirstName() != null){
            appUser.setFirstName(appUserForm.getFirstName());
        }

        if(appUserForm.getLastName() != null){
            appUser.setLastName(appUserForm.getLastName());
        }

        if(appUserForm.getEmail() != null){
            appUser.setEmail(appUserForm.getEmail());
        }

        if(appUserForm.getPhoneNumber() != null){
            appUser.setPhoneNumber(appUserForm.getPhoneNumber());
        }


        return appUser;

    }

    public static AppUser convertRegisterFormToAppUser(RegisterForm registerForm) {
        AppUser appUser = new AppUser();

        // Assuming that RegisterForm has these methods...
        appUser.setFirstName(registerForm.getFirstName());
        appUser.setLastName(registerForm.getLastName());
        appUser.setEmail(registerForm.getEmail());
        appUser.setPhoneNumber(registerForm.getPhoneNumber());
        appUser.setPassword(registerForm.getPassword());

        return appUser;
    }




    public static AppUser convertLoginFormToAppUser(LoginForm loginForm) {
        AppUser appUser = new AppUser();

        appUser.setEmail(loginForm.getEmail());
        appUser.setPassword(loginForm.getPassword());

        return appUser;
    }





    public static AppUser updateAppUserFromForm(AppUserForm form, AppUser existingAppUser) throws ResourceNotFoundException {

        if(form.getFirstName() != null) {
            existingAppUser.setFirstName(form.getFirstName());
        }

        if(form.getLastName()!= null) {
            existingAppUser.setLastName(form.getLastName());
        }

        if(form.getEmail() != null) {
            existingAppUser.setEmail(form.getEmail());
        }

        if(form.getPhoneNumber() != null) {
            existingAppUser.setPhoneNumber(form.getPhoneNumber());
        }




        // ...

        return existingAppUser;
    }

}
