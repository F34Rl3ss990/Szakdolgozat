import { Injectable } from '@angular/core';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {AuthService} from './auth.service';
import {TokenStorageService} from './token-storage.service';
import {LoginDialogComponent} from '../components/global/authorization-authentication/login/login-dialog/login-dialog.component';
import {RegisterComponent} from '../components/global/authorization-authentication/registration/register/register.component';
import {RegistrationSuccessfulComponent} from '../components/global/authorization-authentication/registration/registration-successful/registration-successful.component';
import {PasswordTokenSentComponent} from '../components/global/authorization-authentication/login/passwordReset/password-token-sent/password-token-sent.component';
import {PasswordSuccessfullyChangedComponent} from '../components/global/authorization-authentication/login/passwordReset/password-successfully-changed/password-successfully-changed.component';
import {PasswordResetDialogComponent} from '../components/global/authorization-authentication/login/passwordReset/password-reset-dialog/password-reset-dialog.component';
import {ExpiredResetPasswordTokenComponent} from '../components/global/authorization-authentication/login/passwordReset/expired-reset-password-token/expired-reset-password-token.component';
import {PasswordResetTokenSenderComponent} from '../components/global/authorization-authentication/login/passwordReset/password-reset-token-sender/password-reset-token-sender.component';

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  isLoggedIn = false;
  constructor(private dialog: MatDialog) { }

  openRegistrationDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.panelClass = 'my-dialog';
    dialogConfig.autoFocus = false;

    this.dialog.open(RegisterComponent, dialogConfig);

  }
  openLoginDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.panelClass = 'login-dialog';
    dialogConfig.autoFocus = false;

    this.dialog.open(LoginDialogComponent, dialogConfig);
  }

  openPasswordResetTokenSenderDialog(){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.panelClass = 'my-dialog';
    dialogConfig.autoFocus = false;

    this.dialog.open(PasswordResetTokenSenderComponent, dialogConfig);
  }

  openSuccessfulRegisterDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'custom-dialog-container';
    this.dialog.open(RegistrationSuccessfulComponent, dialogConfig);
  }

  openPasswordTokenSent(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'custom-dialog-container';
    this.dialog.open(PasswordTokenSentComponent, dialogConfig);
  }

  openSuccessPasswordChange(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'custom-dialog-container';
    this.dialog.open(PasswordSuccessfullyChangedComponent, dialogConfig);
  }

  openPasswordResetDialog(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'my-dialog';
    this.dialog.open(PasswordResetDialogComponent, dialogConfig);
  }

  openExpiredTokenDialog(): void {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'custom-dialog-container';
    this.dialog.open(ExpiredResetPasswordTokenComponent, dialogConfig);
  }

  openSuccessfullyReservedUnauthorizedService(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'custom-dialog-container';
    this.dialog.open(PasswordTokenSentComponent, dialogConfig);
  }
}
