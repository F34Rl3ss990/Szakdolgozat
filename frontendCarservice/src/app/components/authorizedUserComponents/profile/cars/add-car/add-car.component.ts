import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {serviceableCarList} from '../../../../../models/serviceableCarList';
import {ServiceReservationService} from '../../../../../services/service-reservation.service';
import {DataService} from '../../../../../services/data.service';
import {ProfileService} from '../../../../../services/profile.service';
import {DialogService} from '../../../../../services/dialog.service';

@Component({
  selector: 'app-add-car',
  templateUrl: './add-car.component.html',
  styleUrls: ['./add-car.component.css']
})
export class AddCarComponent implements OnInit {

  addCarForm: FormGroup;
  loading: boolean;
  brandSet = new Set();
  typeSet = new Set();
  yearOfManufactureSet = new Set();
  engineTypeSet = new Set();
  isSubmitted: boolean;
  errorMessage = ''
  foreignCountryPlate = false;
  serviceableCarList: serviceableCarList[];
  selectedBrand = '';
  selectedType = '';
  selectedYearOfManufacture = '';
  selectedEngineType = '';


  @ViewChild('hideIt') hideIt: ElementRef;

  submit(){
    this.hideIt.nativeElement.focus();
  }

  constructor(private fb: FormBuilder,
              private serviceReservation: ServiceReservationService,
              private el: ElementRef,
              private dataService: DataService,
              private profileService: ProfileService,
              private dialogService: DialogService) {
    this.listSetter();
    this.createForm();
  }

  createForm() {
    this.addCarForm = this.fb.group({
      name: this.fb.control('', {updateOn: 'blur', validators: [Validators.required, Validators.pattern(this.dataService.negatedSet)]}),
      email: this.fb.control('', {updateOn: 'blur', validators: [Validators.pattern(this.dataService.patternEmail), Validators.required]}),
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
    })
  }

  listSetter(): void {
    this.loading = true;
    this.serviceReservation.getServiceableCarsList().subscribe(data => {
      this.serviceableCarList = data;
      for(let item of this.serviceableCarList){
        this.brandSet.add(item.brand)
      }
    },  error => {
      this.loading = false;
    });
  }

  licensePlateValidator(event){
    if(!event.checked) {
      this.addCarForm.controls['licensePlateNumber'].setValidators(Validators.pattern('^[a-zA-Z]{3}[-][0-9]{3}$|[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{2}$|[/p/P][-][0-9]{5}$|^[a-zA-z]{3}[0-9]{5}'))
      this.addCarForm.controls['licensePlateNumber'].updateValueAndValidity();
    } else {
      this.addCarForm.controls['licensePlateNumber'].clearValidators();
      this.addCarForm.controls['licensePlateNumber'].updateValueAndValidity();
    }
  }

  typeSetter(data){
    this.typeSet.clear()
    this.yearOfManufactureSet.clear()
    this.engineTypeSet.clear()
    for(let item of this.serviceableCarList){
      if(item.brand === data.value){
        this.typeSet.add((item.type))
      }
    }
  }

  yearOfManufactureSetter(data){
    this.yearOfManufactureSet.clear()
    this.engineTypeSet.clear()
    for(let item of this.serviceableCarList){
      if(item.brand === this.selectedBrand
        && item.type === data.value){
        console.log(item.yearOfManufacture)
        this.yearOfManufactureSet.add(item.yearOfManufacture)
      }
    }
  }

  engineTypeSetter(data){
    for(let item of this.serviceableCarList){
      if(item.brand === this.selectedBrand
        && item.type === this.selectedType
        && item.yearOfManufacture === data.value){
        this.engineTypeSet.add((item.engineType))
      }
    }
  }
  engineAndChassisSetterIfNull(){
    if(this.addCarForm.controls['chassisNumber'].value==''){
      this.addCarForm.controls['chassisNumber'].setValue(null)
    }
    if(this.addCarForm.controls['engineNumber'].value ==''){
      this.addCarForm.controls['engineNumber'].setValue(null)
    }
  }

  autoFocusOnError() {
    for (const key of Object.keys(this.addCarForm.controls)) {
      if (this.addCarForm.controls[key].invalid) {
        const invalidControl = this.el.nativeElement.querySelector('[formcontrolname="' + key + '"]');
        invalidControl.focus();
        break;
      }
    }
  }

  errCleaner(){
    this.addCarForm.controls['licensePlateNumber'].setErrors({'badLicensePlatePattern' : null});
    this.addCarForm.controls['licensePlateNumber'].updateValueAndValidity();
  }

  onSubmit(): void{
    this.submit()
    this.engineAndChassisSetterIfNull()
    this.isSubmitted = true;
    this.errCleaner()
    if(!this.addCarForm.valid) {
      this.autoFocusOnError()
      return;
    } else {
      this.profileService.addCar(this.addCarForm.value, this.foreignCountryPlate).subscribe(data =>{
          this.dialogService.openSuccessDialog('car-added-successfully.html')
        },
        err => {
          this.errorMessage = err.error.message;
          if(this.errorMessage.includes('Tax number is inc')){
            this.addCarForm.controls['billingTax'].setErrors({'badTaxPattern' : true});
          }
          if(this.errorMessage.includes('LicensePlate is incor')){
            this.addCarForm.controls['licensePlateNumber'].setErrors({'badLicensePlatePattern' : true});
          }
          this.autoFocusOnError()
        })
    }
  }

  ngOnInit(): void {
  }

}
