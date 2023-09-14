import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Products } from '../interface/products';
import { Cart } from '../interface/cart';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(private http : HttpClient) { }

  // private apiAddToCartUrl : string = "http://localhost:8080/api/add";
  // private apiCartUrl : string = "http://localhost:8080/api/cart";
  // private apiCheckoutUrl : string = "http://localhost:8080/api/checkout";

  private apiAddToCartUrl : string = "https://kimchiproject-production.up.railway.app/api/add";
  private apiCartUrl : string = "https://kimchiproject-production.up.railway.app/api/cart";
  private apiCheckoutUrl : string = "https://kimchiproject-production.up.railway.app/api/checkout";


  addToCart(productId: number, quantity: number) : Observable<Products> {

    let headers = new HttpHeaders()
      .set('Content-Type', 'application/json');
    const params = new HttpParams()
      .set('productId', productId)
      .set('quantity', quantity.toString());

    const token = localStorage.getItem('jwtToken');

    if (token != null) {
      headers = headers.set('Authorization', `Bearer ${token}`);

    }

    return this.http.post<Products>(this.apiAddToCartUrl, null, {headers : headers, params : params, withCredentials: true});
  }

  getCartItems() : Observable<Cart[]> {
    let headers = new HttpHeaders()
    .set('Content-Type', 'application/json');

    const token = localStorage.getItem('jwtToken');

    if (token) {
        headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return this.http.get<Cart[]>(this.apiCartUrl, {headers : headers, withCredentials: true});
  }

  removeGuestCartItem(productId : number): Observable<any> {

    return this.http.delete(`${this.apiCartUrl}/delete/guest/${productId}`, { withCredentials: true });

  }

  removeUserCartItem(cartId : number): Observable<any> {

    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders()
    .set('Authorization', `Bearer ${token}`);

    return this.http.delete(`${this.apiCartUrl}/delete/user/${cartId}`, {headers : headers, withCredentials: true });

  }

  clearGuestCart(): Observable<any> {
    return this.http.delete(`${this.apiCartUrl}/clear-guest`, { withCredentials: true });
  }

  clearUserCart(): Observable<any> {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`);

    return this.http.delete(`${this.apiCartUrl}/clear-user`, { headers: headers, withCredentials: true });
  }


  userCheckout(cartItems : Cart[]): Observable<any> {

    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`);

    return this.http.post(`${this.apiCheckoutUrl}/user`, cartItems, {headers : headers, withCredentials: true });
  }

  guestCheckout(cartItems : Cart[], orderDetails: any) : Observable<any> {

    const payload = {cartItems, orderDetails}

    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json');

    return this.http.post(`${this.apiCheckoutUrl}/guest`, payload, {headers : headers, withCredentials: true })

  }
}
