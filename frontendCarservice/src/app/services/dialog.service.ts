import { Injectable } from '@angular/core';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {RegisterComponent} from '../components/registration/register/register.component';
import {AuthService} from './auth.service';
import {LoginDialogComponent} from '../components/login/login-dialog/login-dialog.component';
import {TokenStorageService} from './token-storage.service';
import {PasswordResetComponent} from '../components/login/password-reset/password-reset.component';
import {RegistrationSuccessfulComponent} from '../components/registration/registration-successful/registration-successful.component';
import {PasswordTokenSentComponent} from '../components/login/password-token-sent/password-token-sent.component';

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
    dialogConfig.autoFocus = false;
    dialogConfig.panelClass = 'my-dialog';

    this.dialog.open(LoginDialogComponent, dialogConfig);
  }

  openPasswordResetDialog(){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = false;
    dialogConfig.panelClass = 'my-dialog';

    this.dialog.open(PasswordResetComponent, dialogConfig);
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
}
