import {Directive} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator, ValidatorFn} from '@angular/forms';

export const passwordPatternValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  if (!control.value) {
    return null;
  }
  const hasNumber = /\d/.test(control.value);
  const hasUpper = /[A-Z]/.test(control.value);
  const hasLower = /[a-z]/.test(control.value);
  const hasSpecial = /[<>{}\"/|;:.,~!?@#$%^=&()¿§«»ω⊙¤°℃℉€¥£¢¡®©_+\\[\\]+$/.test(control.value);

  const valid = hasNumber && hasUpper && hasLower && hasSpecial;
  return !valid ? {strong: true} : null;

};

@Directive({
  selector: '[appPasswordRegexpValidator]',
  providers: [{provide: NG_VALIDATORS, useExisting: PasswordRegexpValidatorDirective, multi: true}]
})
export class PasswordRegexpValidatorDirective implements Validator {
  validate(control: AbstractControl): ValidationErrors {
    return passwordPatternValidator(control);
  }

}
