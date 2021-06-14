import { Component, OnInit } from '@angular/core';
import {ProfileService} from '../../../../../services/profile.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-general',
  templateUrl: './general.component.html',
  styleUrls: ['./general.component.css']
})
export class GeneralComponent implements OnInit {

  user: any = {}

  constructor(private profileService: ProfileService,
              private router: Router) { }

  ngOnInit(): void {
    this.profileService.getUserData().subscribe(res => {
      this.user = res;
      }, error => {
      this.router.navigate(['setProfile']);
      }
    )
  }

}
