package sg.edu.nus.iss.miniprojectserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.iss.miniprojectserver.entity.CartItem;


public interface CartItemRepo extends JpaRepository<CartItem, Long> {

    void deleteById(Long cartId);

    List<CartItem> findByUserUserId(Long userId);

    Optional<CartItem> findByProductProductIdAndUserUserId(Long productId, Long userId);

}
