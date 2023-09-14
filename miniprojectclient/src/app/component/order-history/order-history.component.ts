import { Component, OnInit } from '@angular/core';
import { Orderdetails } from 'src/app/interface/orderdetails';
import { UsersService } from 'src/app/service/users.service';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.css']
})
export class OrderHistoryComponent implements OnInit{
  constructor(private usersService : UsersService) {}

  orders: Orderdetails[] = [];
  email! : any;

  ngOnInit(): void {
    this.email = localStorage.getItem('email');
    this.getUserDetails();
  }

  getUserDetails() {
    this.usersService.getOrderByEmail(this.email).subscribe({
      next : (data) => {
        console.log(JSON.stringify(data));
        this.orders = data;
      }
    })
  }
}
