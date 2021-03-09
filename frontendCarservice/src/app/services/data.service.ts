import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  liveToken: boolean;
  alreadyVerified: boolean;
  expiredVerifyToken: boolean;
  token: string;
  submitted: boolean;
  constructor() { }
}
