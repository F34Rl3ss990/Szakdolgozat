import {Component, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {DataService} from '../../../../../services/data.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-verification-dialog',
  templateUrl: './verification-dialog.component.html',
  styleUrls: ['./verification-dialog.component.css']
})
export class VerificationDialogComponent implements OnInit {

  liveToken: boolean;
  isVerified: boolean;
  expiredToken: boolean;

  constructor(private dialogRef: MatDialogRef<VerificationDialogComponent>,
              private renderer: Renderer2,
              private dataService: DataService,
              private router: Router) {
  }
  ngOnInit(): void {
    this.renderer.listen(document, 'keydown', event => {
      if (event.key === 'Escape') {
        this.close();
      }
    });
    this.liveToken = this.dataService.liveToken;
    this.expiredToken = this.dataService.expiredVerifyToken;
  }

  close() {
    this.dataService.liveToken = null;
    this.dataService.expiredVerifyToken = null;
    this.dialogRef.close();
    this.router.navigate(['home'])
  }
}
