import {Component, HostListener, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import {existingEmailValidator} from '../../validators/existing-email-validator.directive';
import {passwordPatternValidator} from '../../validators/password-regexp-validator.directive';
import {matchingPasswordValidator} from '../../validators/matching-password-validator.directive';
import {AuthService} from '../../../services/auth.service';
import {DialogService} from '../../../services/dialog.service';
import {notExistingEmailValidator} from '../../validators/email-not-existing-validator.directive';
import {ErrorMatcherDirective} from '../../validators/error-matcher.directive';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  matcher = new ErrorMatcherDirective();
  resetPasswordForm: FormGroup;
  errorMessage = '';
  isSendFailed: boolean = false;

  @HostListener('document:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if(event.key === 'Enter'){
      this.onSubmit()
    } else if(event.key === 'Escape'){
      this.close()
    }
  }

  constructor(private dialogRef: MatDialogRef<PasswordResetComponent>,
              private renderer: Renderer2,
              private fb: FormBuilder,
              private authService: AuthService,
              private dialogService: DialogService) {
    this.createForm();
  }

  createForm() {
    const patternEmail = '^[a-zA-Z0-9_.+-]+@+[a-zA-Z-09-]+\\.[a-zA-Z0-9-.]{2,}';
    this.resetPasswordForm = this.fb.group({
      email: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.pattern(patternEmail), Validators.required],
        asyncValidators: [notExistingEmailValidator(this.authService)]
      }),
    },{updateOn: 'submit'})
  }

  ngOnInit(): void {
  }

  doLogin(e){
    console.log(e);
  }

  onSubmit(): void {
    console.log("valami2")
    console.log(this.resetPasswordForm.controls['email'].value)
    this.authService.resetPassword(this.resetPasswordForm.value.email).subscribe(
      data => {
        this.isSendFailed = false;
        this.dialogService.openPasswordTokenSent();
        this.dialogRef.close();
      },
      err => {
        if(this.resetPasswordForm.controls['email'].value===''){
          this.resetPasswordForm.controls['email'].setErrors({'required' : true, 'pristine': true});
        }
        this.isSendFailed = true;
      }
    );
  }

  close() {
    this.dialogRef.close();
  }

}
