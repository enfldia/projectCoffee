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

    private LocalDateTime orderTime; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태



    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) //orderItems의 order에 의해 관리된다.
    private List<OrderItem> orderItems = new ArrayList<>();
}
