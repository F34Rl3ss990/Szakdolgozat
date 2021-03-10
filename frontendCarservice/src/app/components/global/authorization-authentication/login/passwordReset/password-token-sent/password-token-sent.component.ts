import {Component, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';

@Component({
  selector: 'app-password-token-sent',
  templateUrl: './password-token-sent.component.html',
  styleUrls: ['./password-token-sent.component.css']
})
export class PasswordTokenSentComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<PasswordTokenSentComponent>,
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
