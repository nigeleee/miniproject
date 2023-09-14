import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Cart } from 'src/app/interface/cart';
import { Products } from 'src/app/interface/products';
import { CartService } from 'src/app/service/cart.service';
import { ProductService } from 'src/app/service/product.service';

@Component({
  selector: 'app-productbyid',
  templateUrl: './productbyid.component.html',
  styleUrls: ['./productbyid.component.css']
})
export class ProductbyidComponent implements OnInit {
  product!: Products;
  productId! : any;
  quantity: number = 1;
  toggleCart = false;
  cart : Cart[] = [];
  items! : Products[];
  sub$! : Subscription;

  constructor(private router : Router, private route: ActivatedRoute, private productService: ProductService, private cartService : CartService, private title : Title) {}

  ngOnInit(): void {
    
    this.sub$ = this.route.params.subscribe(params => {
      this.productId = params['id'];
      this.getProductById();
      this.getAllProducts();
    });
  }

  getProductById() {
    this.productService.getProductById(this.productId).subscribe({
      next: (data) => {
        console.log(data);
        this.product = data;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  getAllProducts() {
    this.productService.getProducts().subscribe({
      next : (result) => {
        this.items = result;
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  add() {
    this.quantity++;
  }

  minus() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  addToCart() {
    this.cartService.addToCart(this.productId, this.quantity).subscribe({
      next : (data) => {
        console.log(`Added ${this.quantity} of ${this.product.name} to cart.`);
        this.showCartNotification();
      }
    })
  }

  goToCart() {
    this.router.navigateByUrl('/cart')
    console.log(`Proceeding to checkout with ${this.quantity} of ${this.product.name}.`);
  }

  showCartNotification() {
    this.toggleCart = true;
    this.getCartItems();
  }

  hideCart() {
    this.toggleCart = false;
  }

  getCartItems() {
    this.cartService.getCartItems().subscribe({
      next : (data) => {
        console.log(data);
        this.cart = data;
      },
      error : (err) => {
        console.log(err);
      }
     });
  }

  removeCartItem(item : Cart) {
    console.log('Trying to remove cart item:', item);  // Debug line

    const loginMethod = localStorage.getItem('loginMethod');
    if(loginMethod == 'jwt' || loginMethod == 'oauth2') {
      this.cartService.removeUserCartItem(item.cartId).subscribe({
        next: (response) => {
          console.log('Item removed for User')
          this.getCartItems();
        },
        error: (err) => {
          console.log(err);
        }
      })
    } else {
      this.cartService.removeGuestCartItem(item.productId).subscribe({
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
      this.cartService.clearUserCart().subscribe({
        next: (response) => {
          console.log('User cart cleared');
          this.getCartItems();
        },
        error: (err) => {
          console.log(err);
        }
      });
    } else {
      this.cartService.clearGuestCart().subscribe({
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

  ngOnDestroy(): void {
    this.sub$.unsubscribe();
  }

}




