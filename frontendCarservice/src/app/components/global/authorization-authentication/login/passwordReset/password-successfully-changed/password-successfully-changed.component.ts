import {Component, OnInit, Renderer2} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {DataService} from '../../../../../../services/data.service';

@Component({
  selector: 'app-passwored-successfully-changed',
  templateUrl: './password-successfully-changed.component.html',
  styleUrls: ['./password-successfully-changed.component.css']
})
export class PasswordSuccessfullyChangedComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<PasswordSuccessfullyChangedComponent>,
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
