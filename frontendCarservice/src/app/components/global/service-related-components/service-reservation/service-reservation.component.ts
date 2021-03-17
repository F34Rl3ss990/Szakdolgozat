import {Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DataService} from '../../../../services/data.service';
import {ServiceReservationService} from '../../../../services/service-reservation.service';
import {Subscription} from 'rxjs';
import {DialogService} from '../../../../services/dialog.service';
import {Router} from '@angular/router';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';
import {MAT_MOMENT_DATE_FORMATS, MomentDateAdapter} from '@angular/material-moment-adapter';
import {serviceableCarList} from '../../../../models/serviceableCarList';
import {ErrorMatcherDirective} from '../../../validators/error-matcher.directive';

@Component({
  selector: 'app-service-reservation',
  templateUrl: './service-reservation.component.html',
  styleUrls: ['./service-reservation.component.css'],
})
export class ServiceReservationComponent implements OnInit {

  serviceReservationForm: FormGroup;
  minDate: Date;
  data: boolean = false;
  billingToCompany = false;
  billingSameAsUserData = true;
  serviceIsChecked: boolean;
  billingForeignCountryTax = false;
  otherChecked = false;
  atLeastOneServiceChecked = false;
  collector: any = [];
  subscription: Subscription;
  submittedValue: any;
  dateFieldValue: boolean;
  isSubmitted: boolean;
  errorMessage = ''
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
  errorMatcher = new ErrorMatcherDirective();

  @ViewChild('hideIt') hideIt: ElementRef;

  carInspection = [{
    name: 'Minimalista csomag',
    value: 'minimalista csomag'
  }, {
    name: 'Közepes csomag',
    value: 'közepes csomag'
  }, {
    name: 'Maximalista csomag',
    value: 'maximalista csomag'
  }]

  authenticityTest = [{
    name: 'Eredetiség vizsgálat',
    value: 'eredetiség vizsgálat'
  }]

  tyre = [{
    name: 'Gumicsere',
    value: 'gumicsere'
  }, {
    name: 'Kerékcentírozás',
    value: 'kerékcentírozás'
  }, {
    name: 'Defektjavítás',
    value: 'defektjavítás'
  },{
    name: 'Egyéb',
    value: 'gumi egyéb'
  }];

  brake = [{
    name: 'Fékbetét csere',
    value: 'fékbetét csere'
  }, {
    name: 'Féktárcsa csere',
    value: 'féktárcsa csere'
  }, {
    name: 'Fékfolyadék csere',
    value: 'fékfolyadék csere'
  }, {
    name: 'Fékrendszer csere',
    value: 'fékrendszer csere'
  }, {
    name: 'Egyéb',
    value: 'fék egyéb'
  }
  ];

  undercarriage = [{
    name: 'Futóműellenőrzés',
    value: 'futóműellenőrzés'
  }, {
    name: 'Futóműállítás',
    value: 'futóműállítás'
  }, {
    name: 'Egyéb',
    value: 'futómű egyéb'
  }];

  oil = [{
    name: 'Olajcsere',
    value: 'olajcsere'
  }]

  periodicService = [{
    name: 'Kisszervíz',
    value: 'kiszervíz'
  }, {
    name: 'Nagyszervíz',
    value: 'nagyszervíz'
  }];

  timingBelt = [{
    name: 'Vezérműszíjcsere',
    value: 'vezérműszíjcsere'
  }];

  diagnostic = [{
    name: 'Hibakód kiolvasás',
    value: 'hibakód kiolvasás'
  }]

  technicalExamination = [{
    name: 'Műszaki vizsgáztatás',
    value: 'műszaki vizsgáztatás'
  }, {
    name: 'Műszaki vizsga felkészítés',
    value: 'műszaki vizsga felkészítés'
  }];

  clime = [{
    name: 'Klímatöltés',
    value: 'klmíatöltés'
  }, {
    name: 'Klíma fertőtlenítés',
    value: 'klíma fertőtlenítés'
  }, {
    name: 'Nyomás ellenőrzés',
    value: 'klíma nyomás ellenőrzés'
  }, {
    name: 'Egyéb',
    value: 'klíma egyéb'
  }]

  accumulator = [{
    name: 'Akkumulátor csere',
    value: 'akkumulátor csere'
  }, {
    name: 'Egyéb',
    value: 'akkumulátor egyéb'
  }]

