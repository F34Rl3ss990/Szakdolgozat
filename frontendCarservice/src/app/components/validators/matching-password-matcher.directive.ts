import {Directive} from '@angular/core';
import {ErrorStateMatcher} from '@angular/material/core';
import {FormControl, FormGroupDirective, NgForm} from '@angular/forms';

@Directive({
  selector: '[appMatchingPasswordMatcher]'
})
export class MatchingPasswordMatcherDirective implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    let bool = true;
    if (control.parent.errors != null) {
      bool = true;
    } else {
      bool = false;
    }
    return control.dirty && bool;
  }

}
