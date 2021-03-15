package com.EGEA1R.CarService.validation.classes.taxnumber;

import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import com.EGEA1R.CarService.web.DTO.payload.request.UserAddRequest;

import java.util.regex.Pattern;

public class TaxNumberUnauthorizedUserReservation extends TaxNumberValidator<UnauthorizedUserReservationDTO> {

    @Override
    protected boolean validation(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO, String regexp) {
        Boolean result = false;
        setValuesIfNull(unauthorizedUserReservationDTO);
        if(!unauthorizedUserReservationDTO.getBillingToCompany()){
            result = true;
        } else if(!unauthorizedUserReservationDTO.getBillingTaxNumber().equals("") && unauthorizedUserReservationDTO.getBillingEuTax()){
            result = true;
        } else if(!unauthorizedUserReservationDTO.getBillingTaxNumber().equals("") && !unauthorizedUserReservationDTO.getBillingEuTax()){
            if(Pattern.matches(regexp, unauthorizedUserReservationDTO.getBillingTaxNumber()))
            {
                result = true;
            } else{
                result = false;
            }
        }
        return result;
    }

    private void setValuesIfNull(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO){
        String taxNum = (unauthorizedUserReservationDTO.getBillingTaxNumber() == null) ? "" : unauthorizedUserReservationDTO.getBillingTaxNumber();
        if(taxNum.equals("")){
            unauthorizedUserReservationDTO.setBillingTaxNumber(taxNum);
        }
        Boolean euTax = (unauthorizedUserReservationDTO.getBillingEuTax() == null) ? false : unauthorizedUserReservationDTO.getBillingEuTax();
        if(euTax.equals(false)){
            unauthorizedUserReservationDTO.setBillingEuTax(false);
        }
    }

}
