package com.EGEA1R.CarService.validation.classes.licenseplate;

import com.EGEA1R.CarService.web.DTO.CarAndUserDTO;

import java.util.regex.Pattern;

public class LicensePlateCarAndUserDTO extends LicensePlateNumberValidator<CarAndUserDTO> {

    @Override
    protected boolean validation(CarAndUserDTO carAndUserDTO) {
        Boolean result = false;
        setValuesIfNull(carAndUserDTO);
        if(carAndUserDTO.getLicensePlateNumber().equals("")){
            result = true;
        } else if(!carAndUserDTO.getLicensePlateNumber().equals("") && carAndUserDTO.getForeignCountryPlate()){
            result = true;
        } else if(!carAndUserDTO.getLicensePlateNumber().equals("") && !carAndUserDTO.getForeignCountryPlate()){
            if((Pattern.matches("^[a-zA-Z]{3}[-][0-9]{3}$", carAndUserDTO.getLicensePlateNumber()) ||
                    Pattern.matches("^[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{2}$", carAndUserDTO.getLicensePlateNumber())||
                    Pattern.matches("^[/p/P][-][0-9]{5}$", carAndUserDTO.getLicensePlateNumber())||
                    Pattern.matches("^[a-zA-z]{3}[0-9]{5}", carAndUserDTO.getLicensePlateNumber())))
            {
                result = true;
            } else{
                result = false;
            }
        }
        return result;
    }

    private void setValuesIfNull(CarAndUserDTO carAndUserDTO){
        Boolean foreignLicensePlate = (carAndUserDTO.getForeignCountryPlate() == null) ? false : carAndUserDTO.getForeignCountryPlate();
        if(foreignLicensePlate.equals(false)){
            carAndUserDTO.setForeignCountryPlate(false);
        }
        String licensePlate = (carAndUserDTO.getLicensePlateNumber() == null) ? "" : carAndUserDTO.getLicensePlateNumber();
        if(licensePlate.equals("")){
            carAndUserDTO.setLicensePlateNumber(licensePlate);
        }
    }
}
