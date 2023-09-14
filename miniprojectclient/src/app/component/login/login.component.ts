import { HttpClient } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticateService } from 'src/app/service/authenticate.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  form!: FormGroup;
  user: any;
  loggedIn: any;
  loginMethod!: any;
  sub$!: Subscription;
  emailErrorMsg: string = '';
  passwordErrorMsg: string = '';
  showPassword: boolean = false;

  constructor(private formBuilder: FormBuilder, private authService: AuthenticateService, private router: Router, private http: HttpClient) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

  }

  onSubmit() {
    if (this.form.invalid) {
      return;

    }

    const input = this.form.value;

    this.authService.login(input.email, input.password)
      .subscribe({
        next : (response) => {
          console.log("User is logged in");
          localStorage.setItem('loginMethod', 'jwt');
          localStorage.setItem('email', input.email);
          this.router.navigate(['/products']);

          this.emailErrorMsg = '';
          this.passwordErrorMsg = '';
        },
        error: (err) => {
          if (err.status === 401) {
            const message = err.error.message.toLowerCase();
            if (message.includes('email')) {
              this.emailErrorMsg = 'Invalid email';
            } else if (message.includes('password')) {
              this.passwordErrorMsg = 'Invalid password';
            } else {
              this.emailErrorMsg = 'An unexpected error occurred';
              this.passwordErrorMsg = 'An unexpected error occurred';
            }
          }
          console.log(err);
        }
      }
    );

  }

  oauth2Login() {
    console.log("Logging in with Oauth2")
    this.authService.OAuth2Login();
  }

  ngOnDestroy(): void {
    if (this.sub$) {
      this.sub$.unsubscribe();
    }
  }

}
