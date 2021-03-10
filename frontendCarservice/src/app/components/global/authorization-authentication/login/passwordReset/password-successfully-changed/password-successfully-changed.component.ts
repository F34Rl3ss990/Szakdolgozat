import {Component, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';

@Component({
  selector: 'app-passwored-successfully-changed',
  templateUrl: './password-successfully-changed.component.html',
  styleUrls: ['./password-successfully-changed.component.css']
})
export class PasswordSuccessfullyChangedComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<PasswordSuccessfullyChangedComponent>,
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
    this.router.navigate(['home'])
  }

}
