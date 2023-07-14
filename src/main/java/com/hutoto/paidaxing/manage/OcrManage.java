package com.hutoto.paidaxing.manage;


import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class OcrManage {
    // 执行OCR识别
//    private void execute(BufferedImage targetImage) throws TesseractException {
//        try {
//            File tempFile = new File(tempImage);
//            if (tempFile == null) {
//                tempFile.mkdirs();
//            }
//            tempFile.mkdirs();
//            ImageIO.write(targetImage, "jpg", tempFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        File file = new File(tempImage);
//
//        ITesseract instance = new Tesseract();
//        // 设置语言库位置
//        instance.setDatapath("src/main/resources/data");
//        // 设置语言
//        String language = "chi_sim";
//        instance.setLanguage(language);
//        String result = instance.doOCR(file);
////        ProgressBar.show(this, thread, "图片正在识别中，请稍后...", "执行结束", "取消");
//    }

    public static void main(String[] args) throws IOException {
        // 创建实例
        ITesseract instance = new Tesseract();
        instance.setDatapath("src/main/resources/data");
        // 设置识别语言
        instance.setLanguage("chi_sim");
        // 设置识别引擎
//        instance.setOcrEngineMode(1);
//        instance.setPageSegMode(6);

        // 读取文件

//        BufferedImage image = ImageIO.read(new File("C:\\Users\\罗燕师\\Desktop\\01f8d9c3321276e358d115c099af8a0.png"));
//        BufferedImage image = ImageIO.read(new File("C:\\Users\\罗燕师\\Desktop\\9e299366c8938fe207193a65e786b9c.jpg"));
        BufferedImage image = ImageIO.read(new File("C:\\Users\\罗燕师\\Desktop\\14f862ee900a450da0c3d38cbd2c3f55.jpeg"));
        try {
            // 识别
            //String res = instance.doOCR(new File("C:\\Users\\Lenovo\\Pictures\\联想截图\\联想截图_20230220144409.png"));
            String result = instance.doOCR(image);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

}
