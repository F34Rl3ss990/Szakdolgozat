import {Component, Input, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {VerificationDialogEntryComponent} from '../verification-dialog-entry/verification-dialog-entry.component';
import {DataService} from '../../../services/data.service';

@Component({
  selector: 'app-verification-dialog',
  templateUrl: './verification-dialog.component.html',
  styleUrls: ['./verification-dialog.component.css']
})
export class VerificationDialogComponent implements OnInit {

  isVerified: boolean;

  constructor(private dialogRef: MatDialogRef<VerificationDialogComponent>,
              private renderer: Renderer2,
              private dataService: DataService) {
  }
  ngOnInit(): void {
    this.renderer.listen(document, 'keydown', event => {
      if (event.key === 'Escape') {
        this.close();
      }
    });
    this.isVerified = this.dataService.sharedData;
  }

  close() {
    this.dialogRef.close();
  }
}
