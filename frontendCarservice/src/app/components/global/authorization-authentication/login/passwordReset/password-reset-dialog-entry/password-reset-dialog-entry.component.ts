import { Component, OnInit } from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../../../../../services/auth.service';
import {DataService} from '../../../../../../services/data.service';
import {DialogService} from '../../../../../../services/dialog.service';

@Component({
  selector: 'app-password-reset-dialog-entry',
  templateUrl: './password-reset-dialog-entry.component.html',
  styleUrls: ['./password-reset-dialog-entry.component.css']
})
export class PasswordResetDialogEntryComponent implements OnInit {

  constructor(private dialog: MatDialog,
              private route: ActivatedRoute,
              private dataService: DataService,
              private authService: AuthService,
              private dialogService: DialogService){
  }

  ngOnInit(): void {
    this.route.params.subscribe(params =>{
      let token = params['token'];
      this.authService.checkPasswordResetToken(token).subscribe(result =>{
        if (result==true){
          this.dataService.token = token;
          this.dialogService.openPasswordResetDialog();
        } else{
          this.dialogService.openExpiredTokenDialog();
        }
      })
    })

  }

}
