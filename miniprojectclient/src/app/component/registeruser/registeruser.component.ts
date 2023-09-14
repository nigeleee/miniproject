import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UsersService } from 'src/app/service/users.service';

@Component({
  selector: 'app-registeruser',
  templateUrl: './registeruser.component.html',
  styleUrls: ['./registeruser.component.css']
})
export class RegisteruserComponent implements OnInit, OnDestroy{
  form!: FormGroup;
  showPassword: boolean = false;
  sub$! : Subscription;
  duplicateEmailMsg!: string;

  constructor(private usersService : UsersService, private router : Router, private fb : FormBuilder) {}

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm() {
    this.form = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required]],
      phone: ['', [Validators.required, Validators.maxLength]],
      address: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength]],
    });
  }

  createUser() {
    this.sub$ = this.usersService.create(this.form.value).subscribe({
      next : (response) => {
        console.log(response);
        alert("Registered Successfully. An email has been sent to your email address. Please validate and log in again.")
        this.router.navigateByUrl('/login');
      },

      error : (err) => {
        this.duplicateEmailMsg = err.error.message;
        console.log(err);
      }
    })
  }

  ngOnDestroy(): void {
    if (this.sub$) {
      this.sub$.unsubscribe();
    }
  }

}
