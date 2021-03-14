import { Component, OnInit } from '@angular/core';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../../../../../services/auth.service';
import {VerificationDialogComponent} from '../../../registration/verification-dialog/verification-dialog.component';
import {DataService} from '../../../../../../services/data.service';
import {ExpiredResetPasswordTokenComponent} from '../expired-reset-password-token/expired-reset-password-token.component';
import {PasswordResetDialogComponent} from '../password-reset-dialog/password-reset-dialog.component';

@Component({
  selector: 'app-password-reset-dialog-entry',
  templateUrl: './password-reset-dialog-entry.component.html',
  styleUrls: ['./password-reset-dialog-entry.component.css']
})
export class PasswordResetDialogEntryComponent implements OnInit {

  constructor(private dialog: MatDialog,
              private route: ActivatedRoute,
              private dataService: DataService,
              private authService: AuthService){
  }

  openPasswordResetDialog(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'custom-dialog-container';
    this.dialog.open(PasswordResetDialogComponent, dialogConfig);
  }

  openExpiredTokenDialog(): void {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'custom-dialog-container';
    this.dialog.open(ExpiredResetPasswordTokenComponent, dialogConfig);
  }

  ngOnInit(): void {
    this.route.params.subscribe(params =>{
      let token = params['token'];
      this.authService.checkPasswordResetToken(token).subscribe(result =>{
        if (result==true){
          this.dataService.token = token;
          this.openPasswordResetDialog();
        } else{
          this.openExpiredTokenDialog();
        }
      })
    })

  }

}