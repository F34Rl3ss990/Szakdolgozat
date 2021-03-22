import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DataService} from '../../../../services/data.service';
import {Subscription} from 'rxjs';
import {ServiceReservationService} from '../../../../services/service-reservation.service';
import {userCarList} from '../../../../models/userCarList';
import {Router} from '@angular/router';
import {TokenStorageService} from '../../../../services/token-storage.service';

@Component({
  selector: 'app-auth-service-reservation',
  templateUrl: './auth-service-reservation.component.html',
  styleUrls: ['./auth-service-reservation.component.css']
})
export class AuthServiceReservationComponent implements OnInit {

  serviceReservationForm: FormGroup;
  subscription: Subscription;
  loading: boolean;
  userCarList: userCarList[];
  otherChecked = false;
  atLeastOneServiceChecked = false;
  collector: any = [];
  dateFieldValue: boolean;
  minDate: Date;
  car;
  isSubmitted: boolean;
  errorMessage = '';
  selectedCar: userCarList;
  carInspection = []
  tyre = []
  brake = []
  undercarriage = []
  periodicService = []
  timingBelt = []
  diagnostic = []
  technicalExamination  = []
  clime = []
  accumulator = []
  bodywork = []
  other = []
  authenticityTest = []
  oil = []

  @ViewChild('hideIt') hideIt: ElementRef;

  holidayList=[
  ]

  submit(){
    this.hideIt.nativeElement.focus();
  }

  constructor(private fb: FormBuilder,
              private dataService: DataService,
              private serviceReservation: ServiceReservationService,
              private el: ElementRef,
              private router: Router,
              private tokenStorage: TokenStorageService) {
    const currentYear = Date.now();
    this.minDate = new Date(currentYear);
    this.checkboxSetter();
    this.listSetter();
    this.createForm();
    if(this.dataService.serviceReservationForm != undefined) {
      this.serviceReservationForm = this.dataService.serviceReservationForm;
      this.selectedCar = this.dataService.selectedCar;
    }
  }

  ngOnInit(): void {
  }

