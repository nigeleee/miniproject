package sg.edu.nus.iss.miniprojectserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import sg.edu.nus.iss.miniprojectserver.entity.Order;
import sg.edu.nus.iss.miniprojectserver.entity.User;
import sg.edu.nus.iss.miniprojectserver.service.OrderService;
import sg.edu.nus.iss.miniprojectserver.service.UserService;

@RestController
@RequestMapping(path = "/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping(path="/user/{email}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getCurrentUserByEmail (@PathVariable String email, HttpSession session) {
        
        try {
            User user = userService.getCurrentUser(session);
            
            return ResponseEntity.ok().body(user);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();

        }
    }   
    
    @GetMapping(path = "/user/order/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable String email, HttpSession session) {
        
        try {
            
            List<Order> orders = orderService.getOrderByEmail(email);
            // System.out.println("----------------------------->" + orders.toString());

            return ResponseEntity.ok().body(orders);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
} 

