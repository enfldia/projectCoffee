package projectCoffee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import projectCoffee.dto.*;
import projectCoffee.entity.Item;
import projectCoffee.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile>
                          itemImgFileList, Optional<Integer> page,ItemSearchDto itemSearchDto){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
             model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
             return "item/itemForm";
        }

        try{
            itemService.saveItem(itemFormDto,itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
                    return "item/itemForm";
        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);//
        // PageRequest - Data jpa에서 사용하는 페이지 요청객체
        // of() 메서드를 사용하여 페이지 번화와 페이지당 항목수 지정하여 페이지 요청 정보생성
        // 0 은 url 경로에서 받아온 페이지 번호를 확인하고 , 값이 없으면 0 = 보고 있던 페이지가 없으면 첫번째 페이지를 보여줌
        // 3 은 한 페이지 당 보여줄 항목수


        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        //itemSearchDto를 사용하여 페이지 네이션 된 테이터를 조회
        model.addAttribute("items", items); //조회된 페이지 네이션된 데이터를 모델에 추가
        model.addAttribute("itemSearchDto", itemSearchDto); //검색 조건 모델에 추가
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }

    //상품 수정하기
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){
        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (Exception e){
            model.addAttribute("errorMessage","존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping("/admin/item/delete/{itemId}")
    public String itemDelete(@PathVariable("itemId") Long itemId,Optional<Integer> page, ItemSearchDto itemSearchDto, Model model){
        itemService.deleteItem(itemId);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);//
        // PageRequest - Data jpa에서 사용하는 페이지 요청객체
        // of() 메서드를 사용하여 페이지 번화와 페이지당 항목수 지정하여 페이지 요청 정보생성
        // 0 은 url 경로에서 받아온 페이지 번호를 확인하고 , 값이 없으면 0 = 보고 있던 페이지가 없으면 첫번째 페이지를 보여줌
        // 3 은 한 페이지 당 보여줄 항목수


        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        //itemSearchDto를 사용하여 페이지 네이션 된 테이터를 조회
        model.addAttribute("items", items); //조회된 페이지 네이션된 데이터를 모델에 추가
        model.addAttribute("itemSearchDto", itemSearchDto); //검색 조건 모델에 추가
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }



    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             Optional<Integer> page,ItemSearchDto itemSearchDto, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try{
            System.out.println("1111111111111111111111111111111111111111111 itemImgFileList"+itemImgFileList.toString());
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);//
        // PageRequest - Data jpa에서 사용하는 페이지 요청객체
        // of() 메서드를 사용하여 페이지 번화와 페이지당 항목수 지정하여 페이지 요청 정보생성
        // 0 은 url 경로에서 받아온 페이지 번호를 확인하고 , 값이 없으면 0 = 보고 있던 페이지가 없으면 첫번째 페이지를 보여줌
        // 3 은 한 페이지 당 보여줄 항목수


        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        //itemSearchDto를 사용하여 페이지 네이션 된 테이터를 조회
        model.addAttribute("items", items); //조회된 페이지 네이션된 데이터를 모델에 추가
        model.addAttribute("itemSearchDto", itemSearchDto); //검색 조건 모델에 추가
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }

    @GetMapping(value = {"/admin/items","admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);//
        // PageRequest - Data jpa에서 사용하는 페이지 요청객체
        // of() 메서드를 사용하여 페이지 번화와 페이지당 항목수 지정하여 페이지 요청 정보생성
        // 0 은 url 경로에서 받아온 페이지 번호를 확인하고 , 값이 없으면 0 = 보고 있던 페이지가 없으면 첫번째 페이지를 보여줌
        // 3 은 한 페이지 당 보여줄 항목수


        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        //itemSearchDto를 사용하여 페이지 네이션 된 테이터를 조회
        model.addAttribute("items", items); //조회된 페이지 네이션된 데이터를 모델에 추가
        model.addAttribute("itemSearchDto", itemSearchDto); //검색 조건 모델에 추가
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }

    @GetMapping("/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId")Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        System.out.println("1234123412341234123412341" +itemFormDto.getItemImgDtoList().get(0).getImgUrl());
        model.addAttribute("item",itemFormDto);
        return "item/ItemDtl";
    }

    @GetMapping("/shopItem")
    public String shopItemShow(ItemSearchDto itemSearchDto, Optional<Integer> page,
                       Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,9);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
//        Page<CoffeeItemDto> coffeeItems = itemService.getCoffeeItemPage(itemSearchDto, pageable);
        Page<ToolsItemDto> toolsItems = itemService.getToolsItemPage(itemSearchDto, pageable);
        Page<EtcItemDto> etcItems = itemService.getEtcItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
//        model.addAttribute("coffeeItems", coffeeItems);
        model.addAttribute("toolsItems", toolsItems);
        model.addAttribute("etcItems", etcItems);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage",5);


        return "/item/shopItem";
    }

    @GetMapping("/show/coffee")
    public String showCoffee(ItemSearchDto itemSearchDto, Optional<Integer> page,
                               Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() :0,12);
        Page<CoffeeItemDto> coffeeItems = itemService.getCoffeeItemPage(itemSearchDto, pageable);

        model.addAttribute("coffeeItems", coffeeItems);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage",5);

        return "/item/showCoffee";
    }

    @GetMapping("/show/tools")
    public String showTools(ItemSearchDto itemSearchDto, Optional<Integer> page,
                             Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() :0,12);
        Page<ToolsItemDto> toolsItems = itemService.getToolsItemPage(itemSearchDto, pageable);

        model.addAttribute("toolsItems", toolsItems);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage",5);

        return "/item/showTools";
    }

    @GetMapping("/show/etc")
    public String showetc(ItemSearchDto itemSearchDto, Optional<Integer> page,
                             Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() :0,12);
        Page<EtcItemDto> etcItems = itemService.getEtcItemPage(itemSearchDto, pageable);

        model.addAttribute("etcItems", etcItems);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage",5);

        return "/item/showEtc";
    }
}
