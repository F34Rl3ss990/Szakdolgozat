import {HTTP_INTERCEPTORS, HttpErrorResponse, HttpEvent} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';

import { TokenStorageService } from '../services/token-storage.service';
import {Observable} from 'rxjs/Observable';
import { catchError } from 'rxjs/operators';
import {  throwError } from 'rxjs';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private token: TokenStorageService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    const token = this.token.getToken();
    const expTime: number = +this.token.getExpDate();
    var currDateString = new Date().getTime();
    if (token != null && expTime > currDateString) {
      authReq = req.clone({headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token)});
    }
    return next.handle(authReq)
      .pipe(catchError(err => {
        console.log(err);
        if (err instanceof HttpErrorResponse) {
          console.log(err.status);
          console.log(err.statusText);
          if (err.status === 401) {
            this.token.signOutFrontEnd();
            window.location.href = "/home";
          }
          return throwError(err.message);
        }
    }))
  }
}

export const authInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
];
