import {Component, OnInit} from '@angular/core';
import {SlimLoadingBarService} from 'ng2-slim-loading-bar';
import {Router} from '@angular/router';
import {TokenStorageService} from '../../services/token-storage.service';
import {MatDialog} from '@angular/material/dialog';
import {AuthService} from '../../services/auth.service';
import {DialogService} from '../../services/dialog.service';
import {DataService} from '../../services/data.service';

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.scss']
})
export class NavigationBarComponent implements OnInit {

  private roles: string[];
  isLoggedIn = false;
  showAdminBoard = false;
  showModeratorBoard = false;
  username: string;

  constructor(private loadingBar: SlimLoadingBarService, private router: Router,
              private tokenStorageService: TokenStorageService,
              private dialog: MatDialog, private authService: AuthService,
              private dialogService: DialogService,
              private dataService: DataService) {
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
    this.dataService.serviceReservationForm = null;
    window.location.reload();
  }

  openRegistrationDialog() {
    this.dialogService.openRegistrationDialog();
  }


  openLoginDialog() {
    this.dialogService.openLoginDialog();
  }

}
