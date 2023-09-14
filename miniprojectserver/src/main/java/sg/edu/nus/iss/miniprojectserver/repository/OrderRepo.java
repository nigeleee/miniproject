package sg.edu.nus.iss.miniprojectserver.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.miniprojectserver.entity.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

	List<Order> findByEmail(String email);
    
}
