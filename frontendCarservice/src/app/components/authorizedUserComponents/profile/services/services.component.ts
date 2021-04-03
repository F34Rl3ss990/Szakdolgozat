import {Component, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {MatSort} from '@angular/material/sort';
import {userCarList} from '../../../../models/userCarList';
import {ProfileService} from '../../../../services/profile.service';
import {ServiceReservationService} from '../../../../services/service-reservation.service';
import {TokenStorageService} from '../../../../services/token-storage.service';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {Address} from 'cluster';

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrls: ['./services.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class ServicesComponent implements OnInit {

  @ViewChild('outerSort', { static: true }) sort: MatSort;
  @ViewChildren('innerSort') innerSort: QueryList<MatSort>;
  @ViewChildren('innerTables') innerTables: QueryList<MatTable<Address>>;

  loading: boolean;
  dataSource: userCarList[];
  columnsToDisplay = ['márk', 'típus', 'rendszámtábla'];
  innerDisplayedColumns = ['típus', 'név', 'feltöltés ideje', 'letöltés'];
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
