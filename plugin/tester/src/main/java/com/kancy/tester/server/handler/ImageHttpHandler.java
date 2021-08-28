package com.kancy.tester.server.handler;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.utils.ImageUtils;
import com.kancy.tester.ui.BankCardPanel;
import com.kancy.tester.ui.IdCardPanel;
import com.sun.net.httpserver.HttpExchange;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * IdCardFrontImageHttpHandler
 *
 * @author huangchengkang
 * @date 2021/8/28 23:09
 */
@Component
public class ImageHttpHandler extends BaseHttpHandler{

    @Autowired
    private IdCardPanel idCardPanel;
    @Autowired
    private BankCardPanel bankCardPanel;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> stringStringMap = queryParamsToMap(httpExchange);

        byte[] respContents = null;
        try {
            idCardPanel.genButtonActionPerformed();
            if (Objects.equals(stringStringMap.getOrDefault("type", "idcard"), "bankcard")){
                bankCardPanel.genBanckCardButtonActionPerformed();
                respContents = ImageUtils.getComponentImage(bankCardPanel.getBankCardImagePanel());
            } else {
                respContents = mergeIdCardImages();
            }
            // 设置响应头
            httpExchange.getResponseHeaders().add("Content-Type", "image/png; charset=" + getResponseCharset());
        } catch (IOException e) {
            respContents = "图片生成错误".getBytes(getResponseCharset());
            httpExchange.getResponseHeaders().add("Content-Type", getContext());
        }
        // 设置响应code和内容长度
        httpExchange.sendResponseHeaders(200, respContents.length);
        // 设置响应内容
        httpExchange.getResponseBody().write(respContents);
        // 关闭处理器, 同时将关闭请求和响应的输入输出流（如果还没关闭）
        httpExchange.close();
    }


    public byte[] mergeIdCardImages() throws IOException {
        BufferedImage bi_0 = ImageIO.read(new ByteArrayInputStream(
                ImageUtils.getComponentImage(idCardPanel.getIdCardFrontImagePanel())));
        BufferedImage bi_1 = ImageIO.read(new ByteArrayInputStream(
                ImageUtils.getComponentImage(idCardPanel.getIdCardBackImagePanel())));

        //假设图片0 和图片1 宽度相同，上下合成
        //new 一个新的图像
        int w = bi_0.getWidth();
        int h_0 = bi_0.getHeight();
        int h_1 = bi_1.getHeight();
        int h = h_0 + h_1;
        BufferedImage bi=new BufferedImage(w,h,BufferedImage.TYPE_4BYTE_ABGR);
        //像素一个一个复制过来
        for(int y=0; y<h_0; y++){
            for(int x=0;x<w;x++){
                bi.setRGB(x,y,bi_0.getRGB(x,y));
            }
        }
        for(int y=0;y<h_1;y++){
            for(int x=0;x<w;x++){
                bi.setRGB(x,h_0+y,bi_1.getRGB(x,y));
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bi, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String getContextPath() {
        return "/image";
    }
}
