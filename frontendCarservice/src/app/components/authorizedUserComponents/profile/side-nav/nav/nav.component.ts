import {AfterViewInit, Component, OnInit} from '@angular/core';
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
      iconName: 'manage_accounts',
      route: 'profile/personal',
      children: [
        {
          displayName: 'Általános',
          iconName: 'account_circle',
          route: 'profile/personal/general'
        },
        {
          displayName: 'Jelszó módosítás',
          iconName: 'settings',
          route: 'profile/personal/changePassword'
        },
        {
          displayName: 'Email módosítás',
          iconName: 'settings',
          route: 'profile/personal/changeEmail'
        },
        {
          displayName: 'Számlázási adatok módosítása',
          iconName: 'settings',
          route: 'profile/personal/billingDataChange'
        }]
    },
    {
      displayName: 'Jármű',
      iconName: 'toys',
      route: 'profile/car',
      children: [
        {
          displayName: 'Jármű adatok',
          iconName: 'directions_car',
          route: 'profile/car/carData'
        },
        {
          displayName: 'Jármű hozzáadása',
          iconName: 'add_circle',
          route: 'profile/car/addCar'
        }]
    },
    {
      displayName: 'Szervizek',
      iconName: 'build',
      route: 'profile/service'
    },
    {
      displayName: 'Dokumentumok',
      iconName: 'document_scanner',
      route: 'profile/files'
    }
  ]

  constructor() {
  }

  ngAfterViewInit() {

  }
}
