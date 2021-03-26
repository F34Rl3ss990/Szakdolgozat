import {Component, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';

@Component({
  selector: 'app-successfully-changed-password',
  templateUrl: './successfully-changed-password.component.html',
  styleUrls: ['./successfully-changed-password.component.css']
})
export class SuccessfullyChangedPasswordComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<SuccessfullyChangedPasswordComponent>,
              private renderer: Renderer2,
              private router: Router) {
  }
  ngOnInit(): void {
    this.renderer.listen(document, 'keydown', event => {
      if (event.key === 'Escape') {
        this.close();
      }
    });
  }

  close() {
    this.dialogRef.close();
    this.router.navigate['/profile'];
  }

}
