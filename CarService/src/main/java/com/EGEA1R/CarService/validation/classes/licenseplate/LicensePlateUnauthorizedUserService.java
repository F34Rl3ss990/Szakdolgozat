package com.EGEA1R.CarService.validation.classes.licenseplate;

import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import com.EGEA1R.CarService.web.DTO.payload.request.ModifyUserDateRequest;

import java.util.regex.Pattern;

public class LicensePlateUnauthorizedUserService extends LicensePlateNumberValidator<UnauthorizedUserReservationDTO> {

    @Override
    protected boolean validation(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) {
        Boolean result = false;
        setValuesIfNull(unauthorizedUserReservationDTO);
        if(unauthorizedUserReservationDTO.getLicensePlateNumber().equals("")){
            result = true;
        } else if(!unauthorizedUserReservationDTO.getLicensePlateNumber().equals("") && unauthorizedUserReservationDTO.getForeignCountryPlate()){
            result = true;
        } else if(!unauthorizedUserReservationDTO.getLicensePlateNumber().equals("") && !unauthorizedUserReservationDTO.getForeignCountryPlate()){
            if((Pattern.matches("^[a-zA-Z]{3}[-][0-9]{3}$", unauthorizedUserReservationDTO.getLicensePlateNumber()) ||
                    Pattern.matches("^[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{2}$", unauthorizedUserReservationDTO.getLicensePlateNumber())||
                    Pattern.matches("^[/p/P][-][0-9]{5}$", unauthorizedUserReservationDTO.getLicensePlateNumber())||
                    Pattern.matches("^[a-zA-z]{3}[0-9]{5}", unauthorizedUserReservationDTO.getLicensePlateNumber())))
            {
                result = true;
            } else{
                result = false;
            }
        }
        return result;
    }

    private void setValuesIfNull(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO){
        Boolean foreignLicensePlate = (unauthorizedUserReservationDTO.getForeignCountryPlate() == null) ? false : unauthorizedUserReservationDTO.getForeignCountryPlate();
        if(foreignLicensePlate.equals(false)){
            unauthorizedUserReservationDTO.setForeignCountryPlate(false);
        }
        String licensePlate = (unauthorizedUserReservationDTO.getLicensePlateNumber() == null) ? "" : unauthorizedUserReservationDTO.getLicensePlateNumber();
        if(licensePlate.equals("")){
            unauthorizedUserReservationDTO.setLicensePlateNumber(licensePlate);
        }
    }
}
