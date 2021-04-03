package com.EGEA1R.CarService.validation.classes.taxnumber;

import com.EGEA1R.CarService.web.DTO.payload.request.ModifyUserDateRequest;

import java.util.regex.Pattern;

public class TaxNumberUserModify extends TaxNumberValidator<ModifyUserDateRequest> {

    protected boolean validation(ModifyUserDateRequest modifyUserDateRequest, String regexp) {
        Boolean result = false;
        setValuesIfNull(modifyUserDateRequest);
        if (!modifyUserDateRequest.getBillingToCompany()) {
            result = true;
        } else if (!modifyUserDateRequest.getBillingTaxNumber().equals("") && modifyUserDateRequest.getBillingEuTax()) {
            result = true;
        } else if (!modifyUserDateRequest.getBillingTaxNumber().equals("") && !modifyUserDateRequest.getBillingEuTax()) {
            if (Pattern.matches(regexp, modifyUserDateRequest.getBillingTaxNumber())) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    private void setValuesIfNull(ModifyUserDateRequest modifyUserDateRequest) {
        String taxNum = (modifyUserDateRequest.getBillingTaxNumber() == null) ? "" : modifyUserDateRequest.getBillingTaxNumber();
        if (taxNum.equals("")) {
            modifyUserDateRequest.setBillingTaxNumber(taxNum);
        }
        Boolean euTax = (modifyUserDateRequest.getBillingEuTax() == null) ? false : modifyUserDateRequest.getBillingEuTax();
        if (euTax.equals(false)) {
            modifyUserDateRequest.setBillingEuTax(false);
        }
    }
}
