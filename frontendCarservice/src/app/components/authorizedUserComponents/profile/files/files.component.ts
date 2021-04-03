import {ChangeDetectorRef, Component, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {MatSort} from '@angular/material/sort';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import {Address} from 'cluster';
import {responseDocument} from '../../../../models/documentPage';

@Component({
  selector: 'app-files',
  templateUrl: './files.component.html',
  styleUrls: ['./files.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class FilesComponent implements OnInit {


  @ViewChild('outerSort', { static: true }) sort: MatSort;
  @ViewChildren('innerSort') innerSort: QueryList<MatSort>;
  @ViewChildren('innerTables') innerTables: QueryList<MatTable<Address>>;

  dataSource: MatTableDataSource<responseDocument>;
  documentData: responseDocument[] = [];
  columnsToDisplay = ['márk', 'típus', 'rendszámtábla'];
  innerDisplayedColumns = ['típus', 'név', 'feltöltés ideje', 'letöltés'];
  expandedElement: responseDocument | null;

  constructor( private cd: ChangeDetectorRef) { }

  ngOnInit(): void {
  }

  toggleRow(element: responseDocument) {
    element.addresses && (element.addresses as MatTableDataSource<Address>).data.length ? (this.expandedElement = this.expandedElement === element ? null : element) : null;
    this.cd.detectChanges();
    this.innerTables.forEach((table, index) => (table.dataSource as MatTableDataSource<Address>).sort = this.innerSort.toArray()[index]);
  }

  applyFilter(filterValue: string) {
    this.innerTables.forEach((table, index) => (table.dataSource as MatTableDataSource<Address>).filter = filterValue.trim().toLowerCase());
  }
}

}
