import {Component, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {DataService} from '../../../../../../services/data.service';

@Component({
  selector: 'app-expired-reset-password-token',
  templateUrl: './expired-reset-password-token.component.html',
  styleUrls: ['./expired-reset-password-token.component.css']
})
export class ExpiredResetPasswordTokenComponent implements OnInit {

  isVerified: boolean;

  constructor(private dialogRef: MatDialogRef<ExpiredResetPasswordTokenComponent>,
              private renderer: Renderer2,
              private dataService: DataService) {
  }
  ngOnInit(): void {
    this.renderer.listen(document, 'keydown', event => {
      if (event.key === 'Escape') {
        this.close();
      }
    });
  }

  close() {
    this.dataService.token = null;
    this.dialogRef.close();
  }
}
