import {Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {AuthService} from '../../../../../../services/auth.service';
import {DialogService} from '../../../../../../services/dialog.service';
import {passwordPatternValidator} from '../../../../../validators/password-regexp-validator.directive';
import {matchingPasswordValidator} from '../../../../../validators/matching-password-validator.directive';
import {DataService} from '../../../../../../services/data.service';
import {MatchingPasswordMatcherDirective} from '../../../../../validators/matching-password-matcher.directive';
import {ErrorMatcherDirective} from '../../../../../validators/error-matcher.directive';

@Component({
  selector: 'app-password-reset-dialog',
  templateUrl: './password-reset-dialog.component.html',
  styleUrls: ['./password-reset-dialog.component.css']
})
export class PasswordResetDialogComponent implements OnInit {


  resetPasswordForm: FormGroup;
  errorMessage = '';
  isResetFailed: boolean = false;
  token: string;
  hide = true;
  CrossFieldErrorMatcher = new MatchingPasswordMatcherDirective();
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

  constructor(private dialogRef: MatDialogRef<PasswordResetDialogComponent>,
              private renderer: Renderer2,
              private fb: FormBuilder,
              private authService: AuthService,
              private dialogService: DialogService,
              private dataService: DataService,
              private dialog: MatDialog,
              private el: ElementRef) {
    this.createForm();
  }

  createForm() {
    this.resetPasswordForm = this.fb.group({
      password: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.required, Validators.minLength(8), passwordPatternValidator]
      }),
      matchingPassword: this.fb.control('', {updateOn: 'change', validators: []}),
      hide: this.fb.control('')
    }, {validator: matchingPasswordValidator});
  }

  ngOnInit(): void {
    this.token = this.dataService.token;
  }

  onSubmit() {
    this.submit()
    this.isSubmitted = true;
    this.authService.savePassword(this.resetPasswordForm.value, this.token).subscribe(
      data => {
        this.dialogRef.close();
        this.dialogService.openSuccessDialog('password-successfully-changed.html');
      },
      err => {
        this.errorMessage = err.error.errors;
        this.isResetFailed = true;
        if (this.resetPasswordForm.controls['matchingPassword'].value == '') {
          this.resetPasswordForm.controls['matchingPassword'].setErrors({'required': true});
        }
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
