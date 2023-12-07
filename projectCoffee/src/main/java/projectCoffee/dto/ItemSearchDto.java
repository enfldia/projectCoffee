package projectCoffee.dto;

import lombok.Getter;
import lombok.Setter;
import projectCoffee.constant.ItemSellStatus;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType;

    private ItemSellStatus itemSellStatus;

    private String searchBy;

    private String searchQuery = "";
}
