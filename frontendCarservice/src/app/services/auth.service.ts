import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';


const AUTH_API = 'http://localhost:8080/api/auth/';
// const AUTH_API = 'http://84.2.172.134:8080/api/auth/';


const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

const httpOptionsText = {
  headers: new HttpHeaders({'Content-Type': 'text/plain'})
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
    });
  }


  register(user): Observable<any> {
    return this.http.post(AUTH_API + 'signup', {
      email: user.email,
      password: user.password,
      matchingPassword: user.matchingPassword
    }, httpOptions);
  }

  getUserByEmail(userEmail: string): Promise<boolean> {

    return this.http.get<boolean>(AUTH_API + 'emailValid?email=' + userEmail).toPromise();
  }

  confirmRegistration(token: string): Observable<any> {
    return this.http.get(`${AUTH_API}registrationConfirm?token=${token}`);
  }

  resetPassword(email: string): Observable<any> {
    return this.http.get(`${AUTH_API}resetPassword?email=${email}`);
  }

  checkPasswordResetToken(token: string): Observable<any> {
    return this.http.get(`${AUTH_API}changePassword?token=${token}`);
  }

  savePassword(passwordResetRequest, token): Observable<any> {
    return this.http.post(AUTH_API + 'savePassword', {
      password: passwordResetRequest.password,
      matchingPassword: passwordResetRequest.matchingPassword,
      token
    }, httpOptions);
  }
}