  createForm() {
    this.serviceReservationForm = this.fb.group({
      car: this.fb.control('',{updateOn:'blur', validators: Validators.required}),
      reservedDate: this.fb.control({value: ''}, {updateOn: 'blur', validators: Validators.required}),
      carInspection: this.fb.array(this.carInspection.map(x => false), {updateOn: 'change'}),
      authenticityTest: this.fb.array(this.authenticityTest.map(x => false), {updateOn: 'change'}),
      tyre: this.fb.array(this.tyre.map(x => false), {updateOn: 'change'}),
      brake: this.fb.array(this.brake.map(x => false), {updateOn: 'change'}),
      undercarriage: this.fb.array(this.undercarriage.map(x => false), {updateOn: 'change'}),
      oil: this.fb.array(this.oil.map(x => false), {updateOn: 'change'}),
      periodicService: this.fb.array(this.periodicService.map(x => false), {updateOn: 'change'}),
      timingBelt: this.fb.array(this.timingBelt.map(x => false), {updateOn: 'change'}),
      diagnostic: this.fb.array(this.diagnostic.map(x => false), {updateOn: 'change'}),
      technicalExamination: this.fb.array(this.technicalExamination.map(x => false), {updateOn: 'change'}),
      clime: this.fb.array(this.clime.map(x => false), {updateOn: 'change'}),
      accumulator: this.fb.array(this.accumulator.map(x => false), {updateOn: 'change'}),
      bodywork: this.fb.array(this.bodywork.map(x => false), {updateOn: 'change'}),
      other: this.fb.array(this.other.map(x => false), {updateOn: 'change'}),
      comment: this.fb.control('', {updateOn: 'blur'})
    }, {updateOn: 'submit'});

    const carInsepctionControl = (this.serviceReservationForm.controls.carInspection as FormArray);
    this.subscription = carInsepctionControl.valueChanges.subscribe(checkbox => {
      carInsepctionControl.setValue(
        carInsepctionControl.value.map((value, i) => value ? this.carInspection[i].value : false),
        { emitEvent: false }
      );
    });
    const authenticityTestControl = (this.serviceReservationForm.controls.authenticityTest as FormArray);
    this.subscription = authenticityTestControl.valueChanges.subscribe(checkbox => {
      authenticityTestControl.setValue(
        authenticityTestControl.value.map((value, i) => value ? this.authenticityTest[i].value : false),
        { emitEvent: false }
      );
    });
    const tyreControl = (this.serviceReservationForm.controls.tyre as FormArray);
    this.subscription = tyreControl.valueChanges.subscribe(checkbox => {
      tyreControl.setValue(
        tyreControl.value.map((value, i) => value ? this.tyre[i].value : false),
        { emitEvent: false }
      );
    });
    const brakeControl = (this.serviceReservationForm.controls.brake as FormArray);
    this.subscription = brakeControl.valueChanges.subscribe(checkbox => {
      brakeControl.setValue(
        brakeControl.value.map((value, i) => value ? this.brake[i].value : false),
        { emitEvent: false }
      );
    });
    const undercarriageControl = (this.serviceReservationForm.controls.undercarriage as FormArray);
    this.subscription = undercarriageControl.valueChanges.subscribe(checkbox => {
      undercarriageControl.setValue(
        undercarriageControl.value.map((value, i) => value ? this.undercarriage[i].value : false),
        { emitEvent: false }
      );
    });
    const oilControl = (this.serviceReservationForm.controls.oil as FormArray);
    this.subscription = oilControl.valueChanges.subscribe(checkbox => {
      oilControl.setValue(
        oilControl.value.map((value, i) => value ? this.oil[i].value : false),
        { emitEvent: false }
      );
    });
    const periodicServiceControl = (this.serviceReservationForm.controls.periodicService as FormArray);
    this.subscription = periodicServiceControl.valueChanges.subscribe(checkbox => {
      periodicServiceControl.setValue(
        periodicServiceControl.value.map((value, i) => value ? this.periodicService[i].value : false),
        { emitEvent: false }
      );
    });
    const timingBeltControl = (this.serviceReservationForm.controls.timingBelt as FormArray);
    this.subscription = timingBeltControl.valueChanges.subscribe(checkbox => {
      timingBeltControl.setValue(
        timingBeltControl.value.map((value, i) => value ? this.timingBelt[i].value : false),
        { emitEvent: false }
      );
    });
    const diagnosticControl = (this.serviceReservationForm.controls.diagnostic as FormArray);
    this.subscription = diagnosticControl.valueChanges.subscribe(checkbox => {
      diagnosticControl.setValue(
        diagnosticControl.value.map((value, i) => value ? this.diagnostic[i].value : false),
        { emitEvent: false }
      );
    });
    const technicalExaminationControl = (this.serviceReservationForm.controls.technicalExamination as FormArray);
    this.subscription = technicalExaminationControl.valueChanges.subscribe(checkbox => {
      technicalExaminationControl.setValue(
        technicalExaminationControl.value.map((value, i) => value ? this.technicalExamination[i].value : false),
        { emitEvent: false }
      );
    });
    const climeControl = (this.serviceReservationForm.controls.clime as FormArray);
    this.subscription = climeControl.valueChanges.subscribe(checkbox => {
      climeControl.setValue(
        climeControl.value.map((value, i) => value ? this.clime[i].value : false),
        { emitEvent: false }
      );
    });
    const accumulatorControl = (this.serviceReservationForm.controls.accumulator as FormArray);
    this.subscription = accumulatorControl.valueChanges.subscribe(checkbox => {
      accumulatorControl.setValue(
        accumulatorControl.value.map((value, i) => value ? this.accumulator[i].value : false),
        { emitEvent: false }
      );
    });
    const bodyworkControl = (this.serviceReservationForm.controls.bodywork as FormArray);
    this.subscription = bodyworkControl.valueChanges.subscribe(checkbox => {
      bodyworkControl.setValue(
        bodyworkControl.value.map((value, i) => value ? this.bodywork[i].value : false),
        { emitEvent: false }
      );
    });
    const otherControl = (this.serviceReservationForm.controls.other as FormArray);
    this.subscription = otherControl.valueChanges.subscribe(checkbox => {
      otherControl.setValue(
        otherControl.value.map((value, i) => value ? this.other[i].value : false),
        { emitEvent: false }
      );
    });
  }

  listSetter(): void {
    this.loading = true;
    this.serviceReservation.getUserCars(this.tokenStorage.getUser().id).subscribe(data => {
      this.userCarList = data;
    },  error => {
      this.loading = false;
    });
  }

  dateSetter(){
    this.holidayList=[
      new Date(`${this.dataService.currentYear}. 01. 01.`),
      new Date(`${this.dataService.currentYear}. 03. 15`),
      new Date(`${this.dataService.currentYear}. ${this.dataService.goodFridayMonth}. ${this.dataService.goodFriday}`),
      new Date(`${this.dataService.currentYear}. ${this.dataService.easterMonth}. ${this.dataService.easterDay}`),
      new Date(`${this.dataService.currentYear}. 05. 01.`),
      new Date(`${this.dataService.currentYear}. ${this.dataService.pentecostMonth}. ${this.dataService.pentecostDay}`),
      new Date(`${this.dataService.currentYear}. 08. 20.`),
      new Date(`${this.dataService.currentYear}. 10. 23.`),
      new Date(`${this.dataService.currentYear}. 11. 01.`),
      new Date(`${this.dataService.currentYear}. 12. 24.`),
      new Date(`${this.dataService.currentYear}. 12. 25.`),
      new Date(`${this.dataService.currentYear}. 12. 26.`),
    ]
  }

