import {MatTableDataSource} from '@angular/material/table';

export interface responseDocument {
  carId: number;
  brand: string;
  type: string;
  licensePlateNumber: string;
  serviceDataId: number;
  date: Date;
  mileage: string;
  documentIds: [];
  documentList?: documentArray[] | MatTableDataSource<documentArray>;
}

export interface documentArray{
  id: number;
  type: string;
  name: string;
  size: string;
}

export interface responseDocumentSource {
  carId: number;
  brand: string;
  type: string;
  licensePlateNumber: string;
  serviceDataId: number;
  date: Date;
  mileage: string;
  documentIds: [];
  documentList?: MatTableDataSource<documentArray>;
}
