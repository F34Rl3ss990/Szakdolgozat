import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DataService} from '../../../services/data.service';
import {ServiceReservationService} from '../../../services/service-reservation.service';
import {Router} from '@angular/router';
import {serviceableCarList} from '../../../models/serviceableCarList';
import {ProfileService} from '../../../services/profile.service';
import {DialogService} from '../../../services/dialog.service';

@Component({
  selector: 'app-fallback-if-no-user',
  templateUrl: './fallback-if-no-user.component.html',
  styleUrls: ['./fallback-if-no-user.component.css']
})
export class FallbackIfNoUserComponent implements OnInit {

  userAndCarSaverForm: FormGroup;
  billingToCompany = false;
  billingSameAsUserData = true;
  billingForeignCountryTax = false;
  isSubmitted: boolean;
  errorMessage = '';
  foreignCountryPlate = false;
  serviceableCarList: serviceableCarList[];
  loading: boolean;
  brandSet = new Set();
  typeSet = new Set();
  yearOfManufactureSet = new Set();
  engineTypeSet = new Set();
  selectedBrand = '';
  selectedType = '';
  selectedYearOfManufacture = '';
  selectedEngineType = '';

  @ViewChild('hideIt') hideIt: ElementRef;

  submit() {
    this.hideIt.nativeElement.focus();
  }

  constructor(private fb: FormBuilder,
              private serviceReservation: ServiceReservationService,
              private el: ElementRef,
              private router: Router,
              private dataService: DataService,
              private profileService: ProfileService,
              private dialogService: DialogService) {
    this.listSetter();
    this.createForm();
  }

  ngOnInit(): void {

  }

  listSetter(): void {
    this.loading = true;
    this.serviceReservation.getServiceableCarsList().subscribe(data => {
      this.serviceableCarList = data;
      for (const item of this.serviceableCarList) {
        this.brandSet.add(item.brand);
      }
    }, error => {
      this.loading = false;
    });
  }

