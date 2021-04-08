import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RegisterComponent} from './components/global/authorization-authentication/registration/register/register.component';
import {LoginDialogComponent} from './components/global/authorization-authentication/login/login-dialog/login-dialog.component';
import {ServiceReservationComponent} from './components/global/service-related-components/service-reservation/service-reservation.component';
import {VerifyServiceReservationComponent} from './components/global/service-related-components/verify-service-reservation/verify-service-reservation.component';
import {AuthServiceReservationComponent} from './components/authorizedUserComponents/serviceReservation/auth-service-reservation/auth-service-reservation.component';
import {VerifyServiceComponent} from './components/authorizedUserComponents/serviceReservation/verify-service/verify-service.component';
import {NavComponent} from './components/authorizedUserComponents/profile/side-nav/nav/nav.component';
import {GeneralComponent} from './components/authorizedUserComponents/profile/personal/general/general.component';
import {ChangePasswordComponent} from './components/authorizedUserComponents/profile/personal/change-password/change-password.component';
import {ChangeBillingDataComponent} from './components/authorizedUserComponents/profile/personal/change-billing-data/change-billing-data.component';
import {CarDataComponent} from './components/authorizedUserComponents/profile/cars/car-data/car-data.component';
import {AddCarComponent} from './components/authorizedUserComponents/profile/cars/add-car/add-car.component';
import {ServicesComponent} from './components/authorizedUserComponents/profile/services/services-user/services.component';
import {FilesComponent} from './components/authorizedUserComponents/profile/files/files.component';
import {ChangePhoneNumberComponent} from './components/authorizedUserComponents/profile/personal/change-phone-number/change-phone-number.component';
import {FallbackIfNoUserComponent} from './components/authorizedUserComponents/fallback-if-no-user/fallback-if-no-user.component';


const routes: Routes = [
  { path: 'login', component: LoginDialogComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'service-reservation', component: ServiceReservationComponent},
  { path: 'service-reservation/verify', component: VerifyServiceReservationComponent},
  { path: 'auth-service-reservation', component: AuthServiceReservationComponent},
  { path: 'auth-service-reservation/verify', component: VerifyServiceComponent},
  { path: 'setProfile', component: FallbackIfNoUserComponent},
  { path: 'profile', component: NavComponent, children: [
      {path: '', component: GeneralComponent},
      {path: 'personal', children:[
          {path: 'general', component: GeneralComponent},
          {path: 'changePassword', component: ChangePasswordComponent},
          {path: 'changePhoneNumber', component: ChangePhoneNumberComponent},
          {path: 'changeBillingData', component: ChangeBillingDataComponent}
        ]},
      {path: 'car', children:[
          {path: 'carData', component: CarDataComponent},
          {path: 'addCar', component: AddCarComponent}
        ]},
      {path: 'service', component: ServicesComponent},
      {path: 'services/:id', component: ServicesComponent},
      {path: 'files/:id', component: FilesComponent},
      {path: 'files', component: FilesComponent},

    ]},
  {path: 'changePassword', component: ChangePasswordComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
