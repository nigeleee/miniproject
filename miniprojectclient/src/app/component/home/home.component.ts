import { Component, OnInit } from '@angular/core';
import { Reviews } from 'src/app/interface/reviews';
import { ReviewService } from 'src/app/service/review.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private reviewService: ReviewService){}

  reviews: Reviews[]= [];

  ngOnInit(): void {
    this.getReviews();
  }

  getReviews() {
    this.reviewService.getReviews().subscribe({
      next : (data) => {
        console.log(data);
        this.reviews = data;
      }
    })
  }
}
