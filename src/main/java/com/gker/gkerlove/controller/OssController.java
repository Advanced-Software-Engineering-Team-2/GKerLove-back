package com.gker.gkerlove.controller;



import com.gker.gkerlove.util.OssClientUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;





@RestController
@RequestMapping("/avatar")
public class OssController {


    @PostMapping("/upload")
    public String fileupload(@RequestParam MultipartFile file) throws IOException {
        if (file == null || file.getSize() <= 0) {
            throw new IOException("file不能为空");
        }
        OssClientUtil ossClient = new OssClientUtil();
        //将文件上传
        String name = ossClient.uploadImg2Oss(file);
        //获取文件的URl地址  以便前台  显示
        String imgUrl = ossClient.getImgUrl(name);
        return imgUrl;

    }

}
