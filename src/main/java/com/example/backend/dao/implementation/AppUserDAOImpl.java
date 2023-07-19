package com.example.backend.dao.implementation;


import com.example.backend.dao.AppUserDAO;
import com.example.backend.exception.ApiException;
import com.example.backend.exception.ExceptionType;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.entity.AppUser;
import com.example.backend.model.entity.EntitiesRoleName;
import com.example.backend.model.entity.Role;
import com.example.backend.model.view.AppUserView;
import com.example.backend.repository.AppUserRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.security.JwtTokenProvider;
import com.example.backend.utils.EntityToViewConverter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@AllArgsConstructor
@Component
public class AppUserDAOImpl implements AppUserDAO {

    AppUserRepository appUserRepository;
    AuthenticationManager authenticationManager;
    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;
    JwtTokenProvider jwtTokenProvider;


    private static final Logger logger = LoggerFactory.getLogger(AppUserDAOImpl.class);

    @Override
    public boolean isUserExist(AppUser appUser) {
        Optional<AppUser> appUserEntity = appUserRepository.findById(appUser.getId());

        return appUserEntity.isPresent();
    }

    @Override
    public Optional<AppUser> getUserInfo(String token) {

        String email = jwtTokenProvider.getEmail(token);
        logger.debug(email);
        return Optional.ofNullable(appUserRepository.findByEmail(email));
    }

    @Override
    public AppUser getAppUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    @Override
    public AppUser getAppUserById(Long id) {
        return appUserRepository.findById(id).orElse(null);
    }

    @Override
    public AppUserView getAppUserInfoById(AppUser appUser) throws ResourceNotFoundException {
        return EntityToViewConverter.convertEntityToAppUserView(getAppUser(appUser));
    }

    @Override
    public AppUser getAppUser(AppUser appUser) throws ResourceNotFoundException {

        AppUser appUserEntity = null;

        if(appUser.getId()  != null) {
            appUserEntity = appUserRepository.findById(appUser.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(ExceptionType.ERROR_MSG_USER_PROFILE_NOT_FOUND));
        }else if(appUser.getEmail()!= null) {
            appUserEntity = appUserRepository.findByEmail(appUser.getEmail());

            if(appUserEntity == null){
                throw  new ResourceNotFoundException(ExceptionType.ERROR_MSG_USER_PROFILE_NOT_FOUND);
            }
        }
        return appUserEntity;
    }

    @Override
    public String login(AppUser appUser) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                appUser.getEmail(), appUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String register(AppUser appUser) {

        // Check if the user already exists
        AppUser existingUser = appUserRepository.findByEmail(appUser.getEmail());
        if (existingUser != null) {
            throw new ApiException("User with this email already exists");
        }

        existingUser = appUserRepository.findByPhoneNumber(appUser.getPhoneNumber());
        if (existingUser != null) {
            throw new ApiException( "User with this phone number already exists");
        }

        // Check if a company with this SIRET already exists
/*        Optional<Entreprise> existingCompany = Optional.ofNullable(enterpriseRepository.findBySiret(appUser.getEntreprise().getSiret()));
        if (existingCompany.isPresent()) {
            throw new ApiException( "Company with this SIRET already exists");
        }*/

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        // Assign roles to the user
        Set<Role> roles = new HashSet<>();
        Optional<Role> userRoleOptional = roleRepository.findByName(EntitiesRoleName.ROLE_ADMIN);
        if (userRoleOptional.isEmpty()) {
            throw new ApiException( "Admin role not found");
        }
        roles.add(userRoleOptional.get());

        appUser.setRoles(roles);

        appUserRepository.save(appUser);

        return "User Created successfully";
    }


    @Override
    public AppUser updateAppUser(AppUser updatedAppUser) throws ResourceNotFoundException {
        // Trouver l'utilisateur existant
        AppUser existingAppUser = appUserRepository.findById(updatedAppUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with id : " + updatedAppUser.getId()));

        // Mettre à jour les informations de l'utilisateur
        existingAppUser.setFirstName(updatedAppUser.getFirstName());
        existingAppUser.setLastName(updatedAppUser.getLastName());
        existingAppUser.setEmail(updatedAppUser.getEmail());
        existingAppUser.setPhoneNumber(updatedAppUser.getPhoneNumber());
        // Supposons que le mot de passe est déjà encodé
        existingAppUser.setPassword(updatedAppUser.getPassword());



        // Sauvegarder les modifications pour l'utilisateur
        return appUserRepository.save(existingAppUser);
    }




}
