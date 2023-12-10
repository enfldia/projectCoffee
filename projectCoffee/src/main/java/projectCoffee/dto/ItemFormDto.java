package projectCoffee.dto;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import projectCoffee.constant.ItemSellStatus;
import projectCoffee.entity.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

//    아이템 카테고
    private String itemCategory;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }
    //현재 ItemFormDto 객체를 사용해서 Item 엔티티를 생성

    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
    //주어진 Item 엔티티를 사용하여 ItemFormDto를 생성하는 정적 메서드입니다.
    //엔티티의 필드 값을 DTO로 매핑합니다.
    }

