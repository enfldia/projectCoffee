package projectCoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectCoffee.entity.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