  bodywork = [{
    name: 'Karosszéria javítás',
    value: 'karosszéria javítás'
  }, {
    name: 'Fényezés',
    value: 'fényezés'
  }]

  other = [{
    name: 'Egyéb',
    value: ' egyéb'
  }]

  holidayList=[
  ]

  submit(){
    this.hideIt.nativeElement.focus();
  }

  constructor(private fb: FormBuilder,
              private dataService: DataService,
              private serviceReservation: ServiceReservationService,
              private el: ElementRef,
              private dialogService: DialogService,
              private router: Router) {
    this.listSetter();
    const currentYear = Date.now();
    this.minDate = new Date(currentYear);
    this.createForm();
    if(this.dataService.serviceReservationForm != undefined) {
      this.serviceReservationForm = this.dataService.serviceReservationForm;
      this.brandSet.add(this.serviceReservationForm.controls['brand'].value)
      this.typeSet.add(this.serviceReservationForm.controls['type'].value)
      this.yearOfManufactureSet.add(this.serviceReservationForm.controls['yearOfManufacture'].value)
      this.engineTypeSet.add(this.serviceReservationForm.controls['engineType'].value)
      this.billingToCompany = this.dataService.billingToCompany;
      this.foreignCountryPlate = this.dataService.foreignCountryPlate;
      this.billingForeignCountryTax = this.dataService.foreignCountryTax;
      this.data = this.dataService.data;
    }
  }

