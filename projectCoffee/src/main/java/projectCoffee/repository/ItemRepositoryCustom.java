package projectCoffee.repository;


import projectCoffee.dto.*;
import projectCoffee.entity.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto,
                                      Pageable pageable);

    Page<CoffeeItemDto> getCoffeeItemPage(ItemSearchDto itemSearchDto,
                                        Pageable pageable);

    Page<ToolsItemDto> getToolsItemPage(ItemSearchDto itemSearchDto,
                                        Pageable pageable);

    Page<EtcItemDto> getEtcItemPage(ItemSearchDto itemSearchDto,
                                    Pageable pageable);

}
//상품 조회 조건을 담고있는 itemSearchDto 객체와
//페이지정보 pageable 객체를 파라미터로 받는 getAdminItemPage 정의
