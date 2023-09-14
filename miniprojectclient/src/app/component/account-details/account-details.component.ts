import { Component, OnInit } from '@angular/core';
import { Accountdetails } from 'src/app/interface/accountdetails';
import { UsersService } from 'src/app/service/users.service';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.css']
})
export class AccountDetailsComponent implements OnInit {

  constructor(private userService : UsersService) {}

  user! : Accountdetails;
  email! : any;

  ngOnInit(): void {
    this.email = localStorage.getItem('email');
    this.getUserDetails();
  }

  getUserDetails() {
    this.userService.getUserByEmail(this.email).subscribe({
      next : (data) => {
        console.log(JSON.stringify(data));
        this.user = data;
      }
    })
  }

}
