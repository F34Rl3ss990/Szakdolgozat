import { Component, OnInit } from '@angular/core';
import {SlimLoadingBarService} from 'ng2-slim-loading-bar';
import {Event, Router} from '@angular/router';
import {TokenStorageService} from '../../services/token-storage.service';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {RegisterComponent} from '../register/register.component';
import {LoginComponent} from '../login/login.component';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})
export class NavigationBarComponent implements OnInit {

  private roles: string[];
  isLoggedIn = false;
  isLoginFailed = false;
  showAdminBoard = false;
  showModeratorBoard = false;
  username: string;
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(private loadingBar: SlimLoadingBarService, private router: Router,
              private tokenStorageService: TokenStorageService,
              private dialog: MatDialog, private authService: AuthService) {
  }

  ngOnInit() {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.roles = user.roles;

      this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      this.showModeratorBoard = this.roles.includes('ROLE_MODERATOR');

      this.username = user.username;
    }
  }

  logout() {
    this.tokenStorageService.signOutBackend().subscribe();
    this.tokenStorageService.signOutFrontEnd();
    window.location.reload();
  }

  openRegisterDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.panelClass = 'my-dialog';

    const dialogRef = this.dialog.open(RegisterComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data =>
      this.authService.register(data).subscribe(
        data => {
          console.log(data);
          this.isSuccessful = true;
          this.isSignUpFailed = false;
        },
        err => {
          this.errorMessage = err.error.errors;
          console.log(this.errorMessage);
          this.isSignUpFailed = true;
        }
      )
    );

  }
  openLoginDialog() {
  const dialogConfig = new MatDialogConfig();

  dialogConfig.disableClose = true;
  dialogConfig.autoFocus = true;
  dialogConfig.panelClass = 'my-dialog';

  const dialogRef = this.dialog.open(LoginComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data =>
        this.authService.login(data).subscribe(
          data => {
            this.tokenStorageService.saveToken(data.accessToken);
            this.tokenStorageService.saveUser(data);

            this.isLoginFailed = false;
            this.isLoggedIn = true;
            this.roles = this.tokenStorageService.getUser().roles;
            this.reloadPage();
          },
          err => {
            this.errorMessage = err.error.message;
            this.isLoginFailed = true;
          }
        )
    );
}
  reloadPage() {
    window.location.reload();
  }
}
