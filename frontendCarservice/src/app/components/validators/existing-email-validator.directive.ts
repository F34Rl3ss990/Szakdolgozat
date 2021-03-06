import { Directive } from '@angular/core';
import { AsyncValidatorFn, AsyncValidator, NG_ASYNC_VALIDATORS, AbstractControl, ValidationErrors } from '@angular/forms';
import { Observable } from "rxjs/Observable";
import 'rxjs/add/operator/map';
import {AuthService} from '../../services/auth.service';


export function existingEmailValidator(authService: AuthService): AsyncValidatorFn {
  return (control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> => {
    return authService.getUserByEmail(control.value).then(
      existEmail => {
        return (existEmail) ? {"emailExists": true} : null;
      }
    );
  };
}

@Directive({
  selector: '[emailExists][formControlName],[emailExists][formControl],[emailExists][ngModel]',
  providers: [{provide: NG_ASYNC_VALIDATORS, useExisting: ExistingEmailValidatorDirective, multi: true}]
})
export class ExistingEmailValidatorDirective implements AsyncValidator {
  constructor(private authService: AuthService) {  }

  validate(control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> {
    return existingEmailValidator(this.authService)(control);
  }
}
