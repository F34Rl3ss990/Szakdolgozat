import {Component, OnInit,  ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {ProfileService} from '../../../../../services/profile.service';
import {TokenStorageService} from '../../../../../services/token-storage.service';
import {ActivatedRoute} from '@angular/router';
import {carServicePageResponse} from '../../../../../models/carServicePage';
import {DialogService} from '../../../../../services/dialog.service';
import {DataService} from '../../../../../services/data.service';

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
              private tokenStorageService: TokenStorageService,
              private dialogService: DialogService,
              private dataService: DataService) {
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
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  openDetailsMenu(comment, servicesDone){
    this.dataService.comment = comment;
    this.dataService.servicesDone = servicesDone;
    this.dialogService.openDetailsDialog();
  }
}

