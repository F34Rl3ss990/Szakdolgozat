import {Directive} from '@angular/core';
import {AsyncValidatorFn, AsyncValidator, NG_ASYNC_VALIDATORS, AbstractControl, ValidationErrors} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {AuthService} from '../../services/auth.service';


export function notExistingEmailValidator(authService: AuthService): AsyncValidatorFn {
  return (control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> => {
    return authService.getUserByEmail(control.value).then(
      existEmail => {
        return (!existEmail) ? {emailNotExists: true} : null;
      }
    );
  };
}

@Directive({
  selector: '[emailNotExists][formControlName],[emailNotExists][formControl],[emailNotExists][ngModel]',
  providers: [{provide: NG_ASYNC_VALIDATORS, useExisting: EmailNotExistingValidatorDirective, multi: true}]
})
export class EmailNotExistingValidatorDirective implements AsyncValidator {
  constructor(private authService: AuthService) {
  }

  validate(control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> {
    return notExistingEmailValidator(this.authService)(control);
  }
}
