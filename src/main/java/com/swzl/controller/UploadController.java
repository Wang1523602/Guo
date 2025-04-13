package com.swzl.controller;

import com.swzl.entity.Liuyan;
import com.swzl.entity.Wupin;
import com.swzl.entity.Zhandian;
import com.swzl.service.LiuyanService;
import com.swzl.service.WupinService;
import com.swzl.service.ZhandainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private ZhandainService zhandainService;
    @Autowired
    private WupinService wupinService;


    //上传物品信息
    @RequestMapping("/information")
    @ResponseBody
    public String toImgKey(@RequestBody Wupin wupin) {
        if (wupin.getWupinmingcheng().equals("") || wupin.getTupian().equals("")
                || wupin.getJianshu().equals("") || wupin.getLianxidianhua().equals("")
                || wupin.getUserid().equals("")) {
            return "有空信息，上传失败！";
        }
        String base64Str = wupin.getTupian();
        base64Str = base64Str.substring(base64Str.indexOf(",") + 1);
        UUID uid = UUID.randomUUID();
        String pictureId = uid.toString().replace("-", "");
        String imgPath = "D:\\project\\xyswzl\\src\\main\\webapp\\upload\\" + pictureId + ".jpg";
        //file:/D:/project/xyswzl/target/xyswzl-1.0-SNAPSHOT/WEB-INF/classes/com/swzl/controller/
//        //获取当前class文件路径
//        String path = UploadController.class.getResource("").toString();
        String zhanDianName = wupin.getZhandianmingcheng().trim();
        Zhandian dbZhanDian = null;
        if (!zhanDianName.equals("")) {
            dbZhanDian = zhandainService.findByZhanDianName(zhanDianName);
        }
        if (dbZhanDian == null) {
            return "获取站点信息失败";
        }
        //保存图片成功再入库
        boolean flag = false;
        if (!base64Str.equals("")) {
            flag = generateImage(base64Str, imgPath);
        }
        if (!flag) {
            return "图片解析失败";
        }
        String tupian = imgPath.substring(imgPath.indexOf("\\upload"));
        tupian = tupian.replace("\\", "/");
        String bianhao = dbZhanDian.getBianhao();
        String fuzeren = dbZhanDian.getFuzeren();
        String dizhi = dbZhanDian.getDizhi();
        Date addtime = new Date();
        wupin.setBianhao(bianhao);
        wupin.setFuzeren(fuzeren);
        wupin.setDizhi(dizhi);
        wupin.setAddtime(addtime);
        wupin.setTupian(tupian);
        wupin.setShenhejieguo("审核中");
        if (wupin.getZhuangtai().equals("未领取")) {
            if (wupin.getJiandaodizhi().equals("")) {
                return "有空信息，上传失败！";
            }
            wupinService.addZLSH(wupin);
        } else {
            if (wupin.getShiqudizhi().equals("") || wupin.getShiquren().equals("")) {
                return "有空信息，上传失败！";
            }
            wupinService.addXWSH(wupin);
        }

//        List<Wupin> dbzlsh = wupinService.findZLSH();
        return "succeful";
    }


    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
// 解密
            byte[] b = decoder.decodeBuffer(imgStr);
// 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {

            System.out.println(e);
            return false;
        }
    }

    //图片转化成base64字符串
    public static String GetImageStr() {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = "F:\\tupian\\a.jpg";//待处理的图片
        // 地址也有写成"F:/deskBG/86619-107.jpg"形式的
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }

}
