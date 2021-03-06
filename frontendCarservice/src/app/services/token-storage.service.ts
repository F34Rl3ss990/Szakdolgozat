import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';
const EXP_DATE = 'auth-exp';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  constructor(private http: HttpClient) { }

  signOutBackend() {
    return this.http.get('http://localhost:8080/api/auth/logout', { responseType: 'text'});
  }

  signOutFrontEnd(){
    window.localStorage.clear();
  }

  public saveToken(token: string) {
    window.localStorage.removeItem(TOKEN_KEY);
    window.localStorage.setItem(TOKEN_KEY, token);
  }

  public saveExp(date: string) {
    window.localStorage.removeItem(EXP_DATE);
    window.localStorage.setItem(EXP_DATE, date);
  }

  public getToken(): string {
    return localStorage.getItem(TOKEN_KEY);
  }

  public getExpDate(): string {
    return localStorage.getItem(EXP_DATE);
  }

  public saveUser(user){
    window.localStorage.removeItem(USER_KEY);
    window.localStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(){
    return JSON.parse(localStorage.getItem(USER_KEY));
  }
}
