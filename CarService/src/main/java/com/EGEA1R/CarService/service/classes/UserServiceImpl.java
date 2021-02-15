package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.exception.BadRequestException;
import com.EGEA1R.CarService.exception.ResourceNotFoundException;
import com.EGEA1R.CarService.persistance.entity.BillingInformation;
import com.EGEA1R.CarService.persistance.entity.Car;
import com.EGEA1R.CarService.persistance.entity.Credential;
import com.EGEA1R.CarService.persistance.entity.User;
import com.EGEA1R.CarService.persistance.repository.CredentialRepository;
import com.EGEA1R.CarService.persistance.repository.UserRepository;
import com.EGEA1R.CarService.service.interfaces.UserService;
import com.EGEA1R.CarService.web.DTO.ModifyUserDataDTO;
import com.EGEA1R.CarService.web.DTO.UserAndCarAndServiceDTO;
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

    public void saveUser(UserAndCarAndServiceDTO userAndCarAndServiceDTO, Long credentialId){
        Credential credential = getCredential(credentialId);
        BillingInformation billingInformation = new BillingInformation();
        setBillingInformation(userAndCarAndServiceDTO);
        User user = new User();
        addUser(userAndCarAndServiceDTO, user, billingInformation);
        user.setCredential(credential);
        userRepository.save(user);
    }

    public void saveUnauthorizedUser(UserAndCarAndServiceDTO userAndCarAndServiceDTO){
        BillingInformation billingInformation = new BillingInformation();
        setBillingInformation(userAndCarAndServiceDTO);
        User user = new User();
        addUser(userAndCarAndServiceDTO, user, billingInformation);
        userRepository.save(user);
    }


    public void modifyUser(ModifyUserDataDTO modifyUserDataDTO, Long userId, Long credentialId){
        Credential credential = getCredential(credentialId);
        User user = getUser(userId);
        BillingInformation billingInformation = user.getBillingInformation();
        user.setPhoneNumber(modifyUserDataDTO.getPhoneNumber());
        user.setEmail(modifyUserDataDTO.getEmail());
        user.setBillingInformation(modifyBillingInformation(billingInformation, modifyUserDataDTO));
        credential.setEmail(modifyUserDataDTO.getEmail());
        credentialRepository.save(credential);
        userRepository.save(user);
    }

    public User getUserDetails(Long credentialId){
        return getUser(credentialId);
    }

    public Page<User> getAllUserPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "lastName"));
        Page<User> pageResult = userRepository.findAll(pageRequest);
        List<User> users = pageResult
                .stream()
                .collect(toList());
        return new PageImpl<>(users, pageRequest, pageResult.getTotalElements());
    }

    public void getUsersAndCarWithServicesToday(){

    }

    public void getUsersAndCarWithServicesInTheFuture(){}

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

    private BillingInformation setBillingInformation(UserAndCarAndServiceDTO userAndCarAndServiceDTO){
        BillingInformation billingInformation = new BillingInformation();
        billingInformation.setName(userAndCarAndServiceDTO.getName());
        billingInformation.setPhoneNumber(userAndCarAndServiceDTO.getBillingPhoneNumber());
        billingInformation.setCountry(userAndCarAndServiceDTO.getCountry());
        billingInformation.setZipCode(userAndCarAndServiceDTO.getZipCode());
        billingInformation.setTown(userAndCarAndServiceDTO.getTown());
        billingInformation.setStreet(userAndCarAndServiceDTO.getStreet());
        billingInformation.setOtherAddressType(userAndCarAndServiceDTO.getOtherAddressType());
        billingInformation.setEmail(userAndCarAndServiceDTO.getBillingEmail());
        if(!userAndCarAndServiceDTO.getTaxNumber().isEmpty() && !userAndCarAndServiceDTO.getEuTax()){
            if(Pattern.matches("^[0-9]{8}[-][0-9][-][0-9]{2}$", userAndCarAndServiceDTO.getTaxNumber())){
                billingInformation.setTaxNumber(userAndCarAndServiceDTO.getTaxNumber());
            }else
                throw new BadRequestException("Tax number is incorrect");
        } else{
            billingInformation.setTaxNumber(userAndCarAndServiceDTO.getTaxNumber());
        }
        return billingInformation;
    }

    private BillingInformation modifyBillingInformation(BillingInformation billingInform, ModifyUserDataDTO modifyUserDataDTO){
        if(!modifyUserDataDTO.getName().isEmpty()){
            billingInform.setName(modifyUserDataDTO.getName());
        }
        if(!modifyUserDataDTO.getCountry().isEmpty()){
            billingInform.setCountry(modifyUserDataDTO.getCountry());
        }
        if(!modifyUserDataDTO.getZipCode().toString().isEmpty()){
            billingInform.setZipCode(modifyUserDataDTO.getZipCode());
        }
        if(!modifyUserDataDTO.getTown().isEmpty()){
            billingInform.setTown(modifyUserDataDTO.getTown());
        }
        if(!modifyUserDataDTO.getStreet().isEmpty()){
            billingInform.setStreet(modifyUserDataDTO.getStreet());
        }
        if(!modifyUserDataDTO.getOtherAddressType().isEmpty()){
            billingInform.setOtherAddressType(modifyUserDataDTO.getOtherAddressType());
        }
        if(!modifyUserDataDTO.getPhoneNumber().toString().isEmpty()){
            billingInform.setPhoneNumber(modifyUserDataDTO.getPhoneNumber());
        }
        if(!modifyUserDataDTO.getTaxNumber().isEmpty() && !modifyUserDataDTO.getEuTax()){
            if(Pattern.matches("^[0-9]{8}[-][0-9][-][0-9]{2}$", modifyUserDataDTO.getTaxNumber())){
                billingInform.setTaxNumber(modifyUserDataDTO.getTaxNumber());
            }else
                throw new BadRequestException("Tax number is incorrect");
        } else{
            billingInform.setTaxNumber(modifyUserDataDTO.getTaxNumber());
        }
        if(!modifyUserDataDTO.getEmail().isEmpty()){
            billingInform.setEmail(modifyUserDataDTO.getBillingEmail());
        }
        return billingInform;
    }

    private User addUser(UserAndCarAndServiceDTO userAndCarAndServiceDTO, User user, BillingInformation billingInformation){
        user.setLastName(user.getLastName());
        user.setFirstName(user.getFirstName());
        user.setPhoneNumber(user.getPhoneNumber());
        user.setEmail(userAndCarAndServiceDTO.getEmail());
        user.setBillingInformation(billingInformation);
        return user;
    }
       
}
