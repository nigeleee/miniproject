package sg.edu.nus.iss.miniprojectserver.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import sg.edu.nus.iss.miniprojectserver.entity.Product;
import sg.edu.nus.iss.miniprojectserver.entity.User;
import sg.edu.nus.iss.miniprojectserver.model.CartItemSummary;
import sg.edu.nus.iss.miniprojectserver.model.CheckoutDetails;
import sg.edu.nus.iss.miniprojectserver.model.CheckoutGuest;
import sg.edu.nus.iss.miniprojectserver.service.CartItemService;
import sg.edu.nus.iss.miniprojectserver.service.ProductService;
import sg.edu.nus.iss.miniprojectserver.service.UserServiceInterface;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserServiceInterface userService;

    @GetMapping(path = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getAllProducts(HttpSession session) {


        List<Product> result = productService.getAllProducts();

        if (result != null) {
            return ResponseEntity.ok().body(result);

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping(path = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProductById(@PathVariable Long id, HttpSession session) {

        try {
            Product result = productService.getProductById(id);
            return ResponseEntity.ok().body(result);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addProductToCart(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {

        User user = userService.getCurrentUser(session);
        // System.out.println(">>>>>>>>>>>>>>>>>>>>. User:" + user);

        if (user != null) {
            
            cartItemService.addToUserCart(productId, user, quantity);
            // System.out.println(">>>>>>>>>>>>>>>>>>>>> Products added for User:" + user + "added" + quantity + " of " + productId);

            return ResponseEntity.ok("{\"message\":\"Product added to user's cart\"}");
        } else {
            
            Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = new HashMap<>();
            }
            int existingQuantity = cartItems.getOrDefault(productId, 0);
            cartItems.put(productId, existingQuantity + quantity);
            session.setAttribute("cartItems", cartItems);
            System.out.println("Session ID in addProductToCart: " + session.getId());

            return ResponseEntity.ok("{\"message\":\"Product added to guest's cart\"}");

        }
    }

    
    @GetMapping(path = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CartItemSummary>> getAllCartItems(HttpSession session) {

        User user = userService.getCurrentUser(session);
        // System.out.println(">>>>>>>>>>>>>>>>>>>> current User: " + user);

        if (user != null) {
            
            List<CartItemSummary> cartItems = cartItemService.getCartItemsForUser(user.getUserId());
            // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>CartItems For Logged In User: " + cartItems);

            return ResponseEntity.ok(cartItems);
        } else {

            Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute("cartItems");

            if (cartItems == null || cartItems.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>()); 
            }

            List<CartItemSummary> cartItemSummaries = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
                Long productId = entry.getKey();
                int quantity = entry.getValue();

                // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Product ID: " + productId + " Quantity: " + quantity);

                Product product = productService.getProductById(productId);

                System.out.println("Product: " + product);

                if (product != null) {
                    CartItemSummary summary = new CartItemSummary();
                    summary.setName(product.getName());
                    summary.setTotalPrice(product.getPrice() * quantity);
                    summary.setQuantity(quantity);
                    summary.setProductId(product.getProductId());
                    cartItemSummaries.add(summary);
                }
            }

            return ResponseEntity.ok(cartItemSummaries);
        }

    }

    @DeleteMapping("/cart/delete/guest/{productId}")
    public ResponseEntity<String> removeGuestCartItem(HttpSession session, @PathVariable Long productId) {
        // Retrieve cart items from the session
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute("cartItems");

        // System.out.println(">>>>>>>>>>>>> Cart Items: " + cartItems);

        if (cartItems != null && cartItems.containsKey(productId)) {

            // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>Removed product with ID: " + productId);

            cartItems.remove(productId);
            session.setAttribute("cartItems", cartItems);

            return ResponseEntity.ok("{\"message\":\"Cart item removed\"}");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"Product not found in cart\"}");
        }
    }

    @DeleteMapping("/cart/delete/user/{cartId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long cartId) {
        cartItemService.removeUserCartItem(cartId);

        return ResponseEntity.ok("{\"message\":\"Cart item removed\"}");
    }

    @DeleteMapping("/cart/clear-guest")
    public ResponseEntity<String> clearCart(HttpSession session) {
        session.removeAttribute("cartItems");

        return ResponseEntity.ok("{\"message\":\"Guest cart cleared\"}");
    }


    @DeleteMapping("/cart/clear-user")
    public ResponseEntity<String> clearUserCart(HttpSession session) {
        User user = userService.getCurrentUser(session);  // Make sure to inject HttpSession if needed

        cartItemService.clearUserCart(user.getUserId());

        return ResponseEntity.ok("{\"message\":\"User cart cleared\"}");

    }


    @PostMapping("/checkout/user")
    public ResponseEntity<String> userCheckout(@RequestBody List<CheckoutDetails> checkoutItems, HttpSession session) {

        try {
        
            User user = userService.getCurrentUser(session);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"User not authenticated\"}");
            }

            cartItemService.createOrderForUser(checkoutItems, user);
            cartItemService.clearUserCart(user.getUserId());


            // System.out.println("Cart items and user details: " + checkoutItems + user);
            return ResponseEntity.ok("{\"message\":\"User Checkout Completed\"}");

        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"An error occurred during checkout\"}");
        }
    }

    @PostMapping(path = "/checkout/guest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> guestCheckout(@RequestBody CheckoutGuest checkoutItems, HttpSession session) {
        
        try {
            // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> checkout items: " + checkoutItems);
            cartItemService.createOrderForGuest(checkoutItems.getCartItems(), checkoutItems.getOrderDetails());
            session.removeAttribute("cartItems");
            
            return new ResponseEntity<>("{\"message\":\"Guest Checkout Completed\"}", HttpStatus.OK);
        
        } catch (Exception e) {
            
            return new ResponseEntity<>("{\"message\":\"An error occurred during checkout\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}


