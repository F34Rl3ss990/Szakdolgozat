import {MatTableDataSource} from '@angular/material/table';

export interface responseServiceData {
  carId: number;
  brand: string;
  type: string;
  licensePlateNumber: string;
  serviceList?: serviceArray[] | MatTableDataSource<serviceArray>;
}

export interface serviceArray{
  mileage: string;
  billNum: string;
  servicesDone: string;
  comment: string;
  amount: string;
  date: Date;
}

export interface responseServiceSource {
  carId: number;
  brand: string;
  type: string;
  licensePlateNumber: string;
  serviceList?: MatTableDataSource<serviceArray>;
}
