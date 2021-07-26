package com.EGEA1R.CarService.validation.classes.taxnumber;

import com.EGEA1R.CarService.web.DTO.CarAndUserDTO;

import java.util.regex.Pattern;

public class TaxNumberCarAndUserDTO extends TaxNumberValidator<CarAndUserDTO> {

    protected boolean validation(CarAndUserDTO carAndUserDTO, String regexp) {
        Boolean result = false;
        setValuesIfNull(carAndUserDTO);
        if (!carAndUserDTO.getBillingToCompany()) {
            result = true;
        } else if (!carAndUserDTO.getBillingTaxNumber().equals("") && carAndUserDTO.getBillingEuTax()) {
            result = true;
        } else if (!carAndUserDTO.getBillingTaxNumber().equals("") && !carAndUserDTO.getBillingEuTax()) {
            if (Pattern.matches(regexp, carAndUserDTO.getBillingTaxNumber())) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    private void setValuesIfNull(CarAndUserDTO carAndUserDTO) {
        String taxNum = (carAndUserDTO.getBillingTaxNumber() == null) ? "" : carAndUserDTO.getBillingTaxNumber();
        if (taxNum.equals("")) {
            carAndUserDTO.setBillingTaxNumber(taxNum);
        }
        Boolean euTax = (carAndUserDTO.getBillingEuTax() == null) ? false : carAndUserDTO.getBillingEuTax();
        if (euTax.equals(false)) {
            carAndUserDTO.setBillingEuTax(false);
        }
    }
}
