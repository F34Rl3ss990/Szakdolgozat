import {AfterViewInit, Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ErrorMatcherDirective} from '../../../../validators/error-matcher.directive';
import {TokenStorageService} from '../../../../../services/token-storage.service';
import {DataService} from '../../../../../services/data.service';
import {AuthService} from '../../../../../services/auth.service';
import {ProfileService} from '../../../../../services/profile.service';
import {DialogService} from '../../../../../services/dialog.service';
import {passwordPatternValidator} from '../../../../validators/password-regexp-validator.directive';
import {matchingPasswordValidator} from '../../../../validators/matching-password-validator.directive';
import {MatchingPasswordMatcherDirective} from '../../../../validators/matching-password-matcher.directive';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['../personal-components-css/change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  changePasswordForm: FormGroup;
  isSubmitted: boolean = false;
  matcher = new ErrorMatcherDirective();
  showHiddenInput: boolean = false;
  hide = true;
  CrossFieldErrorMatcher = new MatchingPasswordMatcherDirective();
  errorMessage = '';

  @ViewChild('hideIt') hideEm: ElementRef;
  @ViewChild('otpButton') otpButton: ElementRef;

  submit() {
    this.hideEm.nativeElement.focus();
  }

  constructor(private fb: FormBuilder,
              private tokenStorageService: TokenStorageService,
              private dataService: DataService,
              private authService: AuthService,
              private profileService: ProfileService,
              private dialogService: DialogService,
              private el: ElementRef) {
  }



  createForm() {
    this.changePasswordForm = this.fb.group({
      oldPassword: this.fb.control('', {updateOn: 'blur'}),
      password: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.minLength(8), passwordPatternValidator]
      }),
      matchingPassword: this.fb.control('', {updateOn: 'change'}),
      otpNum: this.fb.control('', {updateOn: 'blur', validators:[Validators.required]})
    }, {validator: matchingPasswordValidator},);
  }

  generateOtpNumber() {
    this.otpButton.nativeElement.remove();
    this.showHiddenInput = true;
    this.profileService.generateOtpNumber().subscribe();
  }

  errCleaner(){
    this.changePasswordForm.controls['oldPassword'].setErrors({'invalidPassword' : null});
    this.changePasswordForm.controls['oldPassword'].updateValueAndValidity();
    this.changePasswordForm.controls['otpNum'].setErrors({'invalidOtpNum' : null});
    this.changePasswordForm.controls['otpNum'].updateValueAndValidity();

    this.changePasswordForm.controls['oldPassword'].setErrors({'nullVal' : null});
    this.changePasswordForm.controls['oldPassword'].updateValueAndValidity();
    this.changePasswordForm.controls['password'].setErrors({'nullVal' : null});
    this.changePasswordForm.controls['password'].updateValueAndValidity();
    this.changePasswordForm.controls['matchingPassword'].setErrors({'nullVal' : null});
    this.changePasswordForm.controls['matchingPassword'].updateValueAndValidity();
  }

  onSubmit(): void {
    this.submit();
    this.errCleaner();
    this.isSubmitted = true;
    this.profileService.changePassword(this.changePasswordForm.value).subscribe(
      data => {
        this.dialogService.openSuccessfullyChangedPassword();
      },
      err => {
        this.errorMessage = err.message;
        if (this.changePasswordForm.controls['password'].value == '') {
          this.changePasswordForm.controls['password'].setErrors({'nullVal': true, 'pristine': true});
        }
        if (this.changePasswordForm.controls['matchingPassword'].value === '') {
          this.changePasswordForm.controls['matchingPassword'].setErrors({'nullVal': true, 'pristine': true});
        }
        if (this.changePasswordForm.controls['oldPassword'].value === '') {
          this.changePasswordForm.controls['oldPassword'].setErrors({'nullVal': true, 'pristine': true});
        }
        if(this.errorMessage.includes("Incorrect old pass")){
          this.changePasswordForm.controls['oldPassword'].setErrors({'invalidPassword': true, 'pristine': true});
        }
        if(this.errorMessage.includes("Invalid or expired otp")){
          this.changePasswordForm.controls['otpNum'].setErrors({'invalidOtpNum': true, 'pristine': true});
        }
        for (const key of Object.keys(this.changePasswordForm.controls)) {
          if (this.changePasswordForm.controls[key].invalid) {
            const invalidControl = this.el.nativeElement.querySelector('[formcontrolname="' + key + '"]');
            invalidControl.focus();
            break;
          }
        }
      });
  }

  ngOnInit(): void {
    this.createForm();
  }
}
