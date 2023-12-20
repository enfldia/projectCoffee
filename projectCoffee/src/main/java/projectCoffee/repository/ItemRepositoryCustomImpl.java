package projectCoffee.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;
import projectCoffee.constant.ItemSellStatus;
import projectCoffee.dto.*;
import projectCoffee.entity.Item;
import projectCoffee.entity.QItem;
import projectCoffee.entity.QItemImg;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;
    //동적쿼리를 생성하기 위해서 JPAQueryFactory 선언
    public ItemRepositoryCustomImpl(EntityManager em){
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
        } else if(StringUtils.equals("createdBy",searchQuery)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }
     @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
         List<Item> content = queryFactory
                 .selectFrom(QItem.item) //item 엔티티 데이터를 선택
                 .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                         searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                         searchByLike(itemSearchDto.getSearchBy(),itemSearchDto.getSearchQuery())
                         )
                 .orderBy(QItem.item.id.desc())//아이템 id의 역순방향
                 .offset(pageable.getOffset())//페이지 시작 오프셋 설정
                 .limit(pageable.getPageSize())//페이지의 크기를 설정
                 .fetch();// 쿼리를 실행하고 결과를 리스트로 반환

         //토탈 카운트 조회 - 상품의 총갯수를 조회
         long total = queryFactory.select(Wildcard.count).from(QItem.item)// 테이블에서 카운터를 조회하는 쿼리
                 .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                         searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                         searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                 .fetchOne();// 카운터 결과를 단일 값으로 반환
         //Wildcard.count - QueryDsl 에서 제공하는 쿼리 결과의 행수


         return  new PageImpl<>(content,pageable,total);
         //PageImpl 를 사용하여 페이징된 결과를 Page<Item> 형태로 반환

     }

     private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ?
                null : QItem.item.itemNm.like("%" + searchQuery + "%");
     }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto,
                                              Pageable mainPageable){
        QItem item = QItem.item;
         QItemImg itemImg = QItemImg.itemImg;
        //QItem과 QitemImg를 사용해서 QueryDsl에서 사용할 수 있는 객체 정의
         List<MainItemDto> content = queryFactory
                 .select(
                         new QMainItemDto(
                                 item.id,
                                 item.itemNm,
                                 item.itemDetail,
                                 itemImg.imgUrl,
                                 item.price)
                 )
                 .from(itemImg)
                 .join(itemImg.item, item) //itemImg /item 조인하여
                 .where(itemImg.repImgYn.eq("Y")) // 대표이미지 와
                 .where(itemNmLike(itemSearchDto.getSearchQuery())) //상품명 검색
                 .orderBy(item.id.desc()) //상품id를 기준으로 내림차순 정렬(최신이 위로)
                 .offset(mainPageable.getOffset())
                 .limit(mainPageable.getPageSize()) //페이지 네이션처리
                 .fetch(); //쿼리 결과를 리스트로 반환
         long total = queryFactory
                 .select(Wildcard.count) //전체 갯수 확인
                 .from(itemImg) //itemImg 테이블에서
                 .join(itemImg.item, item) //itemImg 와 item 조인해서
                 .where(itemImg.repImgYn.eq("Y")) //대표이미지와
                 .where(itemNmLike(itemSearchDto.getSearchQuery())) //상품명 검색
                 .fetchOne() //궈리 결과를 단일 값으로 반환
                 ; //전체 갯수를 조회
         return  new PageImpl<>(content, mainPageable, total);
         //PageImple 를 사용하여 페이지 네이션 될 결과를 page<MainItemDto> 형태로 반환
     }
    @Override
    public Page<CoffeeItemDto> getCoffeeItemPage(ItemSearchDto itemSearchDto,
                                                 Pageable coffeePageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        //QItem과 QitemImg를 사용해서 QueryDsl에서 사용할 수 있는 객체 정의
        List<CoffeeItemDto> content = queryFactory
                .select(
                        new QCoffeeItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.itemCategory
                        )
                )
                .from(itemImg)
                .join(itemImg.item, item) //itemImg /item 조인하여
                .where(
                        itemImg.repImgYn.eq("Y"),
                        itemImg.item.itemCategory.like("COFFEE")
                )// 대표이미지 와
//                .where(itemImg.item.itemCategory.like("COFFEE")) //아이템이미지에 있는 아이템 필드를 이용해서 아이템 엔티티에 접근.
                                                                     // 아이템 엔티티의 필드값인 아이템 카테고리의 값을 검색
                .orderBy(item.id.desc()) //상품id를 기준으로 내림차순 정렬(최신이 위로)
                .offset(coffeePageable.getOffset())
                .limit(coffeePageable.getPageSize()) //페이지 네이션처리
                .fetch(); //쿼리 결과를 리스트로 반환
        long total = queryFactory
                .select(Wildcard.count) //전체 갯수 확인
                .from(itemImg) //itemImg 테이블에서
                .join(itemImg.item, item) //itemImg 와 item 조인해서
                .where(
                        itemImg.repImgYn.eq("Y"),
                        itemImg.item.itemCategory.like("COFFEE")
                ) //대표이미지와
