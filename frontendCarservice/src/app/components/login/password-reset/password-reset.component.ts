import {Component, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import {existingEmailValidator} from '../../validators/existing-email-validator.directive';
import {passwordPatternValidator} from '../../validators/password-regexp-validator.directive';
import {matchingPasswordValidator} from '../../validators/matching-password-validator.directive';
import {AuthService} from '../../../services/auth.service';
import {DialogService} from '../../../services/dialog.service';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  matcher = new ErrorStateMatcher();
  resetPasswordForm: FormGroup;
  errorMessage = '';
  isSendFailed: boolean = false;

  constructor(private dialogRef: MatDialogRef<PasswordResetComponent>,
              private renderer: Renderer2,
              private fb: FormBuilder,
              private authService: AuthService,
              private dialogService: DialogService) {
    this.createForm();
  }

  createForm() {
    const patternEmail = '^[a-zA-Z0-9_.+-]+@+[a-zA-Z-09-]+\\.[a-zA-Z0-9-.]+$';
    this.resetPasswordForm = this.fb.group({
      email: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.pattern(patternEmail), Validators.required],
        asyncValidators: [existingEmailValidator(this.authService)]
      }),
    })
  }

  ngOnInit(): void {
    this.renderer.listen(document, 'keydown', event => {
      if (event.key === 'Enter' && this.resetPasswordForm.valid) {
        this.onSubmit();
      } else if (event.key === 'Escape') {
        this.close();
      }
    });
  }

  onSubmit() {
    this.authService.resetPassword(this.resetPasswordForm.value).subscribe(
      data => {
        this.isSendFailed = false;
        this.dialogRef.close();
        this.dialogService.openPasswordTokenSent();
      },
      err => {
        this.errorMessage = err.error.errors;
        this.isSendFailed = true;
      }
    );
  }

  close() {
    this.dialogRef.close();
  }

}
