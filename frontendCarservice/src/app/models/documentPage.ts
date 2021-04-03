import {MatTableDataSource} from '@angular/material/table';

export interface responseDocument {
  carId: bigint;
  brand: string;
  type: string;
  serviceDataId: bigint;
  documentIds: {};
  documents?: documentArray[] | MatTableDataSource<documentArray>;
}

export interface documentArray{
  name: string;
  documentId: bigint;
  type: string;
  size: bigint;
}

export interface responseDocumentSource {
  carId: bigint;
  brand: string;
  type: string;
  serviceDataId: bigint;
  documentIds: {};
  documents?: MatTableDataSource<documentArray>;
}