//                .where(itemImg.item.itemCategory.like("COFFEE"))
                .where(itemNmLike(itemSearchDto.getSearchQuery())) //상품명 검색
                .fetchOne() //궈리 결과를 단일 값으로 반환
                ; //전체 갯수를 조회
        return  new PageImpl<>(content, coffeePageable, total);
        //PageImple 를 사용하여 페이지 네이션 될 결과를 page<MainItemDto> 형태로 반환
    }

    @Override
    public Page<EtcItemDto> getEtcItemPage(ItemSearchDto itemSearchDto,
                                                 Pageable etcPageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        //QItem과 QitemImg를 사용해서 QueryDsl에서 사용할 수 있는 객체 정의
        List<EtcItemDto> content = queryFactory
                .select(
                        new QEtcItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.itemCategory
                        )
                )
                .from(itemImg)
                .join(itemImg.item, item) //itemImg /item 조인하여
                .where(
                        itemImg.repImgYn.eq("Y"),
                        itemImg.item.itemCategory.like("ETC")
                ) // 대표이미지 와
//                .where(itemImg.item.itemCategory.like("ETC")) //아이템이미지에 있는 아이템 필드를 이용해서 아이템 엔티티에 접근.
                // 아이템 엔티티의 필드값인 아이템 카테고리의 값을 검색
                .orderBy(item.id.desc()) //상품id를 기준으로 내림차순 정렬(최신이 위로)
                .offset(etcPageable.getOffset())
                .limit(etcPageable.getPageSize()) //페이지 네이션처리
                .fetch(); //쿼리 결과를 리스트로 반환
        long total = queryFactory
                .select(Wildcard.count) //전체 갯수 확인
                .from(itemImg) //itemImg 테이블에서
                .join(itemImg.item, item) //itemImg 와 item 조인해서
                .where(
                        itemImg.repImgYn.eq("Y"),
                        itemImg.item.itemCategory.like("ETC")
                )//대표이미지와
//                .where(itemImg.item.itemCategory.like("ETC"))
                .where(itemNmLike(itemSearchDto.getSearchQuery())) //상품명 검색
                .fetchOne() //궈리 결과를 단일 값으로 반환
                ; //전체 갯수를 조회
        return  new PageImpl<>(content, etcPageable, total);
        //PageImple 를 사용하여 페이지 네이션 될 결과를 page<MainItemDto> 형태로 반환
    }

    @Override
    public Page<ToolsItemDto> getToolsItemPage(ItemSearchDto itemSearchDto,
                                                 Pageable toolsPageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        //QItem과 QitemImg를 사용해서 QueryDsl에서 사용할 수 있는 객체 정의
        List<ToolsItemDto> content = queryFactory
                .select(
                        new QToolsItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.itemCategory
                        )
                )
                .from(itemImg)
                .join(itemImg.item, item) //itemImg /item 조인하여
                .where(
                        itemImg.repImgYn.eq("Y"),
                        itemImg.item.itemCategory.like("TOOLS")
                ) // 대표이미지 와
//                .where(itemImg.item.itemCategory.like("TOOLS")) //아이템이미지에 있는 아이템 필드를 이용해서 아이템 엔티티에 접근.
                // 아이템 엔티티의 필드값인 아이템 카테고리의 값을 검색
                .orderBy(item.id.desc()) //상품id를 기준으로 내림차순 정렬(최신이 위로)
                .offset(toolsPageable.getOffset())
                .limit(toolsPageable.getPageSize()) //페이지 네이션처리
                .fetch(); //쿼리 결과를 리스트로 반환
        long total = queryFactory
                .select(Wildcard.count) //전체 갯수 확인
                .from(itemImg) //itemImg 테이블에서
                .join(itemImg.item, item) //itemImg 와 item 조인해서
                .where(
                        itemImg.repImgYn.eq("Y"),
                        itemImg.item.itemCategory.like("TOOLS")
                ) //대표이미지와
//                .where(itemImg.item.itemCategory.like("TOOLS"))
                .where(itemNmLike(itemSearchDto.getSearchQuery())) //상품명 검색
                .fetchOne() //궈리 결과를 단일 값으로 반환
                ; //전체 갯수를 조회
        return  new PageImpl<>(content, toolsPageable, total);
        //PageImple 를 사용하여 페이지 네이션 될 결과를 page<MainItemDto> 형태로 반환
    }

}
