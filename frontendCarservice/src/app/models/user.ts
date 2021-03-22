export interface user {
  name: string;
  email: string;
  phoneNumber: string;
  billingInformation: billingInformation;
}
export interface billingInformation{
  billingPhoneNumber: string;
  billingName: string;
  billingZipCode: number;
  billingTown: string;
  billingStreet: string;
  billingOtherAddressType: string;
  billingTaxNumber: string;
  billingEmail: string;
}
