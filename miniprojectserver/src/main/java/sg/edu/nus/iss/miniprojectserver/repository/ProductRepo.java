package sg.edu.nus.iss.miniprojectserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.miniprojectserver.entity.Product;


@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

}
