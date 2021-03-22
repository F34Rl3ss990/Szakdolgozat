import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RegisterComponent} from './components/global/authorization-authentication/registration/register/register.component';
import {LoginDialogComponent} from './components/global/authorization-authentication/login/login-dialog/login-dialog.component';
import {HomeComponent} from './components/global/home/home.component';
import {ServiceReservationComponent} from './components/global/service-related-components/service-reservation/service-reservation.component';
import {VerifyServiceReservationComponent} from './components/global/service-related-components/verify-service-reservation/verify-service-reservation.component';
import {AuthServiceReservationComponent} from './components/authorizedUserComponents/serviceReservation/auth-service-reservation/auth-service-reservation.component';
import {VerifyServiceComponent} from './components/authorizedUserComponents/serviceReservation/verify-service/verify-service.component';
import {SideNavComponent} from './components/authorizedUserComponents/profile-nav/side-nav/side-nav.component';


const routes: Routes = [
  { path: 'login', component: LoginDialogComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'service-reservation', component: ServiceReservationComponent},
  { path: 'service-reservation/verify', component: VerifyServiceReservationComponent},
  { path: 'auth-service-reservation', component: AuthServiceReservationComponent},
  { path: 'auth-service-reservation/verify', component: VerifyServiceComponent},
  { path: 'profile', component: SideNavComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
