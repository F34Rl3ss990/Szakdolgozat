import { Component, OnInit } from '@angular/core';
import {DataService} from '../../../../services/data.service';
import {FormGroup} from '@angular/forms';
import {Location} from '@angular/common';
import {MatDialog} from '@angular/material/dialog';
import {DialogService} from '../../../../services/dialog.service';
import {ServiceReservationService} from '../../../../services/service-reservation.service';
import {TokenStorageService} from '../../../../services/token-storage.service';

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
              private serviceReservation: ServiceReservationService,
              private tokenStorageService: TokenStorageService) { }
  ngOnInit(): void {
    this.serviceForm = this.dataService.serviceReservationForm;
    this.collector = this.dataService.collector;
  }

  backClicked() {
    this.location.back();
  }

  verify(){

    this.serviceReservation.reserveUnauthorizedService(this.serviceForm.getRawValue(), this.collector).subscribe(data =>{
        this.dialogService.openSuccessfullyReservedUnauthorizedService();
      }, error => {

    })
  }

}
