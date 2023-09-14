import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Products } from '../interface/products';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  private apiProductsUrl: string = 'https://kimchiproject-production.up.railway.app/api/products';

  // private apiProductsUrl : string = "http://localhost:8080/api/products";

  getProducts() : Observable<Products[]> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json');

    return this.http.get<Products[]>(this.apiProductsUrl, {headers : headers});
  }

  getProductById(productId: number) : Observable<Products> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json');

    return this.http.get<Products>(`${this.apiProductsUrl}/${productId}`, {headers : headers});
  }
}