  checkboxSetter(){
    this.carInspection = this.dataService.carInspection;
    this.tyre = this.dataService.tyre;
    this.brake = this.dataService.brake;
    this.undercarriage = this.dataService.undercarriage;
    this.periodicService = this.dataService.periodicService;
    this.timingBelt = this.dataService.timingBelt;
    this.diagnostic = this.dataService.diagnostic;
    this.technicalExamination  = this.dataService.technicalExamination;
    this.clime = this.dataService.clime;
    this.accumulator = this.dataService.accumulator;
    this.bodywork = this.dataService.bodywork;
    this.other = this.dataService.other;
    this.oil = this.dataService.oil;
    this.authenticityTest = this.dataService.authenticityTest;
  }

  myFilter = (d: Date): boolean => {
    this.dataService.goodFridayAndEasterAndPentecostCalculator(d.getFullYear())
    this.dateSetter()
    const time = d.getTime();
    const day = d.getDay();
    const longWeekCounter = d.getTime() + 86400000;
    let y;
    if(this.holidayList.find(x=>x.getTime() == longWeekCounter)) {
      y = this.holidayList.find(x => x.getTime() == longWeekCounter)
      y = y.getDay()
      if (y == 2) {
        y -= 1;
      } else if (y == 4) {
        y += 1;
      }
    }
    return !this.holidayList.find(x=>x.getTime()==time) && (day !== 0 && day !==6) && (day !== y)
  }

  collectorSetter(){
    this.collector = [''];
    this.collector = this.serviceReservationForm.controls['carInspection'].value.filter(value => !!value)
      .concat(this.serviceReservationForm.controls['authenticityTest'].value.filter(value => !!value))
      .concat(this.serviceReservationForm.controls['tyre'].value.filter(value => !!value))
      .concat(this.serviceReservationForm.controls['brake'].value.filter(value=> !!value))
      .concat(this.serviceReservationForm.controls['undercarriage'].value.filter(value=> !!value))
      .concat(this.serviceReservationForm.controls['oil'].value.filter(value=> !!value))
      .concat(this.serviceReservationForm.controls['periodicService'].value.filter(value=> !!value))
      .concat(this.serviceReservationForm.controls['timingBelt'].value.filter(value=> !!value))
      .concat(this.serviceReservationForm.controls['diagnostic'].value.filter(value=> !!value))
      .concat(this.serviceReservationForm.controls['technicalExamination'].value.filter(value=> !!value))
      .concat(this.serviceReservationForm.controls['clime'].value.filter(value=> !!value))
      .concat(this.serviceReservationForm.controls['accumulator'].value.filter(value=> !!value))
      .concat(this.serviceReservationForm.controls['bodywork'].value.filter(value=> !!value))
      .concat(this.serviceReservationForm.controls['other'].value.filter(value=> !!value));

    this.atLeastOneServiceChecked = false;
    if(this.collector!=''){
      this.atLeastOneServiceChecked = true;
    }
    this.collector = this.collector.join(', ')
    if(!this.atLeastOneServiceChecked){
      this.serviceReservationForm.controls['carInspection'].setErrors({'noServiceChecked': true});
    } else{
      this.serviceReservationForm.controls['carInspection'].setErrors({'noServiceChecked': null});
      this.serviceReservationForm.controls['carInspection'].updateValueAndValidity();
    }
    this.containsOther()
  }

  dateGetter() {
    this.dateFieldValue = false;
    if(this.serviceReservationForm.controls['reservedDate'].value.value===""){
      this.dateFieldValue = true;
    } else{
      this.dateFieldValue = false;
    }
  }

  containsOther(){
    if((this.collector.toString()).includes('egyéb')){
      this.otherChecked = true;
      this.serviceReservationForm.controls['comment'].setValidators(Validators.required);
      this.serviceReservationForm.controls['comment'].updateValueAndValidity();
    }else{
      this.otherChecked = false;
      this.serviceReservationForm.controls['comment'].clearValidators()
      this.serviceReservationForm.controls['comment'].updateValueAndValidity();
    }
  }

  dataServiceSetter(){
    this.dataService.serviceReservationForm = this.serviceReservationForm;
    this.dataService.collector = this.collector;
    for(let item of this.userCarList){
      if(item.carId == this.serviceReservationForm.controls['car'].value){
        this.dataService.selectedCar = item;
      }
    }

  }

  autoFocusOnError() {
    for (const key of Object.keys(this.serviceReservationForm.controls)) {
      if (this.serviceReservationForm.controls[key].invalid) {
        const invalidControl = this.el.nativeElement.querySelector('[formcontrolname="' + key + '"]');
        invalidControl.focus();
        break;
      }
    }
  }

  onSubmit(): void{
    this.submit()
    this.collectorSetter()
    this.dateGetter()
    this.containsOther()
    this.dataServiceSetter()
    this.isSubmitted = true;
    if(!this.serviceReservationForm.valid) {
      this.autoFocusOnError()
      return;
    } else {
      this.serviceReservation.reserveAuthorizedServiceValidation(this.serviceReservationForm.getRawValue(), this.collector, this.dataService.selectedCar.fkCarUser).subscribe(data =>{
        console.log(data)
          this.dataService.user = data;
          this.router.navigate(['auth-service-reservation/verify'])
        },
        err => {
          this.errorMessage = err.error.message;
          this.autoFocusOnError()
        })

    }
  }

}
