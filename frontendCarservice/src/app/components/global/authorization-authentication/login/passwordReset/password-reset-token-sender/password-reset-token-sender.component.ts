import {Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../../../../../services/auth.service';
import {DialogService} from '../../../../../../services/dialog.service';
import {notExistingEmailValidator} from '../../../../../validators/email-not-existing-validator.directive';
import {ErrorMatcherDirective} from '../../../../../validators/error-matcher.directive';
import {DataService} from '../../../../../../services/data.service';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset-token-sender.component.html',
  styleUrls: ['./password-reset-token-sender.component.css']
})
export class PasswordResetTokenSenderComponent implements OnInit {

  resetPasswordForm: FormGroup;
  errorMessage = '';
  isSendFailed = false;
  isSubmitted: boolean;
  matcher = new ErrorMatcherDirective();

  @ViewChild('hideIt') hideEm: ElementRef;

  @HostListener('document:keyup', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (event.key === 'Escape') {
      this.close();
    }
  }

  submit() {
    this.hideEm.nativeElement.focus();
  }

  constructor(private dialogRef: MatDialogRef<PasswordResetTokenSenderComponent>,
              private fb: FormBuilder,
              private authService: AuthService,
              private dialogService: DialogService,
              private dataService: DataService,
              private el: ElementRef) {
    this.createForm();
  }

  createForm() {
    this.resetPasswordForm = this.fb.group({
      email: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.pattern(this.dataService.patternEmail), Validators.required],
        asyncValidators: [notExistingEmailValidator(this.authService)]
      }),
    }, {updateOn: 'submit'});
  }

  ngOnInit(): void {
  }

  doLogin(e) {
    console.log(e);
  }

  onSubmit(): void {
    this.submit();
    this.isSubmitted = true;
    this.authService.resetPassword(this.resetPasswordForm.value.email).subscribe(
      data => {
        this.isSendFailed = false;
        this.dialogService.openSuccessDialog('password-token-sent.html');
        this.dialogRef.close();
      },
      err => {
        if (this.resetPasswordForm.controls.email.value === '') {
          this.resetPasswordForm.controls.email.setErrors({required: true, pristine: true});
        }
        this.isSendFailed = true;
        for (const key of Object.keys(this.resetPasswordForm.controls)) {
          if (this.resetPasswordForm.controls[key].invalid) {
            const invalidControl = this.el.nativeElement.querySelector('[formcontrolname="' + key + '"]');
            invalidControl.focus();
            break;
          }
        }
      }
    );
  }

  close() {
    this.dialogRef.close();
  }

}
