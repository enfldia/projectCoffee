package projectCoffee.repository;

import com.querydsl.core.Query;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;
import projectCoffee.constant.ItemSellStatus;
import projectCoffee.dto.ItemSearchDto;
import projectCoffee.entity.Item;
import projectCoffee.entity.QItem;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;
    //동적쿼리를 생성하기 위해서 JPAQueryFactory 선언
    private ItemRepositoryCustom(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    //JPAQueryFactory의 생성자로 EntityManager 객체를 넣어줌

    //    searchSellStatusEq(): 상품 판매 조건이 전체(null)일 경우는 null를 리턴합니다.
//    결과값이 null이면 where절에서 해당 조건은 무시됩니다.
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else  if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else  if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else  if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }
    private BooleanExpression searchByLike (String searchBy,String searchQuery){

        if(StringUtils.equals("itemNm",searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createBy",searchQuery)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }
     @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
         QueryResults<Item> results = queryFactory
                 .selectFrom(QItem.item) //item 엔티티 데이터를 선택
                 .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                         searchSellStatusEq(itemSearchDto.getItemSellStatus()),
                         searchByLike(itemSearchDto.getSearchBy(),itemSearchDto.getSearchQuery())
                         )
                 .orderBy(QItem.item.id.desc())//아이템 id의 역순방향
                 .offset(pageable.getOffset())//페이지 시작 오프셋 설정
                 .limit(pageable.getPageSize())//페이지의 크기를 설정
                 .fetchResults();// 쿼리를 실행하고 결과를 리스트로 반환

     }
}
