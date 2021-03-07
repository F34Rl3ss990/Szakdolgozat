import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';


const AUTH_API = 'http://localhost:8080/api/auth/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
  }

  login(credentials): Observable<any> {
    return this.http.post(AUTH_API + 'signin', {
      email: credentials.email,
      password: credentials.password
    }, httpOptions);
  }

  register(user): Observable<any> {
    return this.http.post(AUTH_API + 'signup', {
      email: user.email,
      password: user.password,
      matchingPassword: user.matchingPassword
    }, httpOptions);
  }

  getUserByEmail(userEmail: string): Promise<Boolean> {
    return this.http.get<Boolean>(AUTH_API + 'emailValid?email=' + userEmail).toPromise();
  }

  confirmRegistration(token: string): Observable<any>{
    return this.http.get(`${AUTH_API}registrationConfirm?token=${token}`);
  }

  resetPassword(emailParam: string): Observable<any>{
    return this.http.post(AUTH_API + 'resetPassword',{
      email: emailParam
    }, httpOptions);
  }

  checkPasswordResetToken(token: string): Observable<any>{
    return this.http.get(`${AUTH_API}changePassword?token=${token}`);
  }

  savePassword(passwordResetRequest, token): Observable<any>{
    return this.http.post(AUTH_API + 'resetPassword',{
      password: passwordResetRequest.password,
      matchingPassword: passwordResetRequest.matchingPassword,
      token: token
    }, httpOptions);
  }
}