   ngOnInit(): void {
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

  createForm() {
    const patternEmail = '^[a-zA-Z0-9_.+-]+@+[a-zA-Z-09-]+\\.[a-zA-Z0-9-.]{2,}';
    this.serviceReservationForm = this.fb.group({
      name: this.fb.control('',  {updateOn: 'blur', validators: [Validators.required, Validators.pattern(this.dataService.negatedSet)]}),
      email: this.fb.control('', {updateOn: 'blur', validators: [Validators.pattern(patternEmail), Validators.required]}),
      phoneNumber: this.fb.control('',{updateOn: 'blur', validators: [Validators.required, Validators.minLength(8), Validators.maxLength(14), Validators.pattern('[0-9]+')]}),
      brand: this.fb.control('',{updateOn: 'blur', validators: [Validators.required]}),
      type: this.fb.control('',{updateOn: 'blur', validators: [Validators.required]}),
      yearOfManufacture: this.fb.control('',{updateOn: 'blur', validators: [Validators.required]}),
      engineType: this.fb.control('',{updateOn: 'blur', validators: [Validators.required]}),
      mileage: this.fb.control('',{updateOn: 'blur', validators: [Validators.max(999999), Validators.pattern('[0-9]+')]}),
      engineNumber: this.fb.control('', {updateOn: 'blur', validators: [Validators.pattern('^[A-Za-z0-9]+$')]}),
      chassisNumber: this.fb.control('', {updateOn: 'blur', validators: [Validators.pattern('^[A-Za-z0-9]+$'), Validators.minLength(17), Validators.maxLength(17)]}),
      licensePlateNumber: this.fb.control('', {updateOn: 'blur', validators: [Validators.pattern('^[a-zA-Z]{3}[-][0-9]{3}$|[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{2}$|[/p/P][-][0-9]{5}$|^[a-zA-z]{3}[0-9]{5}')]}),
      foreignCountryPlate: this.fb.control('', {}),
      reservedDate: this.fb.control({value: ''}, {updateOn: 'blur', validators: Validators.required}),
      billingToCompany: this.fb.control('', {}),
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
      data: this.fb.control('', {}),
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

  licensePlateValidator(event){
        if(!event.checked) {
          this.serviceReservationForm.controls['licensePlateNumber'].setValidators(Validators.pattern('^[a-zA-Z]{3}[-][0-9]{3}$|[a-zA-Z]{2}[-][0-9]{2}[-][0-9]{2}$|[/p/P][-][0-9]{5}$|^[a-zA-z]{3}[0-9]{5}'))
          this.serviceReservationForm.controls['licensePlateNumber'].updateValueAndValidity();
        } else {
          this.serviceReservationForm.controls['licensePlateNumber'].clearValidators();
          this.serviceReservationForm.controls['licensePlateNumber'].updateValueAndValidity();
        }
      }

  taxNumberValidator(event){
    if(!event.checked) {
      this.serviceReservationForm.controls['billingTax'].setValidators([Validators.pattern('^[0-9]{8}[-][0-9][-][0-9]{2}$'), Validators.required])
      this.serviceReservationForm.controls['billingTax'].updateValueAndValidity();
    } else {
      this.serviceReservationForm.controls['billingTax'].clearValidators();
      this.serviceReservationForm.controls['billingTax'].setValidators(Validators.required)
      this.serviceReservationForm.controls['billingTax'].updateValueAndValidity();
    }
  }

  taxNumberRequiredSetter(event){
    if(event.checked) {
      this.serviceReservationForm.controls['billingTax'].setValidators([Validators.pattern('^[0-9]{8}[-][0-9][-][0-9]{2}$'), Validators.required])
      this.serviceReservationForm.controls['billingTax'].updateValueAndValidity();
    } else {
      this.serviceReservationForm.controls['billingTax'].clearValidators();
      this.serviceReservationForm.controls['billingTax'].updateValueAndValidity();
    }
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

  myFilter2 = (d: Date): boolean => {
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


  billingDataSetter(){
    if(this.billingSameAsUserData){
      this.serviceReservationForm.controls['billingName'].setValue(this.serviceReservationForm.value.name);
      this.serviceReservationForm.controls['billingName'].disable();
      this.serviceReservationForm.controls['billingPhoneNumber'].setValue(this.serviceReservationForm.value.phoneNumber);
      this.serviceReservationForm.controls['billingPhoneNumber'].disable();
      this.serviceReservationForm.controls['billingEmail'].setValue(this.serviceReservationForm.value.email);
      this.serviceReservationForm.controls['billingEmail'].disable();

    } else{
      this.serviceReservationForm.controls['billingName'].setValue('');
      this.serviceReservationForm.controls['billingName'].enable();
      this.serviceReservationForm.controls['billingPhoneNumber'].setValue('');
      this.serviceReservationForm.controls['billingPhoneNumber'].enable();
      this.serviceReservationForm.controls['billingEmail'].setValue('');
      this.serviceReservationForm.controls['billingEmail'].enable();
    }
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

  engineAndChassisSetterIfNull(){
    if(this.serviceReservationForm.controls['chassisNumber'].value==''){
      this.serviceReservationForm.controls['chassisNumber'].setValue(null)
    }
    if(this.serviceReservationForm.controls['engineNumber'].value ==''){
      this.serviceReservationForm.controls['engineNumber'].setValue(null)
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

  errCleaner(){
    this.serviceReservationForm.controls['billingTax'].setErrors({'badTaxPattern' : null});
    this.serviceReservationForm.controls['licensePlateNumber'].setErrors({'badLicensePlatePattern' : null});
    this.serviceReservationForm.controls['billingTax'].updateValueAndValidity();
    this.serviceReservationForm.controls['licensePlateNumber'].updateValueAndValidity();
  }

  dataServiceSetter(){
    this.dataService.serviceReservationForm = this.serviceReservationForm;
    this.dataService.collector = this.collector;
    this.dataService.billingToCompany = this.billingToCompany;
    this.dataService.foreignCountryPlate = this.foreignCountryPlate;
    this.dataService.foreignCountryTax = this.billingForeignCountryTax;
    this.dataService.data = this.data;
  }

  onSubmit(): void{
    this.submit()
    this.collectorSetter()
    this.dateGetter()
    this.containsOther()
    this.engineAndChassisSetterIfNull()
    this.dataServiceSetter()
    this.isSubmitted = true;
    this.errCleaner()
    if(!this.serviceReservationForm.valid) {
      this.autoFocusOnError()
      return;
    } else {
    this.serviceReservation.reserveUnauthorizedServiceValidation(this.serviceReservationForm.getRawValue(), this.collector, this.foreignCountryPlate, this.billingToCompany).subscribe(data =>{
      console.log(data)
      this.router.navigate(['/verif'])
    },
      err => {
      console.log(err)
        this.errorMessage = err.error.message;
      console.log(err.error.message)
        if(this.errorMessage.includes('Tax number is inc')){
          this.serviceReservationForm.controls['billingTax'].setErrors({'badTaxPattern' : true});
        }
        if(this.errorMessage.includes('LicensePlate is incor')){
          this.serviceReservationForm.controls['licensePlateNumber'].setErrors({'badLicensePlatePattern' : true});
        }
        this.autoFocusOnError()
      })

  }
  }
}
