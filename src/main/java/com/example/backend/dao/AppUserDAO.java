package com.example.backend.dao;




import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.entity.AppUser;
import com.example.backend.model.view.AppUserView;

import java.util.Optional;

public interface AppUserDAO {

    boolean isUserExist(AppUser appUser);

    Optional<AppUser> getUserInfo(String token);

    AppUser getAppUserByEmail(String email);

    AppUser getAppUserById(Long id);

    AppUserView getAppUserInfoById(AppUser appUser) throws ResourceNotFoundException;

    String login(AppUser AppUser) throws ResourceNotFoundException;

    String register(AppUser appUser);

    AppUser getAppUser(AppUser appUser) throws ResourceNotFoundException;

    AppUser updateAppUser(AppUser appUser) throws ResourceNotFoundException;



}
