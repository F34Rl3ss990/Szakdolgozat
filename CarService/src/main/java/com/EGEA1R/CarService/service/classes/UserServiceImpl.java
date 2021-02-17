package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.exception.BadRequestException;
import com.EGEA1R.CarService.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.BillingInformation;
import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.persistance.repository.CredentialRepository;
import com.EGEA1R.CarService.persistance.repository.UserRepository;
import com.EGEA1R.CarService.service.interfaces.UserService;
import com.EGEA1R.CarService.web.DTO.payload.request.ModifyUserDateRequest;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private CredentialRepository credentialRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository){
        this.credentialRepository = credentialRepository;
    }

    @Override
    public void saveUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO, Long credentialId){
        Credential credential = getCredential(credentialId);
        BillingInformation billingInformation = new BillingInformation();
        setBillingInformation(unauthorizedUserReservationDTO);
        User user = new User();
        addUser(unauthorizedUserReservationDTO, user, billingInformation);
        user.setCredential(credential);
        userRepository.save(user);
    }

    @Override
    public User saveUnauthorizedUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO){
        BillingInformation billingInformation = new BillingInformation();
        setBillingInformation(unauthorizedUserReservationDTO);
        User user = new User();
        addUser(unauthorizedUserReservationDTO, user, billingInformation);
        userRepository.save(user);
        return user;
    }

    @Override
    public void modifyUser(ModifyUserDateRequest modifyUserDateRequest, Long userId, Long credentialId){
        Credential credential = getCredential(credentialId);
        User user = getUser(userId);
        BillingInformation billingInformation = user.getBillingInformation();
        user.setPhoneNumber(modifyUserDateRequest.getPhoneNumber());
        user.setEmail(modifyUserDateRequest.getEmail());
        user.setBillingInformation(modifyBillingInformation(billingInformation, modifyUserDateRequest));
        credential.setEmail(modifyUserDateRequest.getEmail());
        credentialRepository.save(credential);
        userRepository.save(user);
    }

    @Override
    public User getUserDetails(Long credentialId){
        return getUser(credentialId);
    }

    @Override
    public Page<User> getAllUserPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "lastName"));
        Page<User> pageResult = userRepository.findAll(pageRequest);
        List<User> users = pageResult
                .stream()
                .collect(toList());
        return new PageImpl<>(users, pageRequest, pageResult.getTotalElements());
    }

    private Credential getCredential(Long credentialId){
        return credentialRepository
                .findById(credentialId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Profile with this id not found: %s", Long.toString(credentialId))));
    }

    private User getUser(Long userId){
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with this id not found: %s", Long.toString(userId))));
    }

    private BillingInformation setBillingInformation(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO){
        BillingInformation billingInformation = new BillingInformation();
        billingInformation.setName(unauthorizedUserReservationDTO.getName());
        billingInformation.setPhoneNumber(unauthorizedUserReservationDTO.getBillingPhoneNumber());
        billingInformation.setCountry(unauthorizedUserReservationDTO.getCountry());
        billingInformation.setZipCode(unauthorizedUserReservationDTO.getZipCode());
        billingInformation.setTown(unauthorizedUserReservationDTO.getTown());
        billingInformation.setStreet(unauthorizedUserReservationDTO.getStreet());
        billingInformation.setOtherAddressType(unauthorizedUserReservationDTO.getOtherAddressType());
        billingInformation.setEmail(unauthorizedUserReservationDTO.getBillingEmail());
        if(!unauthorizedUserReservationDTO.getTaxNumber().isEmpty() && !unauthorizedUserReservationDTO.getEuTax()){
            if(Pattern.matches("^[0-9]{8}[-][0-9][-][0-9]{2}$", unauthorizedUserReservationDTO.getTaxNumber())){
                billingInformation.setTaxNumber(unauthorizedUserReservationDTO.getTaxNumber());
            }else
                throw new BadRequestException("Tax number is incorrect");
        } else{
            billingInformation.setTaxNumber(unauthorizedUserReservationDTO.getTaxNumber());
        }
        return billingInformation;
    }

    private BillingInformation modifyBillingInformation(BillingInformation billingInform, ModifyUserDateRequest modifyUserDateRequest){
        if(!modifyUserDateRequest.getName().isEmpty()){
            billingInform.setName(modifyUserDateRequest.getName());
        }
        if(!modifyUserDateRequest.getCountry().isEmpty()){
            billingInform.setCountry(modifyUserDateRequest.getCountry());
        }
        if(!modifyUserDateRequest.getZipCode().toString().isEmpty()){
            billingInform.setZipCode(modifyUserDateRequest.getZipCode());
        }
        if(!modifyUserDateRequest.getTown().isEmpty()){
            billingInform.setTown(modifyUserDateRequest.getTown());
        }
        if(!modifyUserDateRequest.getStreet().isEmpty()){
            billingInform.setStreet(modifyUserDateRequest.getStreet());
        }
        if(!modifyUserDateRequest.getOtherAddressType().isEmpty()){
            billingInform.setOtherAddressType(modifyUserDateRequest.getOtherAddressType());
        }
        if(!modifyUserDateRequest.getPhoneNumber().toString().isEmpty()){
            billingInform.setPhoneNumber(modifyUserDateRequest.getPhoneNumber());
        }
        if(!modifyUserDateRequest.getTaxNumber().isEmpty() && !modifyUserDateRequest.getEuTax()){
            if(Pattern.matches("^[0-9]{8}[-][0-9][-][0-9]{2}$", modifyUserDateRequest.getTaxNumber())){
                billingInform.setTaxNumber(modifyUserDateRequest.getTaxNumber());
            }else
                throw new BadRequestException("Tax number is incorrect");
        } else{
            billingInform.setTaxNumber(modifyUserDateRequest.getTaxNumber());
        }
        if(!modifyUserDateRequest.getEmail().isEmpty()){
            billingInform.setEmail(modifyUserDateRequest.getBillingEmail());
        }
        return billingInform;
    }

    private User addUser(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO, User user, BillingInformation billingInformation){
        user.setLastName(user.getLastName());
        user.setFirstName(user.getFirstName());
        user.setPhoneNumber(user.getPhoneNumber());
        user.setEmail(unauthorizedUserReservationDTO.getEmail());
        user.setBillingInformation(billingInformation);
        return user;
    }
       
}
