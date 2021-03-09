import {Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../../services/auth.service';
import {DialogService} from '../../../services/dialog.service';
import {notExistingEmailValidator} from '../../validators/email-not-existing-validator.directive';
import {ErrorMatcherDirective} from '../../validators/error-matcher.directive';
import {DataService} from '../../../services/data.service';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  resetPasswordForm: FormGroup;
  errorMessage = '';
  isSendFailed: boolean = false;
  isSubmitted: boolean;
  matcher = new ErrorMatcherDirective();

  @ViewChild('hideIt') hideEm : ElementRef;

  @HostListener('document:keyup', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if(event.key === 'Escape'){
      this.close()
    }
  }

  submit(){
    this.hideEm.nativeElement.focus();
  }

  constructor(private dialogRef: MatDialogRef<PasswordResetComponent>,
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
    this.submit();
    this.isSubmitted = true;
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
