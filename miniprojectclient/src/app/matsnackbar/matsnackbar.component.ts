import { Component, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA } from '@angular/material/snack-bar';

@Component({
  selector: 'app-matsnackbar',
  templateUrl: './matsnackbar.component.html',
  styleUrls: ['./matsnackbar.component.css']
})
export class MatsnackbarComponent {
  public message: string;

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: any) { this.message = data.message }
}
