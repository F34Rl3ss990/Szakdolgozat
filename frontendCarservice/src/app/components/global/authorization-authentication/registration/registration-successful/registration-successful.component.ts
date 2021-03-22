import {Component, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';

@Component({
  selector: 'app-registration-successful',
  templateUrl: './registration-successful.component.html',
  styleUrls: ['./registration-successful.component.css']
})
export class RegistrationSuccessfulComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<RegistrationSuccessfulComponent>,
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
    this.router.navigate['/home'];
  }
}
