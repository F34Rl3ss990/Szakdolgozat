import {Component, Inject, OnInit, Renderer2} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {DataService} from '../../services/data.service';

@Component({
  selector: 'app-dynamic-view-dialog',
  templateUrl: './dynamic-view-dialog.component.html',
  styleUrls: ['./dynamic-view-dialog.component.css']
})
export class DynamicViewDialogComponent implements OnInit {


  public viewContent :any;

  constructor(private dialogRef: MatDialogRef<DynamicViewDialogComponent>,
              private renderer: Renderer2,
              private router: Router,
              private http: HttpClient,
              private dataService: DataService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }
  ngOnInit(): void {
    this.http.get(`../../../../../../assets/view/${this.data}`, {responseType: "text"}).subscribe(data=>{
      this.viewContent = data;
    })
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
    this.dataService.token = null;
    this.dialogRef.close();
    this.router.navigate['/home'];
  }
}


