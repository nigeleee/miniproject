package sg.edu.nus.iss.miniprojectserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.miniprojectserver.entity.Order;
import sg.edu.nus.iss.miniprojectserver.repository.OrderRepo;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    public List<Order> getOrderByEmail(String email) {
  
        return orderRepo.findByEmail(email);

    }
}
