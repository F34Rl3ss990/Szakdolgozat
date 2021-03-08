import {Component, OnInit, Renderer2} from '@angular/core';
import {ErrorStateMatcher} from '@angular/material/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatDialog, MatDialogConfig, MatDialogRef} from '@angular/material/dialog';
import {AuthService} from '../../../services/auth.service';
import {DialogService} from '../../../services/dialog.service';
import {existingEmailValidator} from '../../validators/existing-email-validator.directive';
import {passwordPatternValidator} from '../../validators/password-regexp-validator.directive';
import {matchingPasswordValidator} from '../../validators/matching-password-validator.directive';
import {ActivatedRoute} from '@angular/router';
import {DataService} from '../../../services/data.service';
import {MatchingPasswordMatcherDirective} from '../../validators/matching-password-matcher.directive';
import {VerificationDialogComponent} from '../../registration/verification-dialog/verification-dialog.component';
import {PasswordSuccessfullyChangedComponent} from '../password-successfully-changed/password-successfully-changed.component';
import {ErrorMatcherDirective} from '../../validators/error-matcher.directive';

@Component({
  selector: 'app-password-reset-dialog',
  templateUrl: './password-reset-dialog.component.html',
  styleUrls: ['./password-reset-dialog.component.css']
})
export class PasswordResetDialogComponent implements OnInit {

  matcher = new ErrorMatcherDirective();
  resetPasswordForm: FormGroup;
  errorMessage = '';
  isResetFailed: boolean = false;
  token: string;
  hide = true;
  CrossFieldErrorMatcher = new MatchingPasswordMatcherDirective();


  constructor(private dialogRef: MatDialogRef<PasswordResetDialogComponent>,
              private renderer: Renderer2,
              private fb: FormBuilder,
              private authService: AuthService,
              private dialogService: DialogService,
              private dataService: DataService,
              private dialog: MatDialog) {
    this.createForm();
  }

  createForm() {
    this.resetPasswordForm = this.fb.group({
      password: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.required, Validators.minLength(8), passwordPatternValidator]
      }),
      matchingPassword: this.fb.control('', {updateOn: 'change', validators: [Validators.required, Validators.minLength(8)]}),
      hide: this.fb.control('')
    }, {validator: matchingPasswordValidator});
  }

  ngOnInit(): void {
    this.renderer.listen(document, 'keydown', event => {
      if (event.key === 'Enter' && this.resetPasswordForm.valid) {
        this.onSubmit();
      } else if (event.key === 'Escape') {
        this.close();
      }
    });
    this.token = this.dataService.token;
  }

  onSubmit() {
    this.authService.savePassword(this.resetPasswordForm.value, this.token).subscribe(
      data => {
        this.dialogRef.close();
        this.openDialog();
      },
      err => {
        this.errorMessage = err.error.errors;
        this.isResetFailed = true;
      }
    );
  }
  openDialog(): void {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'custom-dialog-container';
    this.dialog.open(PasswordSuccessfullyChangedComponent, dialogConfig);
  }

  close() {
    this.dialogRef.close();
  }

}
