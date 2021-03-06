import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

const serviceReservationURL = 'http://localhost:8080/api/test/serviceReservation/';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})


export class ServiceReservationService {

  constructor(private http: HttpClient) {
  }

  reserveAuthorizedServiceValidation(authorizedService, collector, carId): Observable<any> {
    return this.http.post(serviceReservationURL + 'serviceReservationAuthorizedValidation', {
      fkServiceReservationCarId: authorizedService.car,
      comment: authorizedService.comment,
      reservedDate: authorizedService.reservedDate,
      reservedServices: collector,
      carId: carId
    }, httpOptions);
  }

  reserveAuthorizedService(authorizedService, collector): Observable<any> {
    return this.http.post(serviceReservationURL + 'reserveService', {
      fkServiceReservationCarId: authorizedService.car,
      comment: authorizedService.comment,
      reservedDate: authorizedService.reservedDate,
      reservedServices: collector
    });
  }

  getUserCars(credentialId): Observable<any> {
    return this.http.get(`${serviceReservationURL}getCarsByRegId?credentialId=${credentialId}`);
  }

  reserveUnauthorizedServiceValidation(unauthorizedService, collector, foreignLicensePlate, billingToCompany): Observable<any> {
    return this.http.post(serviceReservationURL + 'serviceReservationUnauthorizedValidation', {
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
      foreignCountryPlate: foreignLicensePlate,
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
      billingToCompany,
      reservedServices: collector,
      comment: unauthorizedService.comment
    }, httpOptions);
  }

  reserveUnauthorizedService(unauthorizedService, collector): Observable<any> {
    return this.http.post(serviceReservationURL + 'serviceReservationUnauthorized', {
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
      reservedDate: unauthorizedService.reservedDate,
      billingName: unauthorizedService.billingName,
      billingPhoneNumber: unauthorizedService.billingPhoneNumber,
      billingEmail: unauthorizedService.billingEmail,
      billingZipCode: unauthorizedService.billingZipCode,
      billingTown: unauthorizedService.billingTown,
      billingStreet: unauthorizedService.billingStreet,
      billingOtherAddressType: unauthorizedService.billingOtherAddressType,
      billingTax: unauthorizedService.billingTax,
      billingToCompany: unauthorizedService.billingToCompany,
      reservedServices: collector,
      comment: unauthorizedService.comment
    }, httpOptions);
  }

  getServiceableCarsList(): Observable<any> {
    return this.http.get(serviceReservationURL + 'reserveDataGetter');
  }


}
