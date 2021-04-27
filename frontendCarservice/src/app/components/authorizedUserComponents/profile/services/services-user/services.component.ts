import {ChangeDetectorRef, Component, HostBinding, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {MatSort} from '@angular/material/sort';
import {ProfileService} from '../../../../../services/profile.service';
import {TokenStorageService} from '../../../../../services/token-storage.service';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {MatPaginator} from '@angular/material/paginator';
import {responseServiceData, serviceArray} from '../../../../../models/ServicePage';
import {DialogService} from '../../../../../services/dialog.service';
import {DataService} from '../../../../../services/data.service';

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrls: ['./services.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
    trigger('indicatorRotate', [
      state('collapsed', style({transform: 'rotate(0deg)'})),
      state('expanded', style({transform: 'rotate(180deg)'})),
      transition('expanded <=> collapsed',
        animate('225ms cubic-bezier(0.4,0.0,0.2,1)')
      ),
    ])
  ],
})
export class ServicesComponent implements OnInit {

  expanded: boolean = false;
  @HostBinding('attr.aria-expanded') ariaExpanded = this.expanded;
  @ViewChild(MatPaginator, {static: true}) branchPaginator: MatPaginator;
  @ViewChild('outerSort', {static: true}) sort: MatSort;
  @ViewChildren('innerSort') innerSort: QueryList<MatSort>;
  @ViewChildren('innerTables') innerTables: QueryList<MatTable<serviceArray>>;

  dataSource: MatTableDataSource<responseServiceData>;
  serviceFromDb: responseServiceData[] = [];
  serviceTable: responseServiceData[] = [];
  columnsToDisplay = ['icon', 'Márka', 'Típus', 'Rendszám'];
  innerDisplayedColumns = ['Dátum', 'Kilométer', 'Számla száma', 'Összeg', 'Részletek'];
  expandedElement: responseServiceData | null;

  constructor(private cd: ChangeDetectorRef,
              private profileService: ProfileService,
              private tokenStorageService: TokenStorageService,
              private dialogService: DialogService,
              private dataService: DataService) {
  }

  ngOnInit() {
    this.profileService.getUserServiceData(this.tokenStorageService.getUser().id).subscribe(dbData => {
      this.serviceFromDb = dbData;
      this.serviceFromDb.forEach(data => {
        if (data.serviceList && Array.isArray(data.serviceList) && data.serviceList.length) {
          this.serviceTable = [...this.serviceTable, {...data, serviceList: new MatTableDataSource(data.serviceList)}];
        } else {
          this.serviceTable = [...this.serviceTable, data];
        }
      });
      this.dataSource = new MatTableDataSource(this.serviceTable);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.branchPaginator;
    });

  }

  toggleRow(element: responseServiceData) {
    element.serviceList && (element.serviceList as MatTableDataSource<serviceArray>).data.length ? (this.expandedElement = this.expandedElement === element ? null : element) : null;
    this.cd.detectChanges();
    this.innerTables.forEach((table, index) => (table.dataSource as MatTableDataSource<serviceArray>).sort = this.innerSort.toArray()[index]);
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
