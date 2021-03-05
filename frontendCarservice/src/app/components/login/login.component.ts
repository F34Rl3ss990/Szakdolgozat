import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {TokenStorageService} from '../../services/token-storage.service';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService,
              private dialogRef: MatDialogRef<LoginComponent>) { }

  ngOnInit() {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }
  }

  save() {
    this.dialogRef.close(this.form);
  }

  close() {
    this.dialogRef.close();
  }

  /*
  reloadPage() {
    window.location.reload();
  }*/
}
