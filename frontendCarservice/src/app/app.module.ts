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
import { authInterceptorProviders } from './helpers/auth.interceptor';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatDialogModule} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import { MatchingPasswordValidatorDirective } from './components/validators/matching-password-validator.directive';
import { ErrorMatcherDirective } from './components/validators/error-matcher.directive';
import { MatchingPasswordMatcherDirective } from './components/validators/matching-password-matcher.directive';
import { PasswordRegexpValidatorDirective } from './components/validators/password-regexp-validator.directive';
import {ExistingEmailValidatorDirective} from './components/validators/existing-email-validator.directive';
import {RouterModule} from '@angular/router';
import { EmailNotExistingValidatorDirective } from './components/validators/email-not-existing-validator.directive';
import { ServiceReservationComponent } from './components/global/service-related-components/service-reservation/service-reservation.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatNativeDateModule} from '@angular/material/core';
import {FlexModule} from '@angular/flex-layout';
import {LoginDialogComponent} from './components/global/authorization-authentication/login/login-dialog/login-dialog.component';
import {RegisterComponent} from './components/global/authorization-authentication/registration/register/register.component';
import {HomeComponent} from './components/global/home/home.component';
import {NavigationBarComponent} from './components/navigation-bar/navigation-bar.component';
import {RegistrationSuccessfulComponent} from './components/global/authorization-authentication/registration/registration-successful/registration-successful.component';
import {VerificationDialogComponent} from './components/global/authorization-authentication/registration/verification-dialog/verification-dialog.component';
import {VerificationDialogEntryComponent} from './components/global/authorization-authentication/registration/verification-dialog-entry/verification-dialog-entry.component';
import {PasswordTokenSentComponent} from './components/global/authorization-authentication/login/passwordReset/password-token-sent/password-token-sent.component';
import {PasswordResetDialogComponent} from './components/global/authorization-authentication/login/passwordReset/password-reset-dialog/password-reset-dialog.component';
import {PasswordResetDialogEntryComponent} from './components/global/authorization-authentication/login/passwordReset/password-reset-dialog-entry/password-reset-dialog-entry.component';
import {PasswordSuccessfullyChangedComponent} from './components/global/authorization-authentication/login/passwordReset/password-successfully-changed/password-successfully-changed.component';
import {ExpiredResetPasswordTokenComponent} from './components/global/authorization-authentication/login/passwordReset/expired-reset-password-token/expired-reset-password-token.component';
import {PasswordResetTokenSenderComponent} from './components/global/authorization-authentication/login/passwordReset/password-reset-token-sender/password-reset-token-sender.component';
import { SuccessfullyReservedUnauthorizedServiceComponent } from './components/global/service-related-components/successfully-reserved-unauthorized-service/successfully-reserved-unauthorized-service.component';
import {DateAdapterService} from './services/date-adapter.service';
import {MatSelectModule} from '@angular/material/select';
import { VerifyServiceReservationComponent } from './components/global/service-related-components/verify-service-reservation/verify-service-reservation.component';
import { AuthServiceReservationComponent } from './components/authorizedUserComponents/serviceReservation/auth-service-reservation/auth-service-reservation.component';
import { VerifyServiceComponent } from './components/authorizedUserComponents/serviceReservation/verify-service/verify-service.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';
import {APP_BASE_HREF} from '@angular/common';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import { MenuListItemComponent } from './components/authorizedUserComponents/profile/side-nav/menu-list-item/menu-list-item.component';
import { NavComponent } from './components/authorizedUserComponents/profile/side-nav/nav/nav.component';

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
    PasswordResetTokenSenderComponent,
    PasswordTokenSentComponent,
    PasswordResetDialogComponent,
    PasswordResetDialogEntryComponent,
    PasswordSuccessfullyChangedComponent,
    ExpiredResetPasswordTokenComponent,
    EmailNotExistingValidatorDirective,
    ServiceReservationComponent,
    SuccessfullyReservedUnauthorizedServiceComponent,
    VerifyServiceReservationComponent,
    AuthServiceReservationComponent,
    VerifyServiceComponent,
    MenuListItemComponent,
    NavComponent
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
    MatDatepickerModule,
    MatNativeDateModule,
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
      {path: '**', redirectTo: 'home'}
    ], {useHash: true}),
    MatDatepickerModule,
    FlexModule,
    MatSelectModule,
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production}),
    MatSidenavModule,
    MatListModule
  ],
  providers: [SlimLoadingBarService, authInterceptorProviders, MatDatepickerModule,
    {provide: MAT_DATE_LOCALE, useValue: 'hu'},
    {provide: DateAdapter, useClass: DateAdapterService},
    { provide: APP_BASE_HREF, useValue: '/' }
    ],
  exports: [AppComponent],
  bootstrap: [AppComponent],
  entryComponents: [LoginDialogComponent,
    RegisterComponent, VerificationDialogEntryComponent, VerificationDialogComponent]
})
export class AppModule { }
