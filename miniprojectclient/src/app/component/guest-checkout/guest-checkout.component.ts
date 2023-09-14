import { Component, NgZone, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Cart } from 'src/app/interface/cart';
import { CartService } from 'src/app/service/cart.service';

declare let paypal : any;

@Component({
  selector: 'app-guest-checkout',
  templateUrl: './guest-checkout.component.html',
  styleUrls: ['./guest-checkout.component.css']
})
export class GuestCheckoutComponent implements OnInit, OnDestroy {
  constructor(private fb: FormBuilder, private router : Router, private cartService : CartService, private zone: NgZone, private snackBar: MatSnackBar) {}

  form! : FormGroup;
  sub$! : Subscription;
  cartItems : Cart[] = [];
  totalAmount!: number;

  ngOnInit(): void {
    this.initializeForm();
    this.getCartItems();

    paypal.Buttons({
        style: {
          layout: 'vertical',
          size: 'small',
          shape: 'pill',
          color: 'gold'
        },
        createOrder: (data: any, actions: any) => {
          return actions.order.create({
            purchase_units: [{
              amount: {
                currency_code: 'SGD',
                value: this.totalAmount.toString()
              }
            }]
          });
        },
        onApprove: (data: any, actions: any) => {
          return actions.order.capture().then((details : any) => {
            this.zone.run(() => {
              this.guestCheckout();
            });
          });
        }
      }).render('#paypal-button-container');

  }

  getCartItems() {
    this.cartService.getCartItems().subscribe({
      next : (data) => {
        console.log(data);
        this.cartItems = data;
        this.totalAmount = this.calculateTotalAmount(this.cartItems);
      },
      error : (err) => {
        console.log(err);
      }
     });
  }

  initializeForm() {
    this.form = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required]],
      phone: ['', [Validators.required, Validators.maxLength]],
      address: ['', [Validators.required]]
    });
  }

  guestCheckout() {

    this.sub$ = this.cartService.guestCheckout(this.cartItems, this.form.value).subscribe({
      next : (response) => {
        this.cartService.clearGuestCart();
        console.log('Checking out by guest user');

        this.snackBar.open(`Thank You For Your Purchase!`, 'Close', {
          duration: 5000,
        });
        this.router.navigateByUrl('/home');
      },

      error : (err) => {
        console.log(err);
      }
    })
  }

  calculateTotalAmount(cartItems : Cart[]) {
    let totalAmount=0;
    for(const item of cartItems) {
      totalAmount += item.totalPrice
    }
    return totalAmount;
  }

  ngOnDestroy() {
    if (this.sub$) {
      this.sub$.unsubscribe();
    }
  }
}
