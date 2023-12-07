package projectCoffee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import projectCoffee.dto.ItemSearchDto;
import projectCoffee.entity.Item;

public interface ItemRepositoryCustom {
    // 사용자 정의 인터페이스 작성
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
