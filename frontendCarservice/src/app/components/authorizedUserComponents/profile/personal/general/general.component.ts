import { Component, OnInit } from '@angular/core';
import {ProfileService} from '../../../../../services/profile.service';

@Component({
  selector: 'app-general',
  templateUrl: './general.component.html',
  styleUrls: ['./general.component.css']
})
export class GeneralComponent implements OnInit {

  user: any = {}

  constructor(private profileService: ProfileService) { }

  ngOnInit(): void {
    this.profileService.getUserData().subscribe(res => {
      this.user = res;
      }
    )
  }

}
