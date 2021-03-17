import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RegisterComponent} from './components/global/authorization-authentication/registration/register/register.component';
import {LoginDialogComponent} from './components/global/authorization-authentication/login/login-dialog/login-dialog.component';
import {HomeComponent} from './components/global/home/home.component';
import {ServiceReservationComponent} from './components/global/service-related-components/service-reservation/service-reservation.component';
import {VerifyServiceReservationComponent} from './components/global/service-related-components/verify-service-reservation/verify-service-reservation.component';


const routes: Routes = [
  { path: 'login', component: LoginDialogComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'service-reservation', component: ServiceReservationComponent},
  { path: 'verif', component: VerifyServiceReservationComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
