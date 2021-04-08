import {ChangeDetectorRef, Component, HostBinding, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {MatSort} from '@angular/material/sort';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import {documentArray, responseDocument} from '../../../../models/documentPage';
import {ProfileService} from '../../../../services/profile.service';
import {TokenStorageService} from '../../../../services/token-storage.service';
import {ActivatedRoute, Router} from '@angular/router';
import { MatPaginator } from '@angular/material/paginator';

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
    trigger('indicatorRotate', [
      state('collapsed', style({transform: 'rotate(0deg)'})),
      state('expanded', style({transform: 'rotate(180deg)'})),
      transition('expanded <=> collapsed',
        animate('225ms cubic-bezier(0.4,0.0,0.2,1)')
      ),
    ])
  ],
})
export class FilesComponent implements OnInit {


  expanded: boolean = false;
  @HostBinding('attr.aria-expanded') ariaExpanded = this.expanded;
  @ViewChild(MatPaginator, { static: true }) branchPaginator: MatPaginator;
  @ViewChild('outerSort', { static: true }) sort: MatSort;
  @ViewChildren('innerSort') innerSort: QueryList<MatSort>;
  @ViewChildren('innerTables') innerTables: QueryList<MatTable<documentArray>>;

  dataSource: MatTableDataSource<responseDocument>;
  documentFromDb: responseDocument[] = [];
  documentTable: responseDocument[] = [];
  columnsToDisplay = ['icon', 'Márka', 'Típus', 'Rendszám', 'Dátum', 'Kilométer', 'Letöltés'];
  innerDisplayedColumns = ['Típus', 'Név', 'Méret', 'Letöltés'];
  expandedElement: responseDocument | null;

  constructor( private cd: ChangeDetectorRef,
               private profileService: ProfileService,
               private tokenStorageService: TokenStorageService,
               private router: Router,
               private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params.id == null) {
        this.profileService.getUserDocuments(this.tokenStorageService.getUser().id).subscribe(dbData =>{
          this.documentFromDb = dbData;
          this.initTable()
        })
      } else {
        this.profileService.getCarDocuments(params.id, this.tokenStorageService.getUser().id).subscribe(dbData =>{
        this.documentFromDb = dbData;
        this.initTable()
        })
      }
    })

  }
  initTable(){
    this.documentFromDb.forEach(data => {
      if (data.documentList && Array.isArray(data.documentList) && data.documentList.length) {
        this.documentTable = [...this.documentTable, {...data, documentList: new MatTableDataSource(data.documentList)}];
      } else {
        this.documentTable = [...this.documentTable, data];
      }
    });
    this.dataSource = new MatTableDataSource(this.documentTable);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.branchPaginator;
  }


  toggleRow(element: responseDocument) {
    element.documentList && (element.documentList as MatTableDataSource<documentArray>).data.length ? (this.expandedElement = this.expandedElement === element ? null : element) : null;
    this.cd.detectChanges();
    this.innerTables.forEach((table, index) => (table.dataSource as MatTableDataSource<documentArray>).sort = this.innerSort.toArray()[index]);
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  downloadFile(documentId): void{
    window.open(`http://localhost:8080/api/test/document/files/${documentId}`)
  }

  downloadZip(documentId:[], event: any): void{
    event.stopPropagation();
    var string = "";
    for(var i = 0; documentId.length>i;i++){
      if(string === ""){
        string += "?id=" + documentId[i];
      }else{
        string += "&id=" + documentId[i];
      }
    }
    window.open(`http://localhost:8080/api/test/document/zip-download${string}`)
  }
}