  createForm() {
    this.userAndCarSaverForm = this.fb.group({
      name: this.fb.control('', {updateOn: 'blur', validators: [Validators.required, Validators.pattern(this.dataService.negatedSet)]}),
      phoneNumber: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.required, Validators.minLength(8), Validators.maxLength(14), Validators.pattern('[0-9]+')]
      }),
      brand: this.fb.control('', {updateOn: 'blur', validators: [Validators.required]}),
      type: this.fb.control('', {updateOn: 'blur', validators: [Validators.required]}),
      yearOfManufacture: this.fb.control('', {updateOn: 'blur', validators: [Validators.required]}),
      engineType: this.fb.control('', {updateOn: 'blur', validators: [Validators.required]}),
      mileage: this.fb.control('', {updateOn: 'blur', validators: [Validators.max(999999), Validators.pattern('[0-9]+')]}),
      engineNumber: this.fb.control('', {updateOn: 'blur', validators: [Validators.pattern('^[A-Za-z0-9]+$')]}),
      chassisNumber: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.pattern('^[A-Za-z0-9]+$'), Validators.minLength(17), Validators.maxLength(17)]
      }),
      licensePlateNumber: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.pattern('^[a-zA-Z]{3}[-][0-9]{3}$|[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{2}$|[/p/P][-][0-9]{5}$|^[a-zA-z]{3}[0-9]{5}')]
      }),
      foreignCountryPlate: this.fb.control('', {}),
      reservedDate: this.fb.control({value: ''}, {updateOn: 'blur', validators: Validators.required}),
      billingToCompany: this.fb.control('', {}),
      billingSameAsUserData: this.fb.control(''),
      billingName: this.fb.control('', {updateOn: 'blur', validators: Validators.required}),
      billingPhoneNumber: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.required, Validators.minLength(8), Validators.maxLength(14), Validators.pattern('[0-9]+')]
      }),
      billingEmail: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.required, Validators.pattern(this.dataService.patternEmail)]
      }),
      billingZipCode: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.required, Validators.minLength(4), Validators.maxLength(4), Validators.pattern('[0-9]+')]
      }),
      billingTown: this.fb.control('', {
        updateOn: 'blur',
        validators: [Validators.required, Validators.pattern(this.dataService.negatedSet)]
      }),
      billingStreet: this.fb.control('', {updateOn: 'blur', validators: Validators.required}),
      billingOtherAddressType: this.fb.control('', {}),
      billingTax: this.fb.control('', {updateOn: 'blur'}),
      billingForeignCountryTax: this.fb.control('', {}),
    }, {updateOn: 'submit'});
  }

  licensePlateValidator(event) {
    if (!event.checked) {
      this.userAndCarSaverForm.controls.licensePlateNumber.setValidators(Validators.pattern('^[a-zA-Z]{3}[-][0-9]{3}$|[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{2}$|[/p/P][-][0-9]{5}$|^[a-zA-z]{3}[0-9]{5}'));
      this.userAndCarSaverForm.controls.licensePlateNumber.updateValueAndValidity();
    } else {
      this.userAndCarSaverForm.controls.licensePlateNumber.clearValidators();
      this.userAndCarSaverForm.controls.licensePlateNumber.updateValueAndValidity();
    }
  }

  taxNumberValidator(event) {
    if (!event.checked) {
      this.userAndCarSaverForm.controls.billingTax.
      setValidators([Validators.pattern('^[0-9]{8}[-][0-9][-][0-9]{2}$'), Validators.required]);
      this.userAndCarSaverForm.controls.billingTax.updateValueAndValidity();
    } else {
      this.userAndCarSaverForm.controls.billingTax.clearValidators();
      this.userAndCarSaverForm.controls.billingTax.setValidators(Validators.required);
      this.userAndCarSaverForm.controls.billingTax.updateValueAndValidity();
    }
  }

  taxNumberRequiredSetter(event) {
    if (event.checked) {
      this.userAndCarSaverForm.controls.billingTax.
      setValidators([Validators.pattern('^[0-9]{8}[-][0-9][-][0-9]{2}$'), Validators.required]);
      this.userAndCarSaverForm.controls.billingTax.updateValueAndValidity();
    } else {
      this.userAndCarSaverForm.controls.billingTax.clearValidators();
      this.userAndCarSaverForm.controls.billingTax.updateValueAndValidity();
    }
  }

  typeSetter(data) {
    this.typeSet.clear();
    this.yearOfManufactureSet.clear();
    this.engineTypeSet.clear();
    if (this.userAndCarSaverForm.controls.type.value !== '') {
      this.userAndCarSaverForm.controls.type.setValue('');
      this.userAndCarSaverForm.controls.yearOfManufacture.setValue('');
      this.userAndCarSaverForm.controls.engineType.setValue('');
    }
    for (const item of this.serviceableCarList) {
      if (item.brand === data.value) {
        this.typeSet.add((item.type));
      }
    }
  }

  yearOfManufactureSetter(data) {
    this.yearOfManufactureSet.clear();
    this.engineTypeSet.clear();
    if (this.userAndCarSaverForm.controls.yearOfManufacture.value !== '') {
      this.userAndCarSaverForm.controls.yearOfManufacture.setValue('');
      this.userAndCarSaverForm.controls.engineType.setValue('');
    }
    for (const item of this.serviceableCarList) {
      if (item.brand === this.selectedBrand
        && item.type === data.value) {
        this.yearOfManufactureSet.add(item.yearOfManufacture);
      }
    }
  }

  engineTypeSetter(data) {
    if (this.userAndCarSaverForm.controls.engineType.value !== '') {
      this.userAndCarSaverForm.controls.engineType.setValue('');
    }
    this.engineTypeSet.clear();
    for (const item of this.serviceableCarList) {
      if (item.brand === this.selectedBrand
        && item.type === this.selectedType
        && item.yearOfManufacture === data.value) {
        this.engineTypeSet.add((item.engineType));
      }
    }
  }

  billingDataSetter() {
    if (this.billingSameAsUserData) {
      this.userAndCarSaverForm.controls.billingName.setValue(this.userAndCarSaverForm.value.name);
      this.userAndCarSaverForm.controls.billingName.disable();
      this.userAndCarSaverForm.controls.billingPhoneNumber.setValue(this.userAndCarSaverForm.value.phoneNumber);
      this.userAndCarSaverForm.controls.billingPhoneNumber.disable();
      this.userAndCarSaverForm.controls.billingEmail.setValue(this.userAndCarSaverForm.value.email);
      this.userAndCarSaverForm.controls.billingEmail.disable();

    } else {
      this.userAndCarSaverForm.controls.billingName.setValue('');
      this.userAndCarSaverForm.controls.billingName.enable();
      this.userAndCarSaverForm.controls.billingPhoneNumber.setValue('');
      this.userAndCarSaverForm.controls.billingPhoneNumber.enable();
      this.userAndCarSaverForm.controls.billingEmail.setValue('');
      this.userAndCarSaverForm.controls.billingEmail.enable();
    }
  }

  engineAndChassisSetterIfNull() {
    if (this.userAndCarSaverForm.controls.chassisNumber.value === '') {
      this.userAndCarSaverForm.controls.chassisNumber.setValue(null);
    }
    if (this.userAndCarSaverForm.controls.engineNumber.value === '') {
      this.userAndCarSaverForm.controls.engineNumber.setValue(null);
    }
  }

  autoFocusOnError() {
    for (const key of Object.keys(this.userAndCarSaverForm.controls)) {
      if (this.userAndCarSaverForm.controls[key].invalid) {
        const invalidControl = this.el.nativeElement.querySelector('[formcontrolname="' + key + '"]');
        invalidControl.focus();
        break;
      }
    }
  }

  errCleaner() {
    this.userAndCarSaverForm.controls.billingTax.setErrors({badTaxPattern: null});
    this.userAndCarSaverForm.controls.licensePlateNumber.setErrors({badLicensePlatePattern: null});
    this.userAndCarSaverForm.controls.billingTax.updateValueAndValidity();
    this.userAndCarSaverForm.controls.licensePlateNumber.updateValueAndValidity();
  }


  onSubmit(): void {
    this.submit();
    this.engineAndChassisSetterIfNull();
    this.isSubmitted = true;
    this.errCleaner();
    if (!this.userAndCarSaverForm.valid) {
      this.autoFocusOnError();
      return;
    } else {
      this.profileService.addCarAndUser(this.userAndCarSaverForm.getRawValue(),
        this.foreignCountryPlate, this.billingToCompany).subscribe(data => {
          this.dialogService.openSuccessDialog('successfully-added-user-and-car.html');
        },
        err => {
          this.errorMessage = err.error.message;
          if (this.errorMessage.includes('Tax number is inc')) {
            this.userAndCarSaverForm.controls.billingTax.setErrors({badTaxPattern: true});
          }
          if (this.errorMessage.includes('LicensePlate is incor')) {
            this.userAndCarSaverForm.controls.licensePlateNumber.setErrors({badLicensePlatePattern: true});
          }
          this.autoFocusOnError();
        });
    }
  }
}
