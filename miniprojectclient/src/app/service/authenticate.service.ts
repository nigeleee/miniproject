import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticateService {

  private apiUrl: string = 'https://kimchiproject-production.up.railway.app/api/';
  private oauthUrl: string = 'https://kimchiproject-production.up.railway.app/api/oauth2/authorization/google';

  // private apiUrl = 'http://localhost:8080/api/';
  // private oauthUrl = 'http://localhost:8080/oauth2/authorization/google'


  constructor(private http: HttpClient, private router: Router) { }

  private loginStatus = new BehaviorSubject<boolean>(this.getLoginMethod());

  get userLoggedIn() {
    return this.loginStatus.asObservable();
  }

  getLoginMethod(): boolean {
    const loginMethod = localStorage.getItem('loginMethod');
    return loginMethod === 'jwt' || loginMethod === 'oauth2';
  }

  login(email: string, password: string): Observable<any> {

    const payload = { email, password };

    return this.http.post(`${this.apiUrl}login`, payload).pipe(
      tap((response: any) => {
        const token = response.jwt;
        if (token) {
          localStorage.setItem('jwtToken', token);
          this.loginStatus.next(true);

        } else {
          console.error('Error');
        }
      })
    );
  }


  OAuth2Login() {

    localStorage.setItem('loginMethod', 'oauth2');
    this.loginStatus.next(true);
    window.location.href = this.oauthUrl;

  }

  logout(loginMethod: any): Observable<any> {
    if (loginMethod === 'jwt') {

      return this.http.post(`${this.apiUrl}logout`, {}).pipe(
        tap(() => {
          localStorage.removeItem('jwtToken');
          localStorage.removeItem('loginMethod');
          this.loginStatus.next(false);
        })
      );

    } else if (loginMethod === 'oauth2') {
      return this.http.post(`${this.apiUrl}logout/oauth2`, {}).pipe(
        tap(() => {
          localStorage.removeItem('oauthToken');
          localStorage.removeItem('loginMethod');
          this.loginStatus.next(false);
        })
      );
    } else {
      return of(null);
    }
  }
}
