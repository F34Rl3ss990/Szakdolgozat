package com.EGEA1R.CarService.validation.classes.licenseplate;

import com.EGEA1R.CarService.web.DTO.CarDTO;

import java.util.regex.Pattern;

public class LicensePlateCarAdd extends LicensePlateNumberValidator<CarDTO> {

    @Override
    protected boolean validation(CarDTO carDTO) {
        Boolean result = false;
        setValuesIfNull(carDTO);
        if(carDTO.getLicensePlateNumber().equals("")){
            result = true;
        } else if(!carDTO.getLicensePlateNumber().equals("") && carDTO.getForeignCountryPlate()){
            result = true;
        } else if(!carDTO.getLicensePlateNumber().equals("") && !carDTO.getForeignCountryPlate()){
            if((Pattern.matches("^[a-zA-Z]{3}[-][0-9]{3}$", carDTO.getLicensePlateNumber()) ||
                    Pattern.matches("^[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{2}$", carDTO.getLicensePlateNumber())||
                    Pattern.matches("^[/p/P][-][0-9]{5}$", carDTO.getLicensePlateNumber())||
                    Pattern.matches("^[a-zA-z]{3}[0-9]{5}", carDTO.getLicensePlateNumber())))
            {
                result = true;
            } else{
                result = false;
            }
        }
        return result;
    }

    private void setValuesIfNull(CarDTO carDTO){
        Boolean foreignLicensePlate = (carDTO.getForeignCountryPlate() == null) ? false : carDTO.getForeignCountryPlate();
        if(foreignLicensePlate.equals(false)){
            carDTO.setForeignCountryPlate(false);
        }
        String licensePlate = (carDTO.getLicensePlateNumber() == null) ? "" : carDTO.getLicensePlateNumber();
        if(licensePlate.equals("")){
            carDTO.setLicensePlateNumber(licensePlate);
        }
    }
}
