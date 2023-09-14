import { Reviews } from './../interface/reviews';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor(private http: HttpClient) { }

  // private reviewApiUrl: string = 'http://localhost:8080/api/submit-review';
  // private getReviewApiUrl: string = 'http://localhost:8080/api/reviews';
  private reviewApiUrl: string = 'https://kimchiproject-production.up.railway.app/api/submit-review';
  private getReviewApiUrl: string = 'https://kimchiproject-production.up.railway.app/api/reviews';

  postReview(formData:FormData): Observable<any> {

    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders()
    .set('Authorization', `Bearer ${token}`)

    return this.http.post(this.reviewApiUrl, formData, {headers : headers, withCredentials: true });
  }

  getReviews() : Observable<Reviews[]> {

    const headers = new HttpHeaders()
    .set('Content-Type', 'application/json')

    return this.http.get<Reviews[]>(this.getReviewApiUrl, {headers : headers, withCredentials: true });
  }
}
