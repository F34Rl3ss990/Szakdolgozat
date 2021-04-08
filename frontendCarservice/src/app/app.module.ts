import { BrowserModule } from '@angular/platform-browser';
import {NgModule} from '@angular/core';

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
import {DateAdapter, MAT_DATE_LOCALE, MatNativeDateModule} from '@angular/material/core';
import {FlexModule} from '@angular/flex-layout';
import {LoginDialogComponent} from './components/global/authorization-authentication/login/login-dialog/login-dialog.component';
import {RegisterComponent} from './components/global/authorization-authentication/registration/register/register.component';
import {HomeComponent} from './components/global/home/home.component';
import {NavigationBarComponent} from './components/navigation-bar/navigation-bar.component';
import {VerificationDialogComponent} from './components/global/authorization-authentication/registration/verification-dialog/verification-dialog.component';
import {VerificationDialogEntryComponent} from './components/global/authorization-authentication/registration/verification-dialog-entry/verification-dialog-entry.component';
import {PasswordResetDialogComponent} from './components/global/authorization-authentication/login/passwordReset/password-reset-dialog/password-reset-dialog.component';
import {PasswordResetDialogEntryComponent} from './components/global/authorization-authentication/login/passwordReset/password-reset-dialog-entry/password-reset-dialog-entry.component';
import {ExpiredResetPasswordTokenComponent} from './components/global/authorization-authentication/login/passwordReset/expired-reset-password-token/expired-reset-password-token.component';
import {PasswordResetTokenSenderComponent} from './components/global/authorization-authentication/login/passwordReset/password-reset-token-sender/password-reset-token-sender.component';
import {DateAdapterService} from './services/date-adapter.service';
import {MatSelectModule} from '@angular/material/select';
import { VerifyServiceReservationComponent } from './components/global/service-related-components/verify-service-reservation/verify-service-reservation.component';
import { AuthServiceReservationComponent } from './components/authorizedUserComponents/serviceReservation/auth-service-reservation/auth-service-reservation.component';
import { VerifyServiceComponent } from './components/authorizedUserComponents/serviceReservation/verify-service/verify-service.component';
import {MatTableModule} from '@angular/material/table';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import { MenuListItemComponent } from './components/authorizedUserComponents/profile/side-nav/menu-list-item/menu-list-item.component';
import { NavComponent } from './components/authorizedUserComponents/profile/side-nav/nav/nav.component';
import { GeneralComponent } from './components/authorizedUserComponents/profile/personal/general/general.component';
import { ChangePasswordComponent } from './components/authorizedUserComponents/profile/personal/change-password/change-password.component';
import { ChangeBillingDataComponent } from './components/authorizedUserComponents/profile/personal/change-billing-data/change-billing-data.component';
import { CarDataComponent } from './components/authorizedUserComponents/profile/cars/car-data/car-data.component';
import { AddCarComponent } from './components/authorizedUserComponents/profile/cars/add-car/add-car.component';
import { ServicesComponent } from './components/authorizedUserComponents/profile/services/services-user/services.component';
import { FilesComponent } from './components/authorizedUserComponents/profile/files/files.component';
import { ChangePhoneNumberComponent } from './components/authorizedUserComponents/profile/personal/change-phone-number/change-phone-number.component';
import {UserService} from './services/user.service';
import {TokenStorageService} from './services/token-storage.service';
import {ServiceReservationService} from './services/service-reservation.service';
import {NavService} from './services/nav.service';
import {ProfileService} from './services/profile.service';
import {DialogService} from './services/dialog.service';
import {DataService} from './services/data.service';
import {AuthService} from './services/auth.service';
import {MatSortModule} from '@angular/material/sort';
import {MatButtonModule} from '@angular/material/button';
import { ServicesCarComponent } from './components/authorizedUserComponents/profile/services/services-car/services-car.component';
import { SafeHtmlPipe } from './services/safe-html.pipe';
import { DynamicViewDialogComponent } from './components/dynamic-view-dialog/dynamic-view-dialog.component';
import { FallbackIfNoUserComponent } from './components/authorizedUserComponents/fallback-if-no-user/fallback-if-no-user.component';

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
    VerificationDialogComponent,
    VerificationDialogEntryComponent,
    PasswordResetTokenSenderComponent,
    PasswordResetDialogComponent,
    PasswordResetDialogEntryComponent,
    ExpiredResetPasswordTokenComponent,
    EmailNotExistingValidatorDirective,
    ServiceReservationComponent,
    VerifyServiceReservationComponent,
    AuthServiceReservationComponent,
    VerifyServiceComponent,
    MenuListItemComponent,
    NavComponent,
    GeneralComponent,
    ChangePasswordComponent,
    ChangeBillingDataComponent,
    CarDataComponent,
    AddCarComponent,
    ServicesComponent,
    FilesComponent,
    ChangePhoneNumberComponent,
    ServicesCarComponent,
    SafeHtmlPipe,
    DynamicViewDialogComponent,
    FallbackIfNoUserComponent,
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
    MatTableModule,
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
    ],),
    MatDatepickerModule,
    FlexModule,
    MatSelectModule,
    MatSidenavModule,
    MatListModule,
    MatSortModule,
    MatButtonModule
  ],
  providers: [SlimLoadingBarService,
    UserService,
    TokenStorageService,
    ServiceReservationService,
    NavService,
    ProfileService,
    FilterPipe,
    DialogService,
    DateAdapterService,
    DataService,
    AuthService,
    authInterceptorProviders,
    MatDatepickerModule,
    {provide: MAT_DATE_LOCALE, useValue: 'hu'},
    {provide: DateAdapter, useClass: DateAdapterService},
    ],
  exports: [AppComponent],
  bootstrap: [AppComponent],
  entryComponents: [LoginDialogComponent,
    RegisterComponent, VerificationDialogEntryComponent, VerificationDialogComponent]
})
export class AppModule { }
