import {Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild} from '@angular/core';
import {AuthService} from '../../../../../services/auth.service';
import {TokenStorageService} from '../../../../../services/token-storage.service';
import {MatDialog, MatDialogConfig, MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, FormGroupDirective, Validators} from '@angular/forms';
import {DialogService} from '../../../../../services/dialog.service';
import {ErrorMatcherDirective} from '../../../../validators/error-matcher.directive';
import {DataService} from '../../../../../services/data.service';


@Component({
  selector: 'app-login',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.css']
})
export class LoginDialogComponent implements OnInit {
  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage: string = "";
  roles: string[] = [];
  loginForm: FormGroup;
  hide = true;
  isSubmitted: boolean;
  matcher = new ErrorMatcherDirective();

  @ViewChild('hideIt') hideIt : ElementRef;



  @HostListener('document:keyup', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
     if(event.key === 'Escape'){
      this.close()
    }
  }

  submit(){
    this.hideIt.nativeElement.focus();
  }

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService,
              private dialogRef: MatDialogRef<LoginDialogComponent>,
              private fb: FormBuilder,
              private dialogService: DialogService,
              private dataService: DataService,
              private el: ElementRef) {
    this.createForm();
  }


  createForm() {
    this.loginForm = this.fb.group({
      email: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.pattern(this.dataService.patternEmail), Validators.required]
      }),
      password: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.required]
      }),
      hide: this.fb.control(''),
      updateOn: 'submit'
    });
  }

  openRegistrationDialog() {
    this.dialogService.openRegistrationDialog();
    this.dialogRef.close();
  }
  resetPasswordDialog(){
    this.dialogService.openPasswordResetTokenSenderDialog();
      this.dialogRef.close();
  }

  ngOnInit(): void {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }
  }

  onSubmit(): void {
    this.submit()
    this.isSubmitted = true;
    this.authService.login(this.loginForm.value).subscribe(
      data => {
        this.tokenStorage.saveToken(data.token);
        this.tokenStorage.saveUser(data);
        this.roles = this.tokenStorage.getUser().roles;
        this.isLoggedIn = true;
        this.dialogRef.close();
        this.reloadPage()
      },
      err => {
        this.errorMessage = err.error.message;
        if(!this.loginForm.controls['email'].valid){
          this.loginForm.controls['email'].setErrors({'pattern': true});
        }
        if(this.loginForm.controls['email'].value==''){
          this.loginForm.controls['email'].setErrors({'required': true, 'pristine' : true});
        }
        if(this.loginForm.controls['password'].value==''){
          this.loginForm.controls['password'].setErrors({'required': true, 'pristine' : true});
        }
        if(this.loginForm.controls['email'].valid && this.loginForm.controls['password'].value!='' && this.errorMessage.includes("Bad credentials")) {
          this.loginForm.controls['password'].setErrors({'badCredentials': true});
        }
        if(this.errorMessage.includes("Account is not verified")) {
          this.loginForm.controls['password'].setErrors({'notVerified': true})
        }
        if(this.errorMessage.includes("Account is banned")) {
          this.loginForm.controls['password'].setErrors({'banned': true})
        }
        for (const key of Object.keys(this.loginForm.controls)) {
          if (this.loginForm.controls[key].invalid) {
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

  reloadPage() {
    window.location.reload();
  }
}
