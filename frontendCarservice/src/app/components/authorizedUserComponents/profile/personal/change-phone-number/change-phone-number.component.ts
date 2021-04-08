import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {TokenStorageService} from '../../../../../services/token-storage.service';
import {DataService} from '../../../../../services/data.service';
import {AuthService} from '../../../../../services/auth.service';
import {ProfileService} from '../../../../../services/profile.service';
import {DialogService} from '../../../../../services/dialog.service';

@Component({
  selector: 'app-change-phone-number',
  templateUrl: './change-phone-number.component.html',
  styleUrls: ['../personal-components-css/change-password.component.scss']
})
export class ChangePhoneNumberComponent implements OnInit {

  changePhoneNumberForm: FormGroup;
  isSubmitted: boolean = false;
  errorMessage = '';

  @ViewChild('hideIt') hideEm: ElementRef;

  submit() {
    this.hideEm.nativeElement.focus();
  }

  constructor(private fb: FormBuilder,
              private tokenStorageService: TokenStorageService,
              private dataService: DataService,
              private authService: AuthService,
              private profileService: ProfileService,
              private dialogService: DialogService,
              private el: ElementRef) {
    this.createForm();
  }

  createForm() {
    this.changePhoneNumberForm = this.fb.group({
      phoneNumber: this.fb.control('',{updateOn: 'blur', validators: [Validators.minLength(8), Validators.maxLength(14), Validators.pattern('[0-9]+')]}),
  })
  }


  onSubmit(): void {
    this.submit();
    this.isSubmitted = true;
    this.profileService.changePhoneNumber(this.changePhoneNumberForm.value).subscribe(
      data => {
        this.dialogService.openSuccessDialog('phonenumber-changed-successfully.html');
      },
      err => {
        this.errorMessage = err.message;
        for (const key of Object.keys(this.changePhoneNumberForm.controls)) {
          if (this.changePhoneNumberForm.controls[key].invalid) {
            const invalidControl = this.el.nativeElement.querySelector('[formcontrolname="' + key + '"]');
            invalidControl.focus();
            break;
          }
        }
      });
  }

  ngOnInit(): void {
  }
}
