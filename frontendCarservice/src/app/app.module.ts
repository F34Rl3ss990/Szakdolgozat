import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {FilterPipe} from './services/filter-pipe.service';
import {SlimLoadingBarModule, SlimLoadingBarService} from 'ng2-slim-loading-bar';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import {MatPaginatorModule} from '@angular/material/paginator';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/registration/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { ProfileComponent } from './components/profile/profile.component';
import { BoardAdminComponent } from './components/board-admin/board-admin.component';
import { BoardUserComponent } from './components/board-user/board-user.component';
import { authInterceptorProviders } from './helpers/auth.interceptor';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatDialogModule} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {NavigationBarComponent} from './components/navigation-bar/navigation-bar.component';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import { MatchingPasswordValidatorDirective } from './components/validators/matching-password-validator.directive';
import { ErrorMatcherDirective } from './components/validators/error-matcher.directive';
import { MatchingPasswordMatcherDirective } from './components/validators/matching-password-matcher.directive';
import { PasswordRegexpValidatorDirective } from './components/validators/password-regexp-validator.directive';
import {ExistingEmailValidatorDirective} from './components/validators/existing-email-validator.directive';
import { RegistrationSuccessfulComponent } from './components/registration/registration-successful/registration-successful.component';

@NgModule({
  declarations: [
    AppComponent,
    FilterPipe,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    ProfileComponent,
    BoardAdminComponent,
    BoardUserComponent,
    NavigationBarComponent,
    MatchingPasswordValidatorDirective,
    ErrorMatcherDirective,
    MatchingPasswordMatcherDirective,
    PasswordRegexpValidatorDirective,
    ExistingEmailValidatorDirective,
    RegistrationSuccessfulComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        NgbModule,
        SlimLoadingBarModule,
        ReactiveFormsModule,
        FormsModule,
        MDBBootstrapModule.forRoot(),
        MatPaginatorModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatDialogModule,
        MatFormFieldModule,
        MatCheckboxModule,
        MatInputModule,
        MatIconModule
    ],
  providers: [SlimLoadingBarService, authInterceptorProviders],
  exports: [AppComponent],
  bootstrap: [AppComponent],
  entryComponents: [LoginComponent,
  RegisterComponent]
})
export class AppModule { }
