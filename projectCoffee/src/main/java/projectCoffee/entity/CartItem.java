package projectCoffee.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
//Auditing 기능을 적용하기 위해 BaseEntity 상속시킴 12/6 상현
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;


    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;
}
