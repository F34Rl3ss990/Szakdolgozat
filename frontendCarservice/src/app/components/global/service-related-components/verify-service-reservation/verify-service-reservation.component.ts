import { Component, OnInit } from '@angular/core';
import {DataService} from '../../../../services/data.service';
import {FormGroup} from '@angular/forms';
import {Location} from '@angular/common';
import {MatDialog} from '@angular/material/dialog';
import {DialogService} from '../../../../services/dialog.service';
import {ServiceReservationService} from '../../../../services/service-reservation.service';

@Component({
  selector: 'app-verify-service-reservation',
  templateUrl: './verify-service-reservation.component.html',
  styleUrls: ['./verify-service-reservation.component.css']
})
export class VerifyServiceReservationComponent implements OnInit {

  serviceForm: FormGroup;
  collector: [];

  constructor(private dataService: DataService,
              private location: Location,
              private dialogService: DialogService,
              private serviceReservation: ServiceReservationService) { }
  ngOnInit(): void {
    this.serviceForm = this.dataService.serviceReservationForm;
    this.collector = this.dataService.collector;
    console.log(this.serviceForm)
  }

  backClicked() {
   // this.dataService.serviceReservationForm = this.serviceForm;
   // this.dataService.collector = this.collector;
    this.location.back();
  }

  verify(){
    this.serviceReservation.reserveUnauthorizedService(this.serviceForm.getRawValue(), this.collector).subscribe(data =>{
        this.dialogService.openSuccessfullyReservedUnauthorizedService();
      }, error => {

    })
  }

}
