import {Component, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {DataService} from '../../../../services/data.service';

@Component({
  selector: 'app-successfully-reserved-unauthorized-service',
  templateUrl: './successfully-reserved-unauthorized-service.component.html',
  styleUrls: ['./successfully-reserved-unauthorized-service.component.css']
})
export class SuccessfullyReservedUnauthorizedServiceComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<SuccessfullyReservedUnauthorizedServiceComponent>,
              private renderer: Renderer2,
              private router: Router,
              private dataService: DataService) {
  }
  ngOnInit(): void {
    this.renderer.listen(document, 'keydown', event => {
      if (event.key === 'Escape') {
        this.close();
      }
    });
  }

  close() {
    this.dataService.user = null
    this.dataService.selectedCar = null;
    this.dataService.serviceReservationForm = null;
    this.dataService.collector = null;
    this.dataService.billingToCompany = null;
    this.dataService.foreignCountryPlate = null;
    this.dataService.foreignCountryTax = null;
    this.dataService.data = null;
    this.dialogRef.close();
    this.router.navigate(['home']);
  }

}
