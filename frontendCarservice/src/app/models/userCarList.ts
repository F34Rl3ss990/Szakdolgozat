export interface userCarList {
  carId: bigint;
  brand: string;
  type: string;
  yearOfManufacture: string;
  engineType: string;
  engineNumber: string;
  chassisNumber: string;
  licensePlateNumber: string;
  fkCarUser: bigint;
  mileage: string;
}
