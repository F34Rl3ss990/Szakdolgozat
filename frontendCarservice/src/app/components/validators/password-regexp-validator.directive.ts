import { Directive } from '@angular/core';
import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator, ValidatorFn} from '@angular/forms';
import {matchingPasswordValidator} from './matching-password-validator.directive';

export const passwordPatternValidator:ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  if (!control.value){
    return null;
  }
  let hasNumber = /\d/.test(control.value);
  let hasUpper = /[A-Z]/.test(control.value);
  let hasLower = /[a-z]/.test(control.value);
  let hasSpecial =/[<>{}\"/|;:.,~!?@#$%^=&()¿§«»ω⊙¤°℃℉€¥£¢¡®©_+\\[\\]+$/.test(control.value);

  const valid = hasNumber && hasUpper && hasLower && hasSpecial;
 return !valid ? { strong: true} : null;

};

@Directive({
  selector: '[appPasswordRegexpValidator]',
  providers: [{ provide: NG_VALIDATORS, useExisting: passwordPatternValidator, multi: true }]
})
export class PasswordRegexpValidatorDirective implements Validator{
  validate(control: AbstractControl): ValidationErrors {
    return  passwordPatternValidator(control);
  }

}