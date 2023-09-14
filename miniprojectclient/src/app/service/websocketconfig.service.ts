import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketconfigService {

  private socket!: WebSocket;
  public messages = new Subject<string>();

  constructor() {
    this.connect();
  }

  connect() {
    const email = localStorage.getItem('email');
    this.socket = new WebSocket(`ws://localhost:8080/websocket/${email}`);

    this.socket.addEventListener('open', (event) => {
      console.log('Connected:', event);
    });

    this.socket.addEventListener('message', (event) => {
      console.log('Listening on server:', event.data);
      this.messages.next(event.data);
    });
  }

  sendMessage(message: string) {
    if (this.socket) {
      this.socket.send(message);
    }
  }
}
