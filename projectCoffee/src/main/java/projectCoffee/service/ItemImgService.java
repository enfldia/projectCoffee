package projectCoffee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import projectCoffee.entity.ItemImg;
import projectCoffee.repository.ItemImgRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
        String imgName = "";
        String imgUrl = "";

            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
                    itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;

        //상품 이미지 정보 저장
            itemImg.updateItemImg(oriImgName, imgName, imgUrl);
            itemImgRepository.save(itemImg);
    }
    } //6장  47페이지

    public void updateItemImg(Long itemImgId,
                              MultipartFile itemImgFile) throws Exception {
        //상품 이미지 아이디를 이용해서 기존에 저장했던 상품 이미지 엔티티를 조회합니다.
        if(!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" +
                        savedItemImg.getImgName());
            } //기존에 등록된 상품이미지 파일이 있을 경우 해당 파일 삭제.
            String oriImgName = itemImgFile.getOriginalFilename();
            //새로운 이미지 파일의 원본 파일 이름을 가져온다.
            String imgName = fileService.uploadFile(itemImgLocation,
                    oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
            //이미지 정보 엔티티의 필드를 업데이트

        }
    }
}

