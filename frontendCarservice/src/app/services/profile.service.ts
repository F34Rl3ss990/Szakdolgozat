import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

const AUTH_API = 'http://localhost:8080/api/auth/';
const USER_API = 'http://localhost:8080/api/test/user/';
const CAR_API = 'http://localhost:8080/api/test/car/';
const DOCUMENT_API = 'http://localhost:8080/api/test/document/';
const SERVICE_DATA_API = 'http://localhost:8080/api/test/serviceData/';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private http: HttpClient) {

  }

  generateOtpNumber(): Observable<any> {
    return this.http.get(AUTH_API + 'generateOtpNumber');
  }

  changePhoneNumber(phoneNumberFormVal): Observable<any> {
    console.log(phoneNumberFormVal.phoneNumber);
    return this.http.post(USER_API + 'changePhoneNumber', {
      phoneNumber: phoneNumberFormVal.phoneNumber,
    }, httpOptions);
  }

  changePassword(passwordFormVal): Observable<any> {
    return this.http.post(AUTH_API + 'updatePassword', {
      password: passwordFormVal.password,
      matchingPassword: passwordFormVal.matchingPassword,
      oldPassword: passwordFormVal.oldPassword,
      otpNum: passwordFormVal.otpNum
    }, httpOptions);
  }

  changeBillingData(billingDataFormVal, billingForeignTax, billingToCompany): Observable<any> {
    console.log(billingForeignTax)
    console.log(billingToCompany)
    return this.http.post(USER_API + 'changeBillingData', {
      billingName: billingDataFormVal.value.billingName,
      billingEmail: billingDataFormVal.value.billingEmail,
      billingPhoneNumber: billingDataFormVal.value.billingPhoneNumber,
      billingZipCode: billingDataFormVal.value.billingZipCode,
      billingTown: billingDataFormVal.value.billingTown,
      billingStreet: billingDataFormVal.value.billingStreet,
      billingOtherAddressType: billingDataFormVal.value.billingOtherAddressType,
      billingTaxNumber: billingDataFormVal.value.billingTaxNumber,
      billingForeignCountryTax: billingForeignTax,
      billingToCompany
    }, httpOptions);
  }

  getUserData(): Observable<any> {
    return this.http.get(USER_API + 'getUser');
  }

  addCar(addCarForm, foreignLicensePlate): Observable<any> {
    console.log(addCarForm.mileage)
    return this.http.post(CAR_API + 'addCar', {
      brand: addCarForm.brand,
      type: addCarForm.type,
      yearOfManufacture: addCarForm.yearOfManufacture,
      engineType: addCarForm.engineType,
      mileage: addCarForm.mileage,
      engineNumber: addCarForm.engineNumber,
      chassisNumber: addCarForm.chassisNumber,
      licensePlateNumber: addCarForm.licensePlateNumber,
      foreignCountryPlate: foreignLicensePlate,
    }, httpOptions);
  }

  getCarsData(): Observable<any> {
    return this.http.get(CAR_API + 'getUser');
  }


  getUserDocuments(credentialId: bigint): Observable<any> {
    let params = new HttpParams();
    params = params.append('credentialId', String(credentialId));
    return this.http.get(`${DOCUMENT_API}filesByUser`, {params});
  }

  getCarDocuments(carId: bigint, credentialId: bigint): Observable<any> {
    let params = new HttpParams();
    params = params.append('carId', String(carId));
    params = params.append('credentialId', String(carId));
    return this.http.get(`${DOCUMENT_API}filesCarId`, {params});
  }

  getUserServiceData(credentialId: bigint): Observable<any> {
    let params = new HttpParams();
    params = params.append('credentialId', String(credentialId));
    return this.http.get(`${SERVICE_DATA_API}serviceDataByUser`, {params});
  }

  getCarServiceData(carId: bigint, credentialId: bigint): Observable<any> {
    let params = new HttpParams();
    params = params.append('carId', String(carId));
    params = params.append('credentialId', String(carId));
    return this.http.get(`${SERVICE_DATA_API}serviceDataByCar`, {params});
  }

  addCarAndUser(carAndUserForm, foreignLicensePlate, billingToCompany): Observable<any> {
    return this.http.post(USER_API + 'addCarAndUser', {
      name: carAndUserForm.name,
      email: carAndUserForm.email,
      phoneNumber: carAndUserForm.phoneNumber,
      brand: carAndUserForm.brand,
      type: carAndUserForm.type,
      yearOfManufacture: carAndUserForm.yearOfManufacture,
      engineType: carAndUserForm.engineType,
      mileage: carAndUserForm.mileage,
      engineNumber: carAndUserForm.engineNumber,
      chassisNumber: carAndUserForm.chassisNumber,
      licensePlateNumber: carAndUserForm.licensePlateNumber,
      foreignCountryPlate: foreignLicensePlate,
      billingName: carAndUserForm.billingName,
      billingPhoneNumber: carAndUserForm.billingPhoneNumber,
      billingEmail: carAndUserForm.billingEmail,
      billingZipCode: carAndUserForm.billingZipCode,
      billingTown: carAndUserForm.billingTown,
      billingStreet: carAndUserForm.billingStreet,
      billingOtherAddressType: carAndUserForm.billingOtherAddressType,
      billingTax: carAndUserForm.billingTax,
      billingForeignCountryTax: carAndUserForm.billingForeignCountryTax,
      billingToCompany,
    }, httpOptions);
  }
}
