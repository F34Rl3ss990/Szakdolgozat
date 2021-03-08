import {Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild} from '@angular/core';
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
import {ErrorMatcherDirective} from '../../validators/error-matcher.directive';


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
  matcher = new ErrorMatcherDirective();
  isSuccessful = false;
  isSignUpFailed = false;

  @HostListener('document:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if(event.key === 'Enter'){
      this.onSubmit()
    } else if(event.key === 'Escape'){
      this.close()
    }
  }



  @ViewChild('submitButton')
  myButton: ElementRef;

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService,
              private dialogRef: MatDialogRef<LoginDialogComponent>,
              private renderer: Renderer2,
              private fb: FormBuilder,
              private dialogService: DialogService) {
    this.createForm();
  }


  createForm() {
    const patternEmail = '^[a-zA-Z0-9_.+-]+@+[a-zA-Z-09-]+\\.[a-zA-Z0-9-.]{2,}';
    this.loginForm = this.fb.group({
      email: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.pattern(patternEmail), Validators.required]
      }),
      password: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.required]
      }),
      hide: this.fb.control(''),
      updateOn: "submit"
    });
  }

  openRegistrationDialog() {
    this.dialogService.openRegistrationDialog();
    this.dialogRef.close();
  }
  resetPasswordDialog(){
    this.dialogService.openPasswordResetDialog();
      this.dialogRef.close();
  }

  ngOnInit(): void {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }
  }

  onSubmit(): void {
    this.authService.login(this.loginForm.value).subscribe(
      data => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);
        this.roles = this.tokenStorage.getUser().roles;
        this.isLoggedIn = true;
        this.dialogRef.close();
        this.reloadPage()
      },
      err => {
        this.errorMessage = err.error.errors;
        if(!this.loginForm.controls['email'].valid){
          this.loginForm.controls['email'].setErrors({'pattern': true});
        }
        if(this.loginForm.controls['email'].value==''){
          this.loginForm.controls['email'].setErrors({'required': true, 'pristine' : true});
        }
        if(this.loginForm.controls['password'].value==''){
          this.loginForm.controls['password'].setErrors({'required': true, 'pristine' : true});
        }
        if(this.loginForm.controls['email'].valid && this.loginForm.controls['password'].value!='') {
          this.loginForm.controls['password'].setErrors({'problem': true});
        }
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
