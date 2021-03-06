import { Directive } from '@angular/core';
import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator, ValidatorFn} from '@angular/forms';


export const matchingPasswordValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const password = control.get('password');
  const matchingPassword = control.get('matchingPassword');
  return password && matchingPassword && password.value != matchingPassword.value ? { passwordDoNotMatch: true } : null;
};
@Directive({
  selector: '[appMatchingPasswordValidator]',
  providers: [{ provide: NG_VALIDATORS, useExisting: MatchingPasswordValidatorDirective, multi: true }]
})
export class MatchingPasswordValidatorDirective implements Validator {
  validate(control: AbstractControl): ValidationErrors {
    return  matchingPasswordValidator(control);
  }
}