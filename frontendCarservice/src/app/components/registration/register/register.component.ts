import {Component, HostListener, OnInit, Renderer2, ViewEncapsulation} from '@angular/core';
import {AuthService} from '../../../services/auth.service';
import {MatDialog, MatDialogConfig, MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import {matchingPasswordValidator} from '../../validators/matching-password-validator.directive';
import {MatchingPasswordMatcherDirective} from '../../validators/matching-password-matcher.directive';
import {passwordPatternValidator} from '../../validators/password-regexp-validator.directive';
import {existingEmailValidator} from '../../validators/existing-email-validator.directive';
import {LoginDialogComponent} from '../../login/login-dialog/login-dialog.component';
import {RegistrationSuccessfulComponent} from '../registration-successful/registration-successful.component';
import {DialogService} from '../../../services/dialog.service';

@HostListener('document:keydown.meta.k')
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class RegisterComponent implements OnInit {
  form: any = {};
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  submitted: boolean;
  registerForm: FormGroup;
  data = false;
  hide = true;
  matcher = new ErrorStateMatcher();
  CrossFieldErrorMatcher = new MatchingPasswordMatcherDirective();

  constructor(private authService: AuthService,
              private dialogRef: MatDialogRef<RegisterComponent>,
              private renderer: Renderer2,
              private fb: FormBuilder,
              private dialogService: DialogService) {
    this.createForm();
  }

  createForm() {
    const patternEmail = '^[a-zA-Z0-9_.+-]+@+[a-zA-Z-09-]+\\.[a-zA-Z0-9-.]+$';
    this.registerForm = this.fb.group({
      email: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.pattern(patternEmail), Validators.required],
        asyncValidators: [existingEmailValidator(this.authService)]
      }),
      password: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.required, Validators.minLength(8), passwordPatternValidator]
      }),
      matchingPassword: this.fb.control('', {updateOn: 'change', validators: [Validators.required, Validators.minLength(8)]}),
      data: this.fb.control(''),
      hide: this.fb.control('')
    }, {validator: matchingPasswordValidator});
  }

  ngOnInit(): void {
    this.renderer.listen(document, 'keydown', event => {
      if (event.key === 'Enter' && this.registerForm.valid) {
        this.onSubmit();
      } else if (event.key === 'Escape') {
        this.close();
      }
    });
  }

  onSubmit() {
    this.submitted = true;
    this.authService.register(this.registerForm.value).subscribe(
      data => {
        this.isSuccessful = true;
        this.isSignUpFailed = false;
        this.dialogRef.close();
        this.dialogService.openSuccessfulRegisterDialog();
      },
      err => {
        this.errorMessage = err.error.errors;
        this.isSignUpFailed = true;
      }
    );
  }

  close() {
    this.dialogRef.close();
  }
}
