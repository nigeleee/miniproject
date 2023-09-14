import { ProductService } from './../../service/product.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Cart } from 'src/app/interface/cart';
import { Products } from 'src/app/interface/products';
import { AuthenticateService } from 'src/app/service/authenticate.service';
import { CartService } from 'src/app/service/cart.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit, OnDestroy{
  cartItems: Cart[] = [];
  productId! : number;
  quantity! : number;
  products!: Products[];
  sub$!: Subscription;

  constructor(private cartService: CartService, private productService: ProductService, private authService: AuthenticateService, private router : Router) { }

  ngOnInit(): void {
    this.getCartItems();
    this.getAllProducts();

  }

  getAllProducts() {
    this.sub$ = this.productService.getProducts().subscribe({
      next : (data) => {
        console.log(JSON.stringify(data));
        this.products = data;
      },
      error: (err) => {
        console.log(err);
      }
    })
  }


  getCartItems() {
    this.sub$ = this.cartService.getCartItems().subscribe({
      next : (data) => {
        console.log(data);
        this.cartItems = data;
      },
      error : (err) => {
        console.log(err);
      }
     });
  }

  deleteCartItem(item : Cart) {
    const loginMethod = localStorage.getItem('loginMethod');
    if(loginMethod == 'jwt' || loginMethod == 'oauth2') {
      this.sub$ = this.cartService.removeUserCartItem(item.cartId).subscribe({
        next: (response) => {
          console.log('Item removed for User')
          this.getCartItems();
        },
        error: (err) => {
          console.log(err);
        }
      })
    } else {
      this.sub$ = this.cartService.removeGuestCartItem(item.productId).subscribe({
        next : (response) => {
          console.log('Item removed for Guest')
          this.getCartItems();
        },
        error : (err) => {
          console.log(err);
        }
      })
    }
  }

  clearCart() {
    const loginMethod = localStorage.getItem('loginMethod');

    if (loginMethod === 'jwt' || loginMethod === 'oauth2') {
      this.sub$ = this.cartService.clearUserCart().subscribe({
        next: (response) => {
          console.log('User cart cleared');
          this.getCartItems();
        },
        error: (err) => {
          console.log(err);
        }
      });
    } else {
      this.sub$ = this.cartService.clearGuestCart().subscribe({
        next: (response) => {
          console.log('Guest cart cleared');
          this.getCartItems();
        },
        error: (err) => {
          console.log(err);
        }
      });
    }
  }

  proceedToCheckout(): void {
    const loginMethod = localStorage.getItem('loginMethod');

    if(loginMethod == 'jwt' || loginMethod == 'oauth2') {
      this.router.navigateByUrl('/checkout');

    } else {
      this.router.navigateByUrl('/guest-checkout');
   }
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe();
  }
}
