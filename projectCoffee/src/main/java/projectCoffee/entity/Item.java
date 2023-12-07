package projectCoffee.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import projectCoffee.constant.ItemSellStatus;
import projectCoffee.dto.ItemFormDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //상품코드

    @Column(nullable = false, length = 50)
    private String itemNm;//상품명

    @Column(name = "price",nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    private LocalDateTime regTime; //등록시간

    private LocalDateTime updateTime; //수정시간


    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }
}
