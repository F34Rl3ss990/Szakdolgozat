package com.EGEA1R.CarService.validation.classes.taxnumber;

import com.EGEA1R.CarService.validation.classes.passwordmatches.PasswordMatchesValidator;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import com.EGEA1R.CarService.web.DTO.payload.request.AddAdminRequest;
import com.EGEA1R.CarService.web.DTO.payload.request.UserAddRequest;

import java.util.regex.Pattern;

public class TaxNumberUserAdd extends TaxNumberValidator<UserAddRequest> {

    protected boolean validation(UserAddRequest userAddRequest, String regexp) {
        Boolean result = false;
        setValuesIfNull(userAddRequest);
        if (!userAddRequest.getBillingToCompany()) {
            result = true;
        } else if (!userAddRequest.getBillingTaxNumber().equals("") && userAddRequest.getBillingEuTax()) {
            result = true;
        } else if (!userAddRequest.getBillingTaxNumber().equals("") && !userAddRequest.getBillingEuTax()) {
            if (Pattern.matches(regexp, userAddRequest.getBillingTaxNumber())) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    private void setValuesIfNull(UserAddRequest userAddRequest) {
        String taxNum = (userAddRequest.getBillingTaxNumber() == null) ? "" : userAddRequest.getBillingTaxNumber();
        if (taxNum.equals("")) {
            userAddRequest.setBillingTaxNumber(taxNum);
        }
        Boolean euTax = (userAddRequest.getBillingEuTax() == null) ? false : userAddRequest.getBillingEuTax();
        if (euTax.equals(false)) {
            userAddRequest.setBillingEuTax(false);
        }
    }
}
