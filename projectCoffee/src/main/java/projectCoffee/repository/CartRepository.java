package projectCoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectCoffee.entity.Cart;

public interface CartRepository extends JpaRepository<Cart,Long> {

}
