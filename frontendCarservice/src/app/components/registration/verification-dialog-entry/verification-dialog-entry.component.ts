import { Component, OnInit } from '@angular/core';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {ActivatedRoute, Router} from '@angular/router';
import {VerificationDialogComponent} from '../verification-dialog/verification-dialog.component';
import {AuthService} from '../../../services/auth.service';
import {DataService} from '../../../services/data.service';
import {DialogService} from '../../../services/dialog.service';

@Component({
  selector: 'app-verification-dialog-entry',
  templateUrl: './verification-dialog-entry.component.html',
  styleUrls: ['./verification-dialog-entry.component.css']
})
export class VerificationDialogEntryComponent implements OnInit {

  constructor(private dialog: MatDialog, private router: Router,
              private route: ActivatedRoute,
              private authService:AuthService,
              private dataService: DataService,
              private dialogService: DialogService){
    this.openDialog();

  }
  openDialog(): void {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'custom-dialog-container';
    console.log('test')
    this.route.params.subscribe(params =>{
      let token = params['token'];
      this.authService.confirmRegistration(token).subscribe(message =>{
          this.dataService.liveToken = true;
          this.dialog.open(VerificationDialogComponent, dialogConfig);
        },
        err => {
          if(err.error.message.includes("This account is already verified or banned")){
            this.dataService.alreadyVerified = true;
          } else {
            this.dataService.expiredVerifyToken = true;
            this.dialog.open(VerificationDialogComponent, dialogConfig);
          }

        }
      );
    });
  }
  ngOnInit(): void {
  }

}
