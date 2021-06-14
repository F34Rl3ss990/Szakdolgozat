import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DataService} from '../../../../../services/data.service';

import {ProfileService} from '../../../../../services/profile.service';
import {DialogService} from '../../../../../services/dialog.service';

@Component({
  selector: 'app-change-billing-data',
  templateUrl: './change-billing-data.component.html',
  styleUrls: ['../personal-components-css/change-password.component.scss', './change-billing-data.component.css']
})
export class ChangeBillingDataComponent implements OnInit {

  changeBillingDataForm: FormGroup;
  isSubmitted: boolean;
  billingSameAsUserData = true;
  billingForeignCountryTax = false;
  errorMessage = ''
  user: any ={};
  billingToCompany = false;

  @ViewChild('hideIt') hideIt: ElementRef;

  submit(){
    this.hideIt.nativeElement.focus();
  }

  constructor(private fb: FormBuilder,
              private dataService: DataService,
              private profileService: ProfileService,
              private el: ElementRef,
              private dialogService: DialogService) {
    this.createForm();
  }

  createForm(){
    this.changeBillingDataForm = this.fb.group({
      billingSameAsUserData: this.fb.control(''),
      billingName: this.fb.control('', {updateOn: 'blur', validators: Validators.required}),
      billingPhoneNumber: this.fb.control('', {updateOn: 'blur', validators: [Validators.required, Validators.minLength(8), Validators.maxLength(14), Validators.pattern('[0-9]+')]}),
      billingEmail: this.fb.control('', {updateOn: 'blur', validators: [Validators.required, Validators.pattern(this.dataService.patternEmail)]}),
      billingZipCode: this.fb.control('', {updateOn: 'blur', validators: [Validators.required, Validators.minLength(4), Validators.maxLength(4), Validators.pattern('[0-9]+')]}),
      billingTown: this.fb.control('', {updateOn: 'blur', validators: [Validators.required, Validators.pattern(this.dataService.negatedSet)]}),
      billingStreet: this.fb.control('', {updateOn: 'blur', validators: Validators.required}),
      billingOtherAddressType: this.fb.control('', {}),
      billingTax: this.fb.control('', {updateOn: 'blur'}),
      billingForeignCountryTax: this.fb.control('',{}),
    }
   )
  }

  taxNumberValidator(event){
    if(!event.checked) {
      this.changeBillingDataForm.controls['billingTax'].setValidators([Validators.pattern('^[0-9]{8}[-][0-9][-][0-9]{2}$'), Validators.required]);
      this.changeBillingDataForm.controls['billingTax'].updateValueAndValidity();
    } else {
      this.changeBillingDataForm.controls['billingTax'].clearValidators();
      this.changeBillingDataForm.controls['billingTax'].setValidators(Validators.required);
      this.changeBillingDataForm.controls['billingTax'].updateValueAndValidity();
    }
  }

  taxNumberRequiredSetter(event){
    if(event.checked) {
      this.changeBillingDataForm.controls['billingTax'].setValidators([Validators.pattern('^[0-9]{8}[-][0-9][-][0-9]{2}$'), Validators.required])
      this.changeBillingDataForm.controls['billingTax'].updateValueAndValidity();
    } else {
      this.changeBillingDataForm.controls['billingTax'].clearValidators();
      this.changeBillingDataForm.controls['billingTax'].updateValueAndValidity();
    }
  }

  autoFocusOnError() {
    for (const key of Object.keys(this.changeBillingDataForm.controls)) {
      if (this.changeBillingDataForm.controls[key].invalid) {
        const invalidControl = this.el.nativeElement.querySelector('[formcontrolname="' + key + '"]');
        invalidControl.focus();
        break;
      }
    }
  }

  onSubmit(): void{
    this.errCleaner();
    this.submit();
    this.isSubmitted = true;
    if(!this.changeBillingDataForm.valid) {
      this.autoFocusOnError()
      return;
    } else {
      this.profileService.changeBillingData(this.changeBillingDataForm, this.billingForeignCountryTax, this.billingToCompany).subscribe(data =>{
            this.dialogService.openSuccessDialog('billingdata-changed-successfully.html')
        },
        err => {
          this.errorMessage = err.error.message;
          if(this.errorMessage.includes('Tax number is inc')){
            this.changeBillingDataForm.controls['billingTax'].setErrors({'badTaxPattern' : true});
          }
          this.autoFocusOnError()
        })
    }
  }

  errCleaner(){
    this.changeBillingDataForm.controls['billingTax'].setErrors({'badTaxPattern' : null});
    this.changeBillingDataForm.controls['billingTax'].updateValueAndValidity();
  }

  dataSetter(){
    this.changeBillingDataForm.controls['billingName'].setValue(this.user.billingInformation.billingName);
    this.changeBillingDataForm.controls['billingPhoneNumber'].setValue(this.user.billingInformation.billingPhoneNumber);
    this.changeBillingDataForm.controls['billingEmail'].setValue(this.user.billingInformation.billingEmail);
    this.changeBillingDataForm.controls['billingTax'].setValue(this.user.billingInformation.billingTaxNumber);
    this.changeBillingDataForm.controls['billingTown'].setValue(this.user.billingInformation.billingTown);
    this.changeBillingDataForm.controls['billingZipCode'].setValue(this.user.billingInformation.billingZipCode);
    this.changeBillingDataForm.controls['billingStreet'].setValue(this.user.billingInformation.billingStreet);
    this.changeBillingDataForm.controls['billingOtherAddressType'].setValue(this.user.billingInformation.billingOtherAddressType);
  }

  ngOnInit(): void {
    this.profileService.getUserData().subscribe(res => {
      this.user = res;
      this.dataSetter()
      if (this.user.billingInformation.billingTaxNumber != null) {
        this.billingToCompany = true;
      }
      if (this.user.billingInformation.billingTaxNumber != null) {
        if (!this.user.billingInformation.billingTaxNumber.match("^[0-9]{8}[-][0-9][-][0-9]{2}$")) {
          this.billingForeignCountryTax = true;
        }
      }
    })
  }

}
