import {Injectable} from '@angular/core';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {LoginDialogComponent} from '../components/global/authorization-authentication/login/login-dialog/login-dialog.component';
import {RegisterComponent} from '../components/global/authorization-authentication/registration/register/register.component';
import {PasswordResetDialogComponent} from '../components/global/authorization-authentication/login/passwordReset/password-reset-dialog/password-reset-dialog.component';
import {ExpiredResetPasswordTokenComponent} from '../components/global/authorization-authentication/login/passwordReset/expired-reset-password-token/expired-reset-password-token.component';
import {PasswordResetTokenSenderComponent} from '../components/global/authorization-authentication/login/passwordReset/password-reset-token-sender/password-reset-token-sender.component';
import {DynamicViewDialogComponent} from '../components/dynamic-view-dialog/dynamic-view-dialog.component';
import {DetailsDialogComponent} from '../components/authorizedUserComponents/profile/services/details-dialog/details-dialog.component';
import {Router} from '@angular/router';
import {BadPasswordResetTokenComponent} from '../components/global/authorization-authentication/login/passwordReset/bad-password-reset-token/bad-password-reset-token.component';


@Injectable({
  providedIn: 'root'
})
export class DialogService {

  isLoggedIn = false;

  constructor(private dialog: MatDialog,
              private router: Router) {
  }

  openSuccessDialog(viewName: string) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'custom-dialog-container';
    dialogConfig.data = viewName;
    const dialogRef = this.dialog.open(DynamicViewDialogComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(result => {
      this.router.navigate(['home']);
    });
  }


  openDetailsDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'custom-dialog-container';
    this.dialog.open(DetailsDialogComponent, dialogConfig);
  }

  openLoginDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.panelClass = 'login-dialog';
    dialogConfig.autoFocus = false;
    this.dialog.open(LoginDialogComponent, dialogConfig);
  }

  openPasswordResetTokenSenderDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.panelClass = 'my-dialog';
    dialogConfig.autoFocus = false;
    this.dialog.open(PasswordResetTokenSenderComponent, dialogConfig);
  }

  openPasswordResetDialog(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'my-dialog';
    this.dialog.open(PasswordResetDialogComponent, dialogConfig);
  }

  openBadPasswordResetTokenDialog(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'my-dialog';
    this.dialog.open(BadPasswordResetTokenComponent, dialogConfig);
  }

  openRegistrationDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.panelClass = 'my-dialog';
    dialogConfig.autoFocus = false;
    this.dialog.open(RegisterComponent, dialogConfig);
  }

  openExpiredTokenDialog(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'custom-dialog-container';
    this.dialog.open(ExpiredResetPasswordTokenComponent, dialogConfig);
  }
}
