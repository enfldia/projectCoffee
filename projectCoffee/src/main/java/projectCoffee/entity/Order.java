package projectCoffee.entity;

import lombok.Getter;
import lombok.Setter;
import projectCoffee.constant.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
//Auditing 기능을 적용하기 위해 BaseEntity 상속시킴 12/6 상현
public class Order extends BaseEntity{

    @Id
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태



    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) //orderItems의 order에 의해 관리된다.
    private List<OrderItem> orderItems = new ArrayList<>();
    //외래키가(order_id)가 order_item 테이블에 있으므로
    //연관관계 주인은 OrderItem
    //OrderItem에 Order에 의해서 관리된다.

    private void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem); //orderItem 객체를 order 객체의 orderItems 추가
        orderItem.setOrder(this); //양방향 연관관계이므로 orderItem 객체 order 객체를 세팅
    }

    public static Order creatOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);

        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    } //총주문금액을 구하는 메소드 입니다.

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL; //order => CANCEL
        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}
