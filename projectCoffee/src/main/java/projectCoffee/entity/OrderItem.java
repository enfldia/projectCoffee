package projectCoffee.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter

public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문가격

    private int count; //수량

//    private LocalDateTime regTime;
//      Auditing 으로 시간 관련된 것 들 삭제
//    private LocalDateTime updateTime;
public static OrderItem createOderItem(Item item, int count) {
    OrderItem orderItem = new OrderItem();
    orderItem.setItem(item);
    orderItem.setCount(count);
    orderItem.setOrderPrice(item.getPrice());
    item.removeStock(count);
    return orderItem;
}

    public int getTotalPrice() {
        return orderPrice*count;
    }

    public void cancel() {
        this.getItem().addStock(count);
    }
}
