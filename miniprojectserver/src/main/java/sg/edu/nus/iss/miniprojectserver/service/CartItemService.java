package sg.edu.nus.iss.miniprojectserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import sg.edu.nus.iss.miniprojectserver.entity.CartItem;
import sg.edu.nus.iss.miniprojectserver.entity.Order;
import sg.edu.nus.iss.miniprojectserver.entity.Product;
import sg.edu.nus.iss.miniprojectserver.entity.User;
import sg.edu.nus.iss.miniprojectserver.model.CartItemSummary;
import sg.edu.nus.iss.miniprojectserver.model.CheckoutDetails;
import sg.edu.nus.iss.miniprojectserver.model.OrderDetails;
import sg.edu.nus.iss.miniprojectserver.repository.CartItemRepo;
import sg.edu.nus.iss.miniprojectserver.repository.OrderRepo;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private EmailSenderService emailSenderService;

    public void addToUserCart(Long productId, User user, Integer quantity) {

        Optional<CartItem> existingCartItem = cartItemRepo.findByProductProductIdAndUserUserId(productId,
                user.getUserId());

        if (existingCartItem.isPresent()) {
            // Update quantity of existing cart item
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepo.save(cartItem);
        } else {
            // Create a new cart item
            CartItem cartItem = new CartItem();
            cartItem.setProduct(productService.getProductById(productId));
            cartItem.setQuantity(quantity);
            cartItem.setUser(user);
            cartItemRepo.save(cartItem);
        }
    }

    // testing persistance to cart_item in sql
    // public void testingAdd(Long productId, Integer quantity) {

    // System.out.println("----------------------------------------->Adding product
    // with productId: " + productId
    // + " to cart with quantity: " + quantity);

    // // Create a new cart item
    // Product product = productService.getProductById(productId);
    // CartItem cartItem = new CartItem();
    // cartItem.setProduct(product);
    // cartItem.setQuantity(quantity);

    // cartItemRepo.save(cartItem);

    // System.out.println("------------------------------------------>CartItem
    // saved: " + cartItem.getCartId());

    // }

    public List<CartItem> getAllCartItems() {
        return cartItemRepo.findAll();
    }

    public List<CartItemSummary> getCartItemsForUser(Long userId) {
        List<CartItemSummary> cartItemSummaries = new ArrayList<>();

        List<CartItem> cartItems = cartItemRepo.findByUserUserId(userId);

        for (CartItem cartItem : cartItems) {
            CartItemSummary summary = new CartItemSummary();
            summary.setName(cartItem.getProduct().getName());
            summary.setTotalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            summary.setQuantity(cartItem.getQuantity());
            summary.setCartId(cartItem.getCartId());
            summary.setProductId(cartItem.getProductId());
            summary.setUserId(userId);
            cartItemSummaries.add(summary);
        }

        return cartItemSummaries;
    }

    public void removeUserCartItem(Long cartId) {
        cartItemRepo.deleteById(cartId);
    }

    public void clearUserCart(Long userId) {
        List<CartItem> cartItems = cartItemRepo.findByUserUserId(userId);
        cartItemRepo.deleteAll(cartItems);
    }

    public void createOrderForUser(List<CheckoutDetails> checkoutItems, User user) throws MessagingException {

        try {
            Order order = new Order();
            order.setFirstName(user.getFirstName());
            order.setLastName(user.getLastName());
            order.setEmail(user.getEmail());
            order.setPhone(user.getPhone());
            order.setAddress(user.getAddress());
            order.setOrderId(new Random().nextLong(100000));

            List<CartItem> savedCartItems = new ArrayList<>();
            for (CheckoutDetails cartItem : checkoutItems) {
                Product product = productService.getProductById(cartItem.getProductId());
                if (product != null) {
                    CartItem savedCartItem = new CartItem();
                    savedCartItem.setProduct(product);
                    savedCartItem.setQuantity(cartItem.getQuantity());
                    savedCartItems.add(savedCartItem);
                }
            }

            order.setCartItems(savedCartItems);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> Saved Cart Items: " + savedCartItems);

            orderRepo.save(order);

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> Order is: " + order);

            emailSenderService.sendReceiptEmail(order, order.getEmail().toString());

        } catch (Exception e) {
            
            throw new RuntimeException("An error occurred during user checkout.", e);
        }

    }

    public void createOrderForGuest(List<CheckoutDetails> checkoutItems, OrderDetails orderDetails) {
        try {

            Order order = new Order();
            order.setFirstName(orderDetails.getFirstName());
            order.setLastName(orderDetails.getLastName());
            order.setEmail(orderDetails.getEmail());
            order.setPhone(orderDetails.getPhone());
            order.setAddress(orderDetails.getAddress());
            order.setOrderId(new Random().nextLong(100000));

            List<CartItem> savedCartItems = new ArrayList<>();
            for (CheckoutDetails guestCartItem : checkoutItems) {

                Product product = productService.getProductById(guestCartItem.getProductId());
                if (product != null) {
                    CartItem savedCartItem = new CartItem();
                    savedCartItem.setProduct(product);
                    savedCartItem.setQuantity(guestCartItem.getQuantity());
                    savedCartItems.add(savedCartItem);
                }
            }

            order.setCartItems(savedCartItems);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> Saved Cart Items: " + savedCartItems);

            orderRepo.save(order);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> Order is: " + order);

            emailSenderService.sendReceiptEmail(order, order.getEmail().toString());

            // for (CheckoutGuest guestCartItem : checkoutItems) {
            // Long productId = guestCartItem.getProductId();
            // int quantity = guestCartItem.getQuantity();
            // productService.updateInventory(productId, -quantity);
            // }
        } catch (Exception e) {
            // Log the exception
            throw new RuntimeException("An error occurred during guest checkout.", e);
        }
    }

}
