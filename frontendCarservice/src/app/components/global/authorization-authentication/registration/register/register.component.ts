import {Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild, ViewEncapsulation} from '@angular/core';
import {AuthService} from '../../../../../services/auth.service';
import {MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {matchingPasswordValidator} from '../../../../validators/matching-password-validator.directive';
import {MatchingPasswordMatcherDirective} from '../../../../validators/matching-password-matcher.directive';
import {passwordPatternValidator} from '../../../../validators/password-regexp-validator.directive';
import {existingEmailValidator} from '../../../../validators/existing-email-validator.directive';
import {DialogService} from '../../../../../services/dialog.service';
import {ErrorMatcherDirective} from '../../../../validators/error-matcher.directive';
import {DataService} from '../../../../../services/data.service';

@HostListener('document:keydown.meta.k')
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  form: any = {};
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  isSubmitted: boolean;
  registerForm: FormGroup;
  data = false;
  hide = true;
  matcher = new ErrorMatcherDirective();
  CrossFieldErrorMatcher = new MatchingPasswordMatcherDirective();


  @ViewChild('hideIt') hideEm: ElementRef;

  @HostListener('document:keyup', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
      if (event.key === 'Escape') {
      this.close();
    }
  }

submit(){
  this.hideEm.nativeElement.focus();
}


  constructor(private authService: AuthService,
              private dialogRef: MatDialogRef<RegisterComponent>,
              private renderer: Renderer2,
              private fb: FormBuilder,
              private dialogService: DialogService,
              private dataService: DataService) {
    this.createForm();
  }

  createForm() {
    this.registerForm = this.fb.group({
      email: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.pattern(this.dataService.patternEmail), Validators.required],
        asyncValidators: [existingEmailValidator(this.authService)]
      }),
      password: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.required, Validators.minLength(8), passwordPatternValidator]
      }),
      matchingPassword: this.fb.control('', {updateOn: 'blur', validators: [Validators.required, Validators.minLength(8)]}),
      data: this.fb.control(''),
      hide: this.fb.control('')
    }, {validator: matchingPasswordValidator},);
  }

  ngOnInit(): void {
  }


  onSubmit(): void {
    this.submit()
    this.isSubmitted = true;
    this.authService.register(this.registerForm.value).subscribe(
      data => {
        this.isSuccessful = true;
        this.isSignUpFailed = false;
        this.dialogRef.close();
        this.dialogService.openSuccessfulRegisterDialog();
      },
      err => {
        if (!this.registerForm.controls['email'].valid) {
          this.registerForm.controls['email'].setErrors({'pattern': true});
        }
        if (this.registerForm.controls['email'].value === '') {
          this.registerForm.controls['email'].setErrors({'required': true, 'pristine': true});
        }
        if (this.registerForm.controls['password'].value === '') {
          this.registerForm.controls['password'].setErrors({'required': true, 'pristine': true});
        }
        if (this.registerForm.controls['matchingPassword'].value === '') {
          this.registerForm.controls['matchingPassword'].setErrors({'required': true, 'pristine': true});
        }
        this.isSignUpFailed = true;
      }
    );
  }

  close() {
    this.dialogRef.close();
  }
}
