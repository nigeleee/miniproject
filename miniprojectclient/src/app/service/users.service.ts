import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Users } from '../interface/users';
import { Accountdetails } from '../interface/accountdetails';
import { Orderdetails } from '../interface/orderdetails';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private http: HttpClient) { }


  private  apiRegisterUrl: string = 'https://kimchiproject-production.up.railway.app/api/register'
  private  apiUserUrl: string = 'https://kimchiproject-production.up.railway.app/api/user'
  private  apiOrderUrl: string = 'https://kimchiproject-production.up.railway.app.up.railway.app/api/order'

  // private apiRegisterUrl : string = "http://localhost:8080/api/register";
  // private apiUserUrl : string = "http://localhost:8080/api/user";
  // private apiOrderUrl : string = "http://localhost:8080/api/user/order";

  create(users: Users) : Observable<Users> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json');

    return this.http.post<Users>(this.apiRegisterUrl, users, {headers : headers});
  }

  getUserByEmail(email: string) : Observable<Accountdetails> {

    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Authorization', `Bearer ${localStorage.getItem('jwtToken')}`)

    return this.http.get<Accountdetails>(`${this.apiUserUrl}/${email}`, {headers : headers, withCredentials : true});

  }

  getOrderByEmail(email: string) : Observable<Orderdetails[]> {

    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Authorization', `Bearer ${localStorage.getItem('jwtToken')}`)

    return this.http.get<Orderdetails[]>(`${this.apiOrderUrl}/${email}`, {headers : headers, withCredentials : true});

  }
}
