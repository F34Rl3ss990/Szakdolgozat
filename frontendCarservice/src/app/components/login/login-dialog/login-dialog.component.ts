import {Component, OnInit, Renderer2} from '@angular/core';
import {AuthService} from '../../../services/auth.service';
import {TokenStorageService} from '../../../services/token-storage.service';
import {MatDialog, MatDialogConfig, MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {existingEmailValidator} from '../../validators/existing-email-validator.directive';
import {passwordPatternValidator} from '../../validators/password-regexp-validator.directive';
import {matchingPasswordValidator} from '../../validators/matching-password-validator.directive';
import {ErrorStateMatcher} from '@angular/material/core';
import {RegisterComponent} from '../../registration/register/register.component';
import {DialogService} from '../../../services/dialog.service';

@Component({
  selector: 'app-login',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.css']
})
export class LoginDialogComponent implements OnInit {
  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  loginForm: FormGroup;
  hide = true;
  matcher = new ErrorStateMatcher();
  isSuccessful = false;
  isSignUpFailed = false;

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService,
              private dialogRef: MatDialogRef<LoginDialogComponent>,
              private renderer: Renderer2,
              private fb: FormBuilder,
              private dialogSerive: DialogService) {
    this.createForm();
  }

  createForm() {
    const patternEmail = '^[a-zA-Z0-9_.+-]+@+[a-zA-Z-09-]+\\.[a-zA-Z0-9-.]+$';
    this.loginForm = this.fb.group({
      email: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.pattern(patternEmail), Validators.required]
      }),
      password: this.fb.control('', {
        updateOn: 'blur'
      }),
      hide: this.fb.control('')
    });
  }

  openRegistrationDialog() {
    this.dialogSerive.openRegistrationDialog();
    this.dialogRef.close();
  }
  resetPasswordDialog(){
    this.dialogSerive.openPasswordResetDialog();
      this.dialogRef.close();
  }

  ngOnInit() {
    /*if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }*/
    this.renderer.listen(document, 'keydown', event => {
      if (event.key === 'Enter' && this.loginForm.valid) {
        this.save();
      } else if (event.key === 'Escape') {
        this.close();
      }
    });
  }

  save() {
    this.authService.register(this.loginForm.value).subscribe(
      data => {
        this.dialogRef.close();
        this.reloadPage()
      },
      err => {
        this.errorMessage = err.error.errors;
        this.isLoginFailed = true;
        console.log(err.error.errors)
      }
    );
  }

  close() {
    this.dialogRef.close();
  }

  reloadPage() {
    window.location.reload();
  }
}
