package projectCoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectCoffee.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
