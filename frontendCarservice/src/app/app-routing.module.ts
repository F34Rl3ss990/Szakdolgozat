import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RegisterComponent} from './components/global/authorization-authentication/registration/register/register.component';
import {LoginDialogComponent} from './components/global/authorization-authentication/login/login-dialog/login-dialog.component';
import {HomeComponent} from './components/global/home/home.component';


const routes: Routes = [
  { path: 'login', component: LoginDialogComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
