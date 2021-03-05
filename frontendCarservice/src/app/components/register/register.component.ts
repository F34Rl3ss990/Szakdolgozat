import {Component, HostListener, OnInit, Renderer2, ViewEncapsulation} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';

@HostListener('document:keydown.meta.k')

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class RegisterComponent implements OnInit {
  form: any = {};
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  submitted: boolean;
  registerForm: FormGroup;

  constructor(private authService: AuthService,
              private dialogRef: MatDialogRef<RegisterComponent>,
              private renderer: Renderer2,
              private fb: FormBuilder) {
    this.createForm();
  }

  emailFormControl = new FormControl("", {
    validators: [Validators.required, Validators.email],
    updateOn: "blur"
  })

  createForm() {
     this.registerForm = this.fb.group({
      emailFormControl: this.fb.control('', {updateOn: 'blur', validators: [Validators.required, Validators.email]})
    });
  }

  matcher = new MyErrorStateMatcher();

  ngOnInit(): void {
    this.renderer.listen(document, 'keydown', event => {
      if (event.key === 'Enter') {
        this.onSubmit();
      } else if (event.key === 'Escape') {
        this.close()
      }

    })

  }

  onSubmit() {
    this.submitted = true;
    this.authService.register(this.form).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.isSignUpFailed = false;
        this.dialogRef.close();
      },
      err => {
        this.errorMessage = err.error.errors;
        console.log(this.errorMessage);
        this.isSignUpFailed = true;
      }
    )
  }

  close() {
    this.dialogRef.close();
  }
}
