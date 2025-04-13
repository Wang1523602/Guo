package com.swzl.controller;


import com.swzl.entity.Page;
import com.swzl.entity.Zhandian;
import com.swzl.service.WupinService;
import com.swzl.service.ZhandainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/zhandian")
public class ZhanDianController {
    @Autowired
    private ZhandainService zhandainService;
    @Autowired
    private WupinService wupinService;
    public String zhanDianName = "";

    //站点列表
    @RequestMapping("/list")
    @ResponseBody//返回json数据到前端
    public String list(@RequestBody Page page) {
        //当前页数
        int pageCount = page.getCurrPage();
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        //总记录数
        Integer totalCount = zhandainService.findZhanDianTotal();
        //重试次数
        int count = 2;
        while (totalCount == 0 && count > 0) {
            totalCount = zhandainService.findZhanDianTotal();
            count--;
        }
        //总页数
        Integer totalPage = totalCount % 10 == 0 ? totalCount / 10 : totalCount / 10 + 1;
        //如果记录数为0或请求页面大于总页数或为负数
        if (pageCount > totalPage || pageCount < 0 || totalCount == 0) {
            ggJson.append("\"status\":" + "\" false \"" + ",");
        } else {//如果数据正常则写成json格式返回前端
            //每页显示数,10条
            Integer pageSize = page.getPageSize();
            // 计算前索引
            Integer startIndex = (pageCount - 1) * pageSize;
            List<Zhandian> zhanDianEntitys = zhandainService.findZhanDianList(startIndex, pageSize);
            //重试次数
            int retry = 2;
            while (zhanDianEntitys.size() == 0 && retry > 0) {
                zhanDianEntitys = zhandainService.findZhanDianList(startIndex, pageSize);
                retry--;
            }
            if (zhanDianEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"totalPage\":" + "\"" + totalPage + "\"" + ",");
                ggJson.append("\"totalCount\":" + "\"" + totalCount + "\"" + ",");
                ggJson.append("\"currPage\":" + "\"" + pageCount + "\"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < zhanDianEntitys.size(); i++) {
                    Zhandian zhanDianPinEntity = zhanDianEntitys.get(i);
                    String zhandianmingcheng = zhanDianPinEntity.getZhandianmingcheng();
                    String dizhi = zhanDianPinEntity.getDizhi();
                    Date addtime = zhanDianPinEntity.getAddtime();
                    Integer id = zhanDianPinEntity.getId();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    ggJson.append("{");
                    ggJson.append("\"zhandianmingcheng\":" + "\"" + zhandianmingcheng + "\"" + ",");
                    ggJson.append("\"dizhi\":" + "\"" + dizhi + "\"" + ",");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"addtime\":" + "\"" + date + "\"");
                    ggJson.append("}");
                    if (i < zhanDianEntitys.size() - 1) {
                        ggJson.append(",");
                    }
                }
                ggJson.append("]");
            } else {
                ggJson.append("\"status\":" + "\" false \"" + ",");
            }
        }
        ggJson.append("}");
        return ggJson.toString();
    }

    //详情页
    @GetMapping("/content")
    public String content(String id, Map<String, Object> map) {
        if (StringUtils.isEmpty(id)) {
            return "false";
        }
        Zhandian dbZhanDian = zhandainService.findZhanDianById(id);
        map.put("dbZhanDian", dbZhanDian);
        return "/zhandianContent.jsp";
    }

    //站点页面搜索信息处理
    @PostMapping("/search")
    public String search(Zhandian zhandian, Model model, HttpSession session) {
        zhanDianName = zhandian.getZhandianmingcheng();
        if (zhanDianName.equals("")) {
//            model.addAttribute("errorMsg", "请输入关键词!");
            Object obj = session.getAttribute("dbZhanDian");
            if (null != obj) {
                session.removeAttribute("dbZhanDian");
            }
            return "/zhandian2.jsp";
        }
        zhanDianName = "%" + zhanDianName + "%";
        List<Zhandian> dbZhanDian = zhandainService.findZDByName(zhanDianName);
        if (dbZhanDian.size() > 0) {
            return "redirect:/zhandian/resSearch";
        } else {
//            model.addAttribute("errorMsg", "该物品不存在!");
            Object obj = session.getAttribute("dbZhanDian");
            if (null != obj) {
                session.removeAttribute("dbZhanDian");
            }
            return "/zhandian2.jsp";
        }
    }

    //返回搜索结果
    @GetMapping("/resSearch")
    public String resSearch(HttpSession session) {
        List<Zhandian> dbZhanDian = zhandainService.findZDByName(zhanDianName);
        session.setAttribute("dbZhanDian", dbZhanDian);
        return "/zhandian2.jsp";
    }

    //上传站点信息
    @RequestMapping("/information")
    @ResponseBody
    public String liuYanAdd(@RequestBody Zhandian zhandian) {
        String bianhao = zhandian.getBianhao();
        String zhandianmingcheng = zhandian.getZhandianmingcheng();
        String jianjie = zhandian.getJianjie();
        String fuzeren = zhandian.getFuzeren();
        String lianxidianhua = zhandian.getLianxidianhua();
        String youxiang = zhandian.getYouxiang();
        String dizhi = zhandian.getDizhi();
        String fuwushijian = zhandian.getFuwushijian();
        if (StringUtils.isEmpty(bianhao) && StringUtils.isEmpty(zhandianmingcheng)
                && StringUtils.isEmpty(jianjie) && StringUtils.isEmpty(fuzeren)
                && StringUtils.isEmpty(lianxidianhua) && StringUtils.isEmpty(youxiang)
                && StringUtils.isEmpty(dizhi) && StringUtils.isEmpty(fuwushijian)
                ) {
            return "false";
        } else {
            Zhandian dbZhanDian = zhandainService.findZhanDianByBh(bianhao);
            if (null != dbZhanDian) {
                return "0";
            }
        }
        Date addTime = new Date();
        zhandian.setAddtime(addTime);
        zhandainService.addZD(zhandian);
        return "succeful";
    }

    //后台全部站点列表
    @RequestMapping("/HTZDList")
    @ResponseBody//返回json数据到前端
    public String HTZDList() {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        List<Zhandian> zhanDianEntitys = zhandainService.findAllZhanDian();
        if (zhanDianEntitys.size() > 0) {
            ggJson.append("\"status\":" + "\" true \"" + ",");
            ggJson.append("\"list\":");
            ggJson.append("[");
            for (int i = 0; i < zhanDianEntitys.size(); i++) {
                Zhandian zhanDianPinEntity = zhanDianEntitys.get(i);
                String zhandianmingcheng = zhanDianPinEntity.getZhandianmingcheng();
                String dizhi = zhanDianPinEntity.getDizhi();
                String bianhao = zhanDianPinEntity.getBianhao();
                String fuzeren = zhanDianPinEntity.getFuzeren();
                String lianxidianhua = zhanDianPinEntity.getLianxidianhua();
                String youxiang = zhanDianPinEntity.getYouxiang();
                String fuwushijian = zhanDianPinEntity.getFuwushijian();
                Date addtime = zhanDianPinEntity.getAddtime();
                Integer id = zhanDianPinEntity.getId();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(addtime);
                ggJson.append("{");
                ggJson.append("\"zhandianmingcheng\":" + "\"" + zhandianmingcheng + "\"" + ",");
                ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                ggJson.append("\"fuzeren\":" + "\"" + fuzeren + "\"" + ",");
                ggJson.append("\"lianxidianhua\":" + "\"" + lianxidianhua + "\"" + ",");
                ggJson.append("\"youxiang\":" + "\"" + youxiang + "\"" + ",");
                ggJson.append("\"fuwushijian\":" + "\"" + fuwushijian + "\"" + ",");
                ggJson.append("\"dizhi\":" + "\"" + dizhi + "\"" + ",");
                ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                ggJson.append("\"addtime\":" + "\"" + date + "\"");
                ggJson.append("}");
                if (i < zhanDianEntitys.size() - 1) {
                    ggJson.append(",");
                }
            }
            ggJson.append("]");
        } else {
            ggJson.append("\"status\":" + "\" false \"" + ",");
        }
        ggJson.append("}");
        return ggJson.toString();
    }

    //后台详情页
    @GetMapping("/HTZDContent")
    public String HTZDContent(String id, Map<String, Object> map) {
        if (StringUtils.isEmpty(id)) {
            return "false";
        }
        Zhandian dbZhanDian = zhandainService.findZhanDianById(id);
        String bianhao = dbZhanDian.getBianhao();
        Integer succefulTotal = wupinService.findZhaoLingZDWCTotal(bianhao);
        Integer wLQTotal = wupinService.findZhaoLingZDCFTotal(bianhao);
        map.put("wLQTotal", wLQTotal);
        map.put("succefulTotal", succefulTotal);
        map.put("dbZhanDian", dbZhanDian);
        return "/houTaiZDContent.jsp";
    }

    //后台删除
    @GetMapping("/HTZDDelete")
    public String HTZDDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        zhandainService.deleteZLDById(id);
        return "redirect:/houTaiZhanDian.jsp";
    }

}
