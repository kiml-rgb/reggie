package com.itheima.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.itheima.vo.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-16 11:38:44
 */
@RestController
@RequestMapping("common")
public class CommonController {

    @PostMapping("upload")
    public R uploadPic(MultipartFile file) throws IOException {
        // 校验
        // 校验文件是否为空
        if (ObjectUtil.isNull(file)) return R.error("上传文件不能为空");
        // 校验文件的格式
        if (!StrUtil.containsAnyIgnoreCase(FileUtil.getSuffix(file.getOriginalFilename()), "jpg", "png")) {
            return R.error("上传文件格式错误");
        }
        // 校验文件的大小
        if (file.getSize() / 1024 / 1024 > 2) return R.error("文件过大，限制在2MB以内");
        // 给文件进行重命名
        String fileName = UUID.fastUUID().toString() + "." + FileUtil.getSuffix(file.getOriginalFilename());
        // 给文件目录分离
        // 图片保存地点 web服务器（项目所运行在的电脑） 云存储服务商 自己搭建文件服务器（FastDIS Minio）

        file.transferTo(new File("D:\\Develop\\workspace\\reggie\\reggie_take_out\\src\\main\\resources\\static\\images\\" + fileName));
        return R.success(fileName);
    }

    @GetMapping("download")
    public void downloadPic(String name, HttpServletResponse response) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:\\Develop\\workspace\\reggie\\reggie_take_out\\src\\main\\resources\\static\\images\\" + name);
        ServletOutputStream outputStream = response.getOutputStream();

        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }

        fileInputStream.close();
        outputStream.close();
    }
}
