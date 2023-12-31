package projectCoffee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import projectCoffee.dto.*;
import projectCoffee.entity.Item;
import projectCoffee.entity.ItemImg;
import projectCoffee.repository.ItemImgRepository;
import projectCoffee.repository.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static projectCoffee.entity.QItemImg.itemImg;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
            throws Exception{
        //상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0)
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("n");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        //해당 상품 이미지를 조회 해서 등록순으로 가지고 오기 위해서 상품 이미지 아이디 오름차순으로 가지고옴
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            //이미 엔티티 리스트를 dto로 변환
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        //해당 id의 상품정보를 데이터 베이스에서 가져옵니다. 없으면 예외 처리

        ItemFormDto itemFormDto = ItemFormDto.of(item);

        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto,
                           List<MultipartFile> itemImgFileList) throws Exception {
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        //1. 상품등록 화면으로부터 전달받은 상품 아이디를 이용하여 상품엔티티 조회

        item.updateItem(itemFormDto);
        //2. 상품등록 화면으로부터 전달받은 itemFormDto 통해 상품 엔티티 업데이트
        List<Long> itemImgIds = itemFormDto.getItemImgIds();


        if(itemImgIds.size() != itemImgFileList.size()){
            List<Long> newItemImgIds = new ArrayList<>();
            //이미지 등록
            for(int i= itemImgIds.size();i<itemImgFileList.size();i++){
                ItemImg itemImg = new ItemImg();
                itemImg.setItem(item);
                itemImg.setRepImgYn("n");
                itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
                newItemImgIds.add(itemImg.getId());
            }
            itemImgIds.addAll(newItemImgIds);
            //addAll로 앞에 리스트 안에 () 가로 속 리스트를 병합시킴.
        }

        //itemFormDto에서 항목 이미지 Id 목록을 가져옵니다.
        //(상품이미지 아이디 리스트를 조회)
        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++) {
                itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        //itemImgFileList를 반복하면서 각 이미지에 대해
        //itemService의 updateItemImg 메서드를 호출합니다.
        //get(i) = List나 배열에서 i에 해당하는 요소를 가져오는 메소드
        //get(0) 첫번째 요소
        //상품이미지 업데이트를 통해 updateItemImg 메소드
        //상품이미지 아이디, 상품이미지 파일정보를 파라메타로 전달
        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CoffeeItemDto> getCoffeeItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getCoffeeItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ToolsItemDto> getToolsItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getToolsItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<EtcItemDto> getEtcItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getEtcItemPage(itemSearchDto, pageable);
    }

    public void deleteItem(Long itemId) {

       Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
       itemRepository.delete(item);


    }
}
