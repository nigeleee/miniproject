import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Subscription } from 'rxjs';
import { Products } from 'src/app/interface/products';
import { AuthenticateService } from 'src/app/service/authenticate.service';
import { ProductService } from 'src/app/service/product.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent {
  sub$! : Subscription;
  products: Products[] = [];

  constructor(private http: HttpClient, private authService: AuthenticateService, private productService: ProductService){}

  ngOnInit(): void {
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

  ngOnDestroy(): void {
    if (this.sub$) {
      this.sub$.unsubscribe();
    }
  }
}
