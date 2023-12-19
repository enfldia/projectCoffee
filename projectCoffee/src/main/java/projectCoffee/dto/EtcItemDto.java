package projectCoffee.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class EtcItemDto {

        private Long id;

        private String itemNm;

        private String itemDetail;

        private String imgUrl;

        private Integer price;

        private String category;

        @QueryProjection
        public EtcItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price, String category) {
            this.id = id;
            this.itemNm = itemNm;
            this.itemDetail = itemDetail;
            this.imgUrl = imgUrl;
            this.price = price;
            this.category = category;
        }

    //@QueryProjection을 사용하면 Item 객체로 값을 받은후
        //Dto 클래스로 변환하는 과정없이 바로 Dto 객체를 뽑아낼수 있다.
    }




