import {Component, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-successfully-reserved-unauthorized-service',
  templateUrl: './successfully-reserved-unauthorized-service.component.html',
  styleUrls: ['./successfully-reserved-unauthorized-service.component.css']
})
export class SuccessfullyReservedUnauthorizedServiceComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<SuccessfullyReservedUnauthorizedServiceComponent>,
              private renderer: Renderer2) {
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
  }

}
