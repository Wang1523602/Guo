package com.swzl.controller;

import com.swzl.entity.Liuyan;
import com.swzl.entity.Page;
import com.swzl.entity.User;
import com.swzl.entity.Wupin;
import com.swzl.service.LiuyanService;
import com.swzl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/liuyan")
public class LiuyanController {

    @Autowired
    private LiuyanService liuyanService;

    //上传留言
    @RequestMapping("/information")
    @ResponseBody
    public String liuYanAdd(@RequestBody Liuyan liuyan) {
        String liuYanContent = liuyan.getLiuyanneirong().replace(" ", "");
        if (liuYanContent == null || liuYanContent.length() == 0) {
            return "false";
        }
        Date addTime = new Date();
        liuyan.setLiuyanriqi(addTime);
        liuyan.setAddtime(addTime);
        liuyanService.add(liuyan);
        return "succeful";
    }


    //获取留言列表
    @RequestMapping("/list")
    @ResponseBody
    public String liuYanList(@RequestBody Page page) {
        //当前页数
        int pageCount = page.getCurrPage();
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        //总记录数
        Integer totalCount = liuyanService.findLiuYanTotal();
        //重试次数
        int count = 2;
        while (totalCount == 0 && count > 0) {
            totalCount = liuyanService.findLiuYanTotal();
            count--;
        }
        //总页数
        Integer totalPage = totalCount % 10 == 0 ? totalCount / 10 : totalCount / 10 + 1;
        //如果记录数为0或请求页面大于总页数或为负数
        if (pageCount > totalPage || pageCount < 0 || totalCount == 0) {
            ggJson.append("\"status\":" + "\" false \"");
        } else {//如果数据正常则写成json格式返回前端
            //每页显示数,10条
            Integer pageSize = page.getPageSize();
            // 计算前索引
            Integer startIndex = (pageCount - 1) * pageSize;
            List<Liuyan> liuYanEntitys = liuyanService.findLiuYanList(startIndex, pageSize);
            //重试次数
            int retry = 2;
            while (liuYanEntitys.size() == 0 && retry > 0) {
                liuYanEntitys = liuyanService.findLiuYanList(startIndex, pageSize);
                retry--;
            }
            if (liuYanEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"totalPage\":" + "\"" + totalPage + "\"" + ",");
                ggJson.append("\"totalCount\":" + "\"" + totalCount + "\"" + ",");
                ggJson.append("\"currPage\":" + "\"" + pageCount + "\"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < liuYanEntitys.size(); i++) {
                    Liuyan liuYanEntity = liuYanEntitys.get(i);
                    String liuyanren = liuYanEntity.getLiuyanren();
                    String liuyanneirong = liuYanEntity.getLiuyanneirong();
                    Date addtime = liuYanEntity.getAddtime();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = formatter.format(addtime);
                    ggJson.append("{");
                    ggJson.append("\"liuyanren\":" + "\"" + liuyanren + "\"" + ",");
                    ggJson.append("\"liuyanriqi\":" + "\"" + date + "\"" + ",");
                    ggJson.append("\"liuyanneirong\":" + "\"" + liuyanneirong + "\"");
                    ggJson.append("}");
                    if (i < liuYanEntitys.size() - 1) {
                        ggJson.append(",");
                    }
                }
                ggJson.append("]");
            } else {
                ggJson.append("\"status\":" + "\" false \"");
            }
        }
        ggJson.append("}");
        return ggJson.toString();
    }


    //根据用户id获取留言列表
    @RequestMapping("/userLY")
    @ResponseBody
    public String userLY(@RequestBody Liuyan liuyan) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        Integer user_Id = liuyan.getUserid();
        String userId = String.valueOf(user_Id);
        userId = userId.replace(" ", "");
        if ("".equals(userId) || userId.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");
        } else {//如果数据正常则写成json格式返回前端
            List<Liuyan> liuYanEntitys = liuyanService.findLYByUserId(userId);
            if (liuYanEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < liuYanEntitys.size(); i++) {
                    Liuyan liuYanEntity = liuYanEntitys.get(i);
                    Integer id = liuYanEntity.getId();
                    String liuyanren = liuYanEntity.getLiuyanren();
                    String liuyanneirong = liuYanEntity.getLiuyanneirong();
                    Date addtime = liuYanEntity.getAddtime();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = formatter.format(addtime);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"liuyanren\":" + "\"" + liuyanren + "\"" + ",");
                    ggJson.append("\"liuyanriqi\":" + "\"" + date + "\"" + ",");
                    ggJson.append("\"liuyanneirong\":" + "\"" + liuyanneirong + "\"");
                    ggJson.append("}");
                    if (i < liuYanEntitys.size() - 1) {
                        ggJson.append(",");
                    }
                }
                ggJson.append("]");
            } else {
                ggJson.append("\"status\":" + "\" false \"");
            }
        }
        ggJson.append("}");
        return ggJson.toString();
    }

    //个人根据id删除留言
    @GetMapping("/LYDelete")
    public String LYDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        liuyanService.deleteLYById(id);
        return "redirect:/userLiuYan.jsp";
    }

    //后台获取留言列表
    @RequestMapping("/HTLY")
    @ResponseBody
    public String HTLY() {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        List<Liuyan> liuYanEntitys = liuyanService.findAllLY();
        if (liuYanEntitys.size() > 0) {
            ggJson.append("\"status\":" + "\" true \"" + ",");
            ggJson.append("\"list\":");
            ggJson.append("[");
            for (int i = 0; i < liuYanEntitys.size(); i++) {
                Liuyan liuYanEntity = liuYanEntitys.get(i);
                Integer id = liuYanEntity.getId();
                String liuyanren = liuYanEntity.getLiuyanren();
                String liuyanneirong = liuYanEntity.getLiuyanneirong();
                Date addtime = liuYanEntity.getAddtime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = formatter.format(addtime);
                ggJson.append("{");
                ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                ggJson.append("\"liuyanren\":" + "\"" + liuyanren + "\"" + ",");
                ggJson.append("\"liuyanriqi\":" + "\"" + date + "\"" + ",");
                ggJson.append("\"liuyanneirong\":" + "\"" + liuyanneirong + "\"");
                ggJson.append("}");
                if (i < liuYanEntitys.size() - 1) {
                    ggJson.append(",");
                }
            }
            ggJson.append("]");
        } else {
            ggJson.append("\"status\":" + "\" false \"");
        }
        ggJson.append("}");
        return ggJson.toString();
    }

    //后台根据id删除留言
    @GetMapping("/HTLYDelete")
    public String HTLYDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        liuyanService.deleteLYById(id);
        return "redirect:/houTaiLiuYan.jsp";
    }
}
