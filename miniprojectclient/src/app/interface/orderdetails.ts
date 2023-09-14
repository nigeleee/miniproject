export interface Orderdetails {
  cartItems : Cart[],
  orderId : number;
}

export interface Cart {
  name: string,
  totalPrice: number,
  quantity: number,
  productId: number,
  cartId: number,
  userId: number,
  orderId: number
}


