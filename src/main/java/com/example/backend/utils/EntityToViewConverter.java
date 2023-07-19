package com.example.backend.utils;


import com.example.backend.model.entity.AppUser;
import com.example.backend.model.view.AppUserView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EntityToViewConverter {



    private static final Logger logger = LoggerFactory.getLogger(EntityToViewConverter.class);


    public static AppUserView convertEntityToAppUserView(AppUser savedAppUser) {

        AppUserView view = new AppUserView();

        view.setId(savedAppUser.getId());
        view.setEmail(savedAppUser.getEmail());
        view.setFirstName(savedAppUser.getFirstName());
        view.setLastName(savedAppUser.getLastName());
        view.setPhoneNumber(savedAppUser.getPhoneNumber());

        return view;
    }
}
