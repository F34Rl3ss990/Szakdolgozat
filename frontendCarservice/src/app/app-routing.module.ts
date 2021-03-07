import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {BoardAdminComponent} from './components/board-admin/board-admin.component';
import {BoardUserComponent} from './components/board-user/board-user.component';
import {ProfileComponent} from './components/profile/profile.component';
import {RegisterComponent} from './components/registration/register/register.component';
import {LoginDialogComponent} from './components/login/login-dialog/login-dialog.component';
import {HomeComponent} from './components/home/home.component';


const routes: Routes = [
//  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginDialogComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'user', component: BoardUserComponent },
  { path: 'admin', component: BoardAdminComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
 // {path: 'registrationConfirm/:token', component: HomeComponent },
 // {path: '**', component: HomeComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
