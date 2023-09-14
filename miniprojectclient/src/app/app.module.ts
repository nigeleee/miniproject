import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './component/home/home.component';
import { AboutComponent } from './component/about/about.component';
import { CartComponent } from './component/cart/cart.component';
import { CheckoutComponent } from './component/checkout/checkout.component';
import { GuestCheckoutComponent } from './component/guest-checkout/guest-checkout.component';
import { LoginComponent } from './component/login/login.component';
import { OrderHistoryComponent } from './component/order-history/order-history.component';
import { ProductbyidComponent } from './component/productbyid/productbyid.component';
import { ProductsComponent } from './component/products/products.component';
import { ReviewComponent } from './component/review/review.component';
import { RegisteruserComponent } from './component/registeruser/registeruser.component';
import { MatsnackbarComponent } from './matsnackbar/matsnackbar.component';
import { AccountDetailsComponent } from './component/account-details/account-details.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MaterialsModule } from './materials/materials.module';
import { ServiceWorkerModule } from '@angular/service-worker';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AboutComponent,
    CartComponent,
    CheckoutComponent,
    GuestCheckoutComponent,
    LoginComponent,
    OrderHistoryComponent,
    ProductbyidComponent,
    ProductsComponent,
    ReviewComponent,
    RegisteruserComponent,
    MatsnackbarComponent,
    AccountDetailsComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    MaterialsModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: !isDevMode(),
      // Register the ServiceWorker as soon as the application is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
