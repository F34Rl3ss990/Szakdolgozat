import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

const AUTH_API = 'http://localhost:8080/api/auth/';
const USER_API = 'http://localhost:8080/api/test/user/';

const httpOptions = {

  headers: new HttpHeaders({ 'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private http: HttpClient) {

  }



  generateOtpNumber(): Observable<any>{
    return this.http.get(`${AUTH_API}generateOtpNumber`);
  }

  changePhoneNumber(phoneNumberFormVal): Observable<any> {
    return this.http.post(AUTH_API + 'changePhoneNumber', {
      phoneNumber: phoneNumberFormVal.phoneNumber,
    }, httpOptions);
  }

  changePassword(passwordFormVal): Observable<any> {
    return this.http.post(AUTH_API + 'updatePassword', {
      password: passwordFormVal.email,
      matchingPassword: passwordFormVal.otpNum,
      oldPassword: passwordFormVal.oldPassword,
      otpNum: passwordFormVal.otpNum
    }, httpOptions);
  }

  changeBillingData(billingDataFormVal, billingForeignTax, billingToCompany): Observable<any> {
    return this.http.post(USER_API + 'changeBillingData', {
      billingName: billingDataFormVal.billingName,
      billingEmail: billingDataFormVal.billingEmail,
      billingPhoneNumber: billingDataFormVal.billingPhoneNumber,
      billingZipCode: billingDataFormVal.billingZipCode,
      billingTown: billingDataFormVal.billingTown,
      billingStreet: billingDataFormVal.billingStreet,
      billingOtherAddressType: billingDataFormVal.billingOtherAddressType,
      billingTaxNumber: billingDataFormVal.billingTaxNumber,
      billingForeignCountryTax: billingForeignTax,
      billingToCompany: billingToCompany
    }, httpOptions);
  }

  getUserData(): Observable<any>{
   return this.http.get(USER_API + 'getUser');
  }
}
