import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-service-reservation',
  templateUrl: './service-reservation.component.html',
  styleUrls: ['./service-reservation.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ServiceReservationComponent implements OnInit {

  serviceReservationForm: FormGroup;
  minDate: Date;
  data: boolean;

  checkboxes = [{
    name: 'Value 1',
    value: 'value-1'
  }, {
    name: 'Value 2',
    value: 'value-2'
  }, {
    name: 'Value 3',
    value: 'value-3'
  }];


  constructor(private fb: FormBuilder) {
    const currentYear = Date.now();
    this.minDate = new Date(currentYear);
    this.createForm();
  }

  ngOnInit(): void {
  }


  createForm() {
    const patternEmail = '^[a-zA-Z0-9_.+-]+@+[a-zA-Z-09-]+\\.[a-zA-Z0-9-.]{2,}';
    this.serviceReservationForm = this.fb.group({
      firstName: this.fb.control('', {}),
      lastName: this.fb.control('', {}),
      email: this.fb.control('', {
        validators: [Validators.pattern(patternEmail), Validators.required]
      }),
      phoneNumber: this.fb.control('',{}),
      brand: this.fb.control('',{}),
      type: this.fb.control('', {}),
      yearOfManufacture: this.fb.control('', {}),
      engineType: this.fb.control('', {}),
      engineNumber: this.fb.control('', {}),
      chassisNumber: this.fb.control('', {}),
      licensePlateNumber: this.fb.control('', {}),
      foreignCountryPlate: this.fb.control('', {}),
      checkboxes: this.fb.array(this.checkboxes.map(x => false)),
      billingToCompany: this.fb.control('', {}),
      billingSameAsUserData: this.fb.control('', {}),
      billingName: this.fb.control('', {}),
      billingPhoneNumber: this.fb.control('', {}),
      billingEmail: this.fb.control('', {}),
      billingZipCode: this.fb.control('', {}),
      billingTown: this.fb.control('', {}),
      billingStreet: this.fb.control('', {}),
      billingOtherAddressType: this.fb.control('', {}),
      billingTax: this.fb.control('', {}),
      billingForeignCountryTax: this.fb.control('', {}),
      datePicker: this.fb.control('', {})
    }, {updateOn: 'submit'});
  }

  onSubmit(): void{

  }
}
