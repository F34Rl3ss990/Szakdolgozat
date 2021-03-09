import {Directive, OnInit} from '@angular/core';
import {ErrorStateMatcher} from '@angular/material/core';
import {FormControl, FormGroupDirective, NgForm} from '@angular/forms';
import {DataService} from '../../services/data.service';

@Directive({
  selector: '[appErrorMatcher]'
})
export class ErrorMatcherDirective implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    let isSubmittedByForm = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmittedByForm) || (control.hasError('required') && control.hasError('pristine')));
  }
}
