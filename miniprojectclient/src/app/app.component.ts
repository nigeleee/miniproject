import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthenticateService } from './service/authenticate.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  isLoggedIn = false;

  constructor(private router : Router, private authService : AuthenticateService, private snackBar : MatSnackBar) {}

  ngOnInit(): void {

    this.authService.userLoggedIn.subscribe((status) => {
      this.isLoggedIn = status;
      if(status == true ){
        console.log('Logged In User');
      } else {
        console.log('Guest User')
      }

    });
  }

  // checkLogin() {
  //   if(localStorage.getItem('loginMethod') == 'jwt' || localStorage.getItem('loginMethod') == 'oauth2') {
  //     console.log('User is logged in with : ' + localStorage.getItem('loginMethod'))
  //     this.isLoggedIn = true;
  //   } else {
  //     console.log('Guest mode')
  //   }
  // }

  logout() {
    console.log('Logout function called')

    const loginMethod = localStorage.getItem('loginMethod');
    console.log(`Login Method: ${loginMethod}`);

    if (loginMethod) {
      this.authService.logout(loginMethod).subscribe({
        next: () => {
          console.log("Logout Successful");
          // alert('Successfully Logged Out');
          this.snackBar.open(`You have been logged out.`, 'Close', {
            duration: 3000,
          });
          this.router.navigate(['/home']);
        },
        error: (error) => {
          console.error(`${loginMethod} Logout failed`, error);
        },
      });
    }
  }
}
