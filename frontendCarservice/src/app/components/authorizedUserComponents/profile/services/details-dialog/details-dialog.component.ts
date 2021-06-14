import {Component, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {DataService} from '../../../../../services/data.service';

@Component({
  selector: 'app-details-dialog',
  templateUrl: './details-dialog.component.html',
  styleUrls: ['./details-dialog.component.css']
})
export class DetailsDialogComponent implements OnInit {

  comment;
  servicesDone;

  constructor(private dialogRef: MatDialogRef<DetailsDialogComponent>,
              private renderer: Renderer2,
              private dataService: DataService) { }

  ngOnInit(): void {
    this.renderer.listen(document, 'keydown', event => {
      if (event.key === 'Escape') {
        this.close();
      }
    });
    this.comment = this.dataService.comment;
    this.servicesDone = this.dataService.servicesDone;
  }

  close() {
    this.dataService.comment = null;
    this.dataService.servicesDone = null;
    this.dialogRef.close();
  }
}
