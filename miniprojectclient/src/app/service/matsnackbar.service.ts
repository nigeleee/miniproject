import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatsnackbarComponent } from '../matsnackbar/matsnackbar.component';

@Injectable({
  providedIn: 'root'
})
export class MatsnackbarService {

  constructor(private snackBar: MatSnackBar) { }

  show(message: string): void {
    this.snackBar.openFromComponent(MatsnackbarComponent, {
      data: { message },
      duration: 3000
    });
  }
}
