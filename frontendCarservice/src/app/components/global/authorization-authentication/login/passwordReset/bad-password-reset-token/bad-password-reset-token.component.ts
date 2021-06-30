import {Component, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {DataService} from '../../../../../../services/data.service';

@Component({
  selector: 'app-bad-password-reset-token',
  templateUrl: './bad-password-reset-token.component.html',
  styleUrls: ['./bad-password-reset-token.component.css']
})
export class BadPasswordResetTokenComponent implements OnInit {

  isVerified: boolean;

  constructor(private dialogRef: MatDialogRef<BadPasswordResetTokenComponent>,
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
