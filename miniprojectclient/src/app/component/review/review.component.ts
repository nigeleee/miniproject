import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { MatsnackbarComponent } from 'src/app/matsnackbar/matsnackbar.component';
import { ReviewService } from 'src/app/service/review.service';
import { WebsocketconfigService } from 'src/app/service/websocketconfig.service';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent {
  form!: FormGroup;
  file!: File;


  constructor(private fb: FormBuilder, private router: Router, private reviewService: ReviewService, private webSocketService: WebsocketconfigService, private snackBar : MatSnackBar) {}

  ngOnInit() : void {
    this.form = this.fb.group({
      review: ['', [Validators.required]]
    });

    this.webSocketService.messages.subscribe({
      next : (message) => {
        this.snackBar.openFromComponent(MatsnackbarComponent, {
          data: { message: message },
          duration: 3000,
        });
      },
      error : (err) => {
        console.error(err);
      }
    });
  }

  onFileChange(event: any): void {
    const file = event.target.files[0];
    this.file = event.target.files[0];

  }

  submitReview(): void {
    if (this.form.valid) {
      const formData = new FormData();
      formData.append('review', this.form.value.review);
      formData.append('imageUrl', this.file);

      this.reviewService.postReview(formData).subscribe({
        next : (response) => {
          console.log('Review submitted');
          this.snackBar.openFromComponent(MatsnackbarComponent, {
            data: { message: 'Review submitted successfully' },
            duration: 3000,
          });
          this.router.navigateByUrl('/home');
        },
        error:(err) => {
          console.log(err);
        }
      });
    }
  }
}
