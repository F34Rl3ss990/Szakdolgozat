import {AfterViewInit, Component} from '@angular/core';
import {NavItem} from '../../../../../models/NavItem';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss']
})
export class NavComponent implements AfterViewInit {
  navItems: NavItem[] = [
    {
      displayName: 'Személyes adatok',
      iconName: 'user-cog',
      route: 'profile/personal',
      children: [
        {
          displayName: 'Általános',
          iconName: 'user-alt',
          route: 'profile/personal/general'
        },
        {
          displayName: 'Jelszó módosítás',
          iconName: 'cog',
          route: 'profile/personal/changePassword'
        },
        {
          displayName: 'Telefonszám módosítás',
          iconName: 'cog',
          route: 'profile/personal/changePhoneNumber'
        },
        {
          displayName: 'Számlázási adatok módosítása',
          iconName: 'cog',
          route: 'profile/personal/changeBillingData'
        }]
    },
    {
      displayName: 'Jármű',
      iconName: 'car-side',
      route: 'profile/car',
      children: [
        {
          displayName: 'Jármű adatok',
          iconName: 'car',
          route: 'profile/car/carData'
        },
        {
          displayName: 'Jármű hozzáadása',
          iconName: 'car',
          route: 'profile/car/addCar'
        }]
    },
    {
      displayName: 'Szervizek',
      iconName: 'file-alt',
      route: 'profile/service'
    },
    {
      displayName: 'Dokumentumok',
      iconName: 'file-alt',
      route: 'profile/files'
    }
  ];

  constructor() {

  }

  ngAfterViewInit() {

  }
}
