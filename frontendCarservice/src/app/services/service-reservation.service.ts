import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

const AUTH_API = 'http://localhost:8080/api/test/serviceReservation/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})

export class ServiceReservationService {

  constructor(private http: HttpClient) { }

  reserveUnauthorizedService(unauthorizedService, collector): Observable<any> {
    console.log(unauthorizedService)
    console.log(collector)
    return this.http.post(AUTH_API + 'serviceReservationUnauthorized', {
      name: unauthorizedService.name,
      email: unauthorizedService.email,
      phoneNumber: unauthorizedService.phoneNumber,
      brand: unauthorizedService.brand,
      type: unauthorizedService.type,
      yearOfManufacture: unauthorizedService.yearOfManufacture,
      engineType: unauthorizedService.engineType,
      mileage: unauthorizedService.mileage,
      engineNumber: unauthorizedService.engineNumber,
      chassisNumber: unauthorizedService.chassisNumber,
      licensePlateNumber: unauthorizedService.licensePlateNumber,
      foreignCountryPlate: unauthorizedService.foreignCountryPlate,
      reservedDate: unauthorizedService.reservedDate,
      billingName: unauthorizedService.billingName,
      billingPhoneNumber: unauthorizedService.billingPhoneNumber,
      billingEmail: unauthorizedService.billingEmail,
      billingZipCode: unauthorizedService.billingZipCode,
      billingTown: unauthorizedService.billingTown,
      billingStreet: unauthorizedService.billingStreet,
      billingOtherAddressType: unauthorizedService.billingOtherAddressType,
      billingTax: unauthorizedService.billingTax,
      billingForeignCountryTax: unauthorizedService.billingForeignCountryTax,
      reservedServices: collector,
      comment: unauthorizedService.comment
    }, httpOptions);
  }

}
