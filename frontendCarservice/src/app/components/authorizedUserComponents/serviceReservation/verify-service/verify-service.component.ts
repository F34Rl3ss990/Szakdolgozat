import {Component, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {DataService} from '../../../../services/data.service';
import {Location} from '@angular/common';
import {DialogService} from '../../../../services/dialog.service';
import {ServiceReservationService} from '../../../../services/service-reservation.service';
import {userCarList} from '../../../../models/userCarList';
import {user} from '../../../../models/user';

@Component({
  selector: 'app-verify-service',
  templateUrl: './verify-service.component.html',
  styleUrls: ['./verify-service.component.css']
})
export class VerifyServiceComponent implements OnInit {

  serviceForm: FormGroup;
  selectedCar: userCarList;
  collector: [];
  user: user;
  selectedCarObj;

  constructor(private dataService: DataService,
              private location: Location,
              private dialogService: DialogService,
              private serviceReservation: ServiceReservationService) {
  }

  ngOnInit(): void {
    this.serviceForm = this.dataService.serviceReservationForm;
    this.collector = this.dataService.collector;
    this.selectedCar = this.dataService.selectedCar;
    this.user = this.dataService.user;
    this.selectedCarObj = this.dataService.selectedCarObj;
    console.log(this.user)
    console.log(this.serviceForm)
    console.log(this.user.name)
    console.log(this.selectedCar)
    console.log(this.selectedCarObj)
  }

  backClicked() {
    this.location.back();
  }

  verify() {
    this.serviceReservation.reserveAuthorizedService(this.serviceForm.getRawValue(), this.collector).subscribe(data => {
      this.dialogService.openSuccessDialog('successfully-reserved-service.html');
    }, error => {
          console.log(error);
    });
  }


}
