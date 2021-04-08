import {Component, OnInit, ViewChild} from '@angular/core';
import {ProfileService} from '../../../../../services/profile.service';
import {userCarList} from '../../../../../models/userCarList';
import {ServiceReservationService} from '../../../../../services/service-reservation.service';
import {TokenStorageService} from '../../../../../services/token-storage.service';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-car-data',
  templateUrl: './car-data.component.html',
  styleUrls: ['./car-data.component.css']
})
export class CarDataComponent implements OnInit {
  @ViewChild(MatSort) sort: MatSort;

  loading: boolean;
  dataSource: userCarList[];
  displayedColumns: string[] = ['márka', 'típus', 'gyártási év', 'motor típus', 'aktuális kilométer', 'rendszám', 'actions'];
  cars;

  constructor(private profileService: ProfileService,
              private serviceReservation: ServiceReservationService,
              private tokenStorage: TokenStorageService) {
  }

  ngOnInit(): void {
    this.getList();
  }

  private getList() {
    this.loading = true;
    this.serviceReservation.getUserCars(this.tokenStorage.getUser().id).subscribe(data => {
      this.dataSource = data;
      this.cars = new MatTableDataSource(this.dataSource);
      this.cars.sort = this.sort;
      this.loading = false;
    }, error => {
      this.loading = false;
    });
  }

}
