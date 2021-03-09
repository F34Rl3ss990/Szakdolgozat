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
import { LoginDialogComponent } from './components/login/login-dialog/login-dialog.component';
import { RegisterComponent } from './components/registration/register/register.component';
import { HomeComponent } from './components/home/home.component';
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
import {RouterModule} from '@angular/router';
import { VerificationDialogComponent } from './components/registration/verification-dialog/verification-dialog.component';
import { VerificationDialogEntryComponent } from './components/registration/verification-dialog-entry/verification-dialog-entry.component';
import { PasswordResetComponent } from './components/login/password-reset/password-reset.component';
import { PasswordTokenSentComponent } from './components/login/password-token-sent/password-token-sent.component';
import { PasswordResetDialogComponent } from './components/login/password-reset-dialog/password-reset-dialog.component';
import { PasswordResetDialogEntryComponent } from './components/login/password-reset-dialog-entry/password-reset-dialog-entry.component';
import { PasswordSuccessfullyChangedComponent } from './components/login/password-successfully-changed/password-successfully-changed.component';
import { ExpiredResetPasswordTokenComponent } from './components/login/expired-reset-password-token/expired-reset-password-token.component';
import { EmailNotExistingValidatorDirective } from './components/validators/email-not-existing-validator.directive';


@NgModule({
  declarations: [
    AppComponent,
    FilterPipe,
    LoginDialogComponent,
    RegisterComponent,
    HomeComponent,
    NavigationBarComponent,
    MatchingPasswordValidatorDirective,
    ErrorMatcherDirective,
    MatchingPasswordMatcherDirective,
    PasswordRegexpValidatorDirective,
    ExistingEmailValidatorDirective,
    RegistrationSuccessfulComponent,
    VerificationDialogComponent,
    VerificationDialogEntryComponent,
    PasswordResetComponent,
    PasswordTokenSentComponent,
    PasswordResetDialogComponent,
    PasswordResetDialogEntryComponent,
    PasswordSuccessfullyChangedComponent,
    ExpiredResetPasswordTokenComponent,
    EmailNotExistingValidatorDirective
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
        MatIconModule,
        RouterModule.forRoot([
        {
          path: 'home',
          component: HomeComponent,
          children: [
            {
              path: 'registrationConfirm/:token',
              component: VerificationDialogEntryComponent
            },
            {
              path: 'changePassword/:token',
              component: PasswordResetDialogEntryComponent
            }
          ]
        },
        { path: '**', redirectTo: 'home' }
      ])
    ],
  providers: [SlimLoadingBarService, authInterceptorProviders],
  exports: [AppComponent],
  bootstrap: [AppComponent],
  entryComponents: [LoginDialogComponent,
  RegisterComponent, VerificationDialogEntryComponent, VerificationDialogComponent]
})
export class AppModule { }
