import {Component, OnInit,  ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {ProfileService} from '../../../../../services/profile.service';
import {TokenStorageService} from '../../../../../services/token-storage.service';
import {ActivatedRoute} from '@angular/router';
import {carServicePageResponse} from '../../../../../models/carServicePage';

@Component({
  selector: 'app-services-car',
  templateUrl: './services-car.component.html',
  styleUrls: ['./services-car.component.css']
})
export class ServicesCarComponent implements OnInit {

  @ViewChild(MatPaginator, {static: true}) branchPaginator: MatPaginator;
  @ViewChild('outerSort', {static: true}) sort: MatSort;

  dataSource: MatTableDataSource<carServicePageResponse>;
  columnsToDisplay = ['icon', 'Márka', 'Típus', 'Rendszám'];


  constructor(private profileService: ProfileService,
              private route: ActivatedRoute,
              private tokenStorageService: TokenStorageService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.profileService.getCarServiceData(params.id, this.tokenStorageService.getUser().id).subscribe(dbData => {
        this.dataSource = new MatTableDataSource(dbData);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.branchPaginator;
      })
    })
  }
  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }
}

