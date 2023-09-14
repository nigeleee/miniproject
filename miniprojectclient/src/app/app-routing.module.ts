import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { RegisteruserComponent } from './component/registeruser/registeruser.component';
import { LoginComponent } from './component/login/login.component';
import { ProductsComponent } from './component/products/products.component';
import { ProductbyidComponent } from './component/productbyid/productbyid.component';
import { CartComponent } from './component/cart/cart.component';
import { CheckoutComponent } from './component/checkout/checkout.component';
import { AboutComponent } from './component/about/about.component';
import { GuestCheckoutComponent } from './component/guest-checkout/guest-checkout.component';
import { ReviewComponent } from './component/review/review.component';
import { AccountDetailsComponent } from './component/account-details/account-details.component';
import { OrderHistoryComponent } from './component/order-history/order-history.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' }, // redirect to `home` route
  { path: 'home', component: HomeComponent, title: 'Home' },
  { path: 'register', component: RegisteruserComponent, title: 'Sign Up' },
  { path: 'login', component: LoginComponent, title: 'Login' },
  { path: 'products', component: ProductsComponent, title: 'Products' },
  { path: 'product/:id', component: ProductbyidComponent},
  { path: 'cart', component: CartComponent, title: 'Shopping Cart'},
  { path: 'checkout', component: CheckoutComponent, title: 'Checkout'},
  { path: 'about', component: AboutComponent, title: 'About'},
  { path: 'guest-checkout', component: GuestCheckoutComponent, title: 'Checkout'},
  { path: 'submit-review', component: ReviewComponent, title: 'Submit Review'},
  { path: 'account-details', component: AccountDetailsComponent, title: 'Account Information'},
  { path: 'order-history', component: OrderHistoryComponent, title: 'Order History'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
