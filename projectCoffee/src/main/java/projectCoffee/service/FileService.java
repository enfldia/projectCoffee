package projectCoffee.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
    public String uploadFile(String uploadPath, String originalFileName,
                             byte[] fileData) throws Exception {

        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;

        System.out.println("88888888888888888" + savedFileName.toString());

        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        System.out.println("9999999999999999999999" + fileUploadFullUrl.toString() );

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);

        System.out.println("1212121212121212121" + fos.toString());

        fos.write(fileData); //fileDate 배열에 내용을 파일에 기록

        System.out.println("1112231234123412");

        fos.close(); //파일 출력 스트림을 닫습니다.111

        System.out.println("232323232323232323");
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);

        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
