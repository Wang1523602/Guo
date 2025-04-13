package com.swzl.controller;

import com.swzl.entity.Page;
import com.swzl.entity.Wupin;
import com.swzl.entity.Zhandian;
import com.swzl.service.WupinService;
import com.swzl.service.ZhandainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/found")
public class FoundController {
    @Autowired
    private WupinService wupinService;
    @Autowired
    private ZhandainService zhandainService;

    public String wupinmingcheng = "";

    //寻物列表
    @RequestMapping("/list")
    @ResponseBody//返回json数据到前端
    public String list(@RequestBody Page page) {
        //当前页数
        int pageCount = page.getCurrPage();
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        //总记录数
        Integer totalCount = wupinService.findXunWuTotal();
        //重试次数
        int count = 2;
        while (totalCount == 0 && count > 0) {
            totalCount = wupinService.findXunWuTotal();
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
            List<Wupin> wuPinEntitys = wupinService.findXunWuList(startIndex, pageSize);
            //重试次数
            int retry = 2;
            while (wuPinEntitys.size() == 0 && retry > 0) {
                wuPinEntitys = wupinService.findXunWuList(startIndex, pageSize);
                retry--;
            }
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"totalPage\":" + "\"" + totalPage + "\"" + ",");
                ggJson.append("\"totalCount\":" + "\"" + totalCount + "\"" + ",");
                ggJson.append("\"currPage\":" + "\"" + pageCount + "\"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String title = wuPinEntity.getWupinmingcheng();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    ggJson.append("{");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + title + "\"" + ",");
                    ggJson.append("\"zhuangtai\":" + "\"" + zhuangtai + "\"" + ",");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"addtime\":" + "\"" + date + "\"");
                    ggJson.append("}");
                    if (i < wuPinEntitys.size() - 1) {
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


    //寻物页面搜索信息处理
    @PostMapping("/search")
    public String search(Wupin Wupin, Model model, HttpSession session) {
        wupinmingcheng = Wupin.getWupinmingcheng();
        if (wupinmingcheng.equals("")) {
//            model.addAttribute("errorMsg", "请输入关键词!");
            Object obj = session.getAttribute("dbWupin");
            if (null != obj) {
                session.removeAttribute("dbWupin");
            }
            return "/found2.jsp";
        }
        wupinmingcheng = "%" + wupinmingcheng + "%";
        List<Wupin> dbWupin = wupinService.findXunWuByWuPinName(wupinmingcheng);
        if (dbWupin.size() > 0) {
            return "redirect:/found/resSearch";
        } else {
//            model.addAttribute("errorMsg", "该物品不存在!");
            Object obj = session.getAttribute("dbWupin");
            if (null != obj) {
                session.removeAttribute("dbWupin");
            }
            return "/found2.jsp";
        }
    }

    //返回搜索结果
    @GetMapping("/resSearch")
    public String resSearch(HttpSession session) {
        List<Wupin> dbWupin = wupinService.findXunWuByWuPinName(wupinmingcheng);
        session.setAttribute("dbWupin", dbWupin);
        return "/found2.jsp";
    }

    //寻物页面详情页
    @GetMapping("/content")
    public String content(String id, Map<String, Object> map) {
        Wupin dbWupin = wupinService.findXunWuById(id);
        String zhanDianName = dbWupin.getZhandianmingcheng();
        Zhandian dbZhanDian = null;
        if (!zhanDianName.equals("")) {
            dbZhanDian = zhandainService.findByZhanDianName(zhanDianName);
        }
        dbWupin.setLianxidianhua(dbZhanDian.getLianxidianhua());
        map.put("dbWupin", dbWupin);
        return "/foundContent.jsp";
    }


    //个人中心寻物审核中列表
    @RequestMapping("/userXWSH")
    @ResponseBody//返回json数据到前端
    public String userXunWu(@RequestBody Wupin wupin) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        String userid = wupin.getUserid();
        userid = userid.replace(" ", "");
        if ("".equals(userid) || userid.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");
        } else {
            List<Wupin> wuPinEntitys = wupinService.findXWSHByUserId(wupin.getUserid());
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String bianhao = wuPinEntity.getBianhao();
                    String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                    String tupian = wuPinEntity.getTupian();
                    String shiquren = wuPinEntity.getShiquren();
                    String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                    String shenhejieguo = wuPinEntity.getShenhejieguo();
                    Date shiquriqi = wuPinEntity.getShiquriqi();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    String date2 = formatter.format(shiquriqi);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                    ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                    ggJson.append("\"shiquren\":" + "\"" + shiquren + "\"" + ",");
                    ggJson.append("\"shiquriqi\":" + "\"" + date2 + "\"" + ",");
                    ggJson.append("\"zhuangtai\":" + "\"" + zhuangtai + "\"" + ",");
                    ggJson.append("\"shenhejieguo\":" + "\"" + shenhejieguo + "\"" + ",");
                    ggJson.append("\"zhandianmingcheng\":" + "\"" + zhandianmingcheng + "\"" + ",");
                    ggJson.append("\"addtime\":" + "\"" + date + "\"");
                    ggJson.append("}");
                    if (i < wuPinEntitys.size() - 1) {
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

    //个人中心寻物审核未通过列表
    @RequestMapping("/userXWJJ")
    @ResponseBody//返回json数据到前端
    public String userXWJJ(@RequestBody Wupin wupin) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        String userid = wupin.getUserid();
        userid = userid.replace(" ", "");
        if ("".equals(userid) || userid.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");
        } else {
            List<Wupin> wuPinEntitys = wupinService.findXWSHByUserIdJJ(wupin.getUserid());
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String bianhao = wuPinEntity.getBianhao();
                    String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                    String tupian = wuPinEntity.getTupian();
                    String shiquren = wuPinEntity.getShiquren();
                    String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                    String shenhejieguo = wuPinEntity.getShenhejieguo();
                    Date shiquriqi = wuPinEntity.getShiquriqi();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    String date2 = formatter.format(shiquriqi);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                    ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                    ggJson.append("\"shiquren\":" + "\"" + shiquren + "\"" + ",");
                    ggJson.append("\"shiquriqi\":" + "\"" + date2 + "\"" + ",");
                    ggJson.append("\"zhuangtai\":" + "\"" + zhuangtai + "\"" + ",");
                    ggJson.append("\"shenhejieguo\":" + "\"" + shenhejieguo + "\"" + ",");
                    ggJson.append("\"zhandianmingcheng\":" + "\"" + zhandianmingcheng + "\"" + ",");
                    ggJson.append("\"addtime\":" + "\"" + date + "\"");
                    ggJson.append("}");
                    if (i < wuPinEntitys.size() - 1) {
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


    //个人中心寻物审核详情页
    @GetMapping("/XWSHContent")
    public String XWSHContent(String id, Map<String, Object> map) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        Wupin dbWupin = wupinService.findXWSHById(id);
        map.put("dbWupin", dbWupin);
        return "/userXWContent.jsp";
    }


    //个人中心寻物审核删除
    @GetMapping("/XWSHDelete")
    public String XWSHDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.deleteXWSHById(id);
        return "redirect:/userXunWu.jsp";
    }

    //个人中心寻物公示中列表
    @RequestMapping("/userXWXZ")
    @ResponseBody//返回json数据到前端
    public String userXWXZ(@RequestBody Wupin wupin) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        String userid = wupin.getUserid();
        userid = userid.replace(" ", "");
        if ("".equals(userid) || userid.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");
        } else {
            List<Wupin> wuPinEntitys = wupinService.findXWByUserId(wupin.getUserid());
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String bianhao = wuPinEntity.getBianhao();
                    String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                    String tupian = wuPinEntity.getTupian();
                    String shiquren = wuPinEntity.getShiquren();
                    String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                    Date shiquriqi = wuPinEntity.getShiquriqi();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    String date2 = formatter.format(shiquriqi);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                    ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                    ggJson.append("\"shiquren\":" + "\"" + shiquren + "\"" + ",");
                    ggJson.append("\"shiquriqi\":" + "\"" + date2 + "\"" + ",");
                    ggJson.append("\"zhuangtai\":" + "\"" + zhuangtai + "\"" + ",");
                    ggJson.append("\"zhandianmingcheng\":" + "\"" + zhandianmingcheng + "\"" + ",");
                    ggJson.append("\"addtime\":" + "\"" + date + "\"");
                    ggJson.append("}");
                    if (i < wuPinEntitys.size() - 1) {
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

    //个人中心寻物已完成列表
    @RequestMapping("/userXWXZWC")
    @ResponseBody//返回json数据到前端
    public String userXWXZWC(@RequestBody Wupin wupin) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        String userid = wupin.getUserid();
        userid = userid.replace(" ", "");
        if ("".equals(userid) || userid.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");
        } else {
            List<Wupin> wuPinEntitys = wupinService.findXWByUserIdWC(wupin.getUserid());
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String bianhao = wuPinEntity.getBianhao();
                    String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                    String tupian = wuPinEntity.getTupian();
                    String shiquren = wuPinEntity.getShiquren();
                    String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                    Date shiquriqi = wuPinEntity.getShiquriqi();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    String date2 = formatter.format(shiquriqi);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                    ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                    ggJson.append("\"shiquren\":" + "\"" + shiquren + "\"" + ",");
                    ggJson.append("\"shiquriqi\":" + "\"" + date2 + "\"" + ",");
                    ggJson.append("\"zhuangtai\":" + "\"" + zhuangtai + "\"" + ",");
                    ggJson.append("\"zhandianmingcheng\":" + "\"" + zhandianmingcheng + "\"" + ",");
                    ggJson.append("\"addtime\":" + "\"" + date + "\"");
                    ggJson.append("}");
                    if (i < wuPinEntitys.size() - 1) {
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

    //个人中心寻物详情页
    @GetMapping("/XWContent")
    public String XWContent(String id, Map<String, Object> map) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        Wupin dbWupin = wupinService.findXunWuById(id);
        map.put("dbWupin", dbWupin);
        return "/userXWContent.jsp";
    }

    //个人中心寻物删除
    @GetMapping("/XWDelete")
    public String XWDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.deleteXWById(id);
        return "redirect:/userXunWu.jsp";
    }

    //管理员寻物审核列表
    @RequestMapping("/HTXWSH")
    @ResponseBody//返回json数据到前端
    public String HTXWSH() {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        List<Wupin> wuPinEntitys = wupinService.findAllXWSH();
        if (wuPinEntitys.size() > 0) {
            ggJson.append("\"status\":" + "\" true \"" + ",");
            ggJson.append("\"list\":");
            ggJson.append("[");
            for (int i = 0; i < wuPinEntitys.size(); i++) {
                Wupin wuPinEntity = wuPinEntitys.get(i);
                String bianhao = wuPinEntity.getBianhao();
                String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                String tupian = wuPinEntity.getTupian();
                String shiquren = wuPinEntity.getShiquren();
                String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                String shenhejieguo = wuPinEntity.getShenhejieguo();
                Date shiquriqi = wuPinEntity.getShiquriqi();
                Date addtime = wuPinEntity.getAddtime();
                Integer id = wuPinEntity.getId();
                String zhuangtai = wuPinEntity.getZhuangtai();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(addtime);
                String date2 = formatter.format(shiquriqi);
                ggJson.append("{");
                ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                ggJson.append("\"shiquren\":" + "\"" + shiquren + "\"" + ",");
                ggJson.append("\"shiquriqi\":" + "\"" + date2 + "\"" + ",");
                ggJson.append("\"zhuangtai\":" + "\"" + zhuangtai + "\"" + ",");
                ggJson.append("\"shenhejieguo\":" + "\"" + shenhejieguo + "\"" + ",");
                ggJson.append("\"zhandianmingcheng\":" + "\"" + zhandianmingcheng + "\"" + ",");
                ggJson.append("\"addtime\":" + "\"" + date + "\"");
                ggJson.append("}");
                if (i < wuPinEntitys.size() - 1) {
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

    //管理员查看寻物审核详情页
    @GetMapping("/HTXWSHContent")
    public String HTXWSHContent(String id, Map<String, Object> map) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        Wupin dbWupin = wupinService.findXWSHById(id);
        map.put("dbWupin", dbWupin);
        return "/houTaiXWContent.jsp";
    }

    //管理员寻物审核同意
    @GetMapping("/HTXWSHTYUpdate")
    public String HTXWSHTYUpdate(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.updateXWSH(id);
        Wupin dbWupin = wupinService.findXWSHById(id);
        wupinService.addXW(dbWupin);
        return "redirect:/houTaiIndex.jsp";
    }

    //管理员寻物审核拒绝
    @GetMapping("/HTXWSHJJUpdate")
    public String HTXWSHJJUpdate(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.updateJJXWSH(id);
        return "redirect:/houTaiIndex.jsp";
    }

    //管理员寻物公告公示中列表
    @RequestMapping("/HTXWXZ")
    @ResponseBody//返回json数据到前端
    public String HTXWXZ() {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        List<Wupin> wuPinEntitys = wupinService.findAllXunWu();
        if (wuPinEntitys.size() > 0) {
            ggJson.append("\"status\":" + "\" true \"" + ",");
            ggJson.append("\"list\":");
            ggJson.append("[");
            for (int i = 0; i < wuPinEntitys.size(); i++) {
                Wupin wuPinEntity = wuPinEntitys.get(i);
                String bianhao = wuPinEntity.getBianhao();
                String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                String tupian = wuPinEntity.getTupian();
                String shiquren = wuPinEntity.getShiquren();
                String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                Date shiquriqi = wuPinEntity.getShiquriqi();
                Date addtime = wuPinEntity.getAddtime();
                Integer id = wuPinEntity.getId();
                String zhuangtai = wuPinEntity.getZhuangtai();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(addtime);
                String date2 = formatter.format(shiquriqi);
                ggJson.append("{");
                ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                ggJson.append("\"shiquren\":" + "\"" + shiquren + "\"" + ",");
                ggJson.append("\"shiquriqi\":" + "\"" + date2 + "\"" + ",");
                ggJson.append("\"zhuangtai\":" + "\"" + zhuangtai + "\"" + ",");
                ggJson.append("\"zhandianmingcheng\":" + "\"" + zhandianmingcheng + "\"" + ",");
                ggJson.append("\"addtime\":" + "\"" + date + "\"");
                ggJson.append("}");
                if (i < wuPinEntitys.size() - 1) {
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

    //管理员查看寻物公告公示中的详情页
    @GetMapping("/HTXWXZContent")
    public String HTXWXZContent(String id, Map<String, Object> map) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        Wupin dbWupin = wupinService.findXunWuById(id);
        map.put("dbWupin", dbWupin);
        return "/houTaiXWContent.jsp";
    }

    //管理员提交完成寻物公示中的公告
    @GetMapping("/HTXWXZWCUpdate")
    public String HTXWXZWCUpdate(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.updateXWXZ(id);
        return "redirect:/houTaiIndex.jsp";
    }

    //管理员删除寻物公示公告
    @GetMapping("/HTXWXZDelete")
    public String HTXWXZDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.deleteXWById(id);
        return "redirect:/houTaiIndex.jsp";
    }

    //管理员寻物公告已完成列表
    @RequestMapping("/HTXWWC")
    @ResponseBody//返回json数据到前端
    public String HTXWWC() {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        List<Wupin> wuPinEntitys = wupinService.findAllXWWC();
        if (wuPinEntitys.size() > 0) {
            ggJson.append("\"status\":" + "\" true \"" + ",");
            ggJson.append("\"list\":");
            ggJson.append("[");
            for (int i = 0; i < wuPinEntitys.size(); i++) {
                Wupin wuPinEntity = wuPinEntitys.get(i);
                String bianhao = wuPinEntity.getBianhao();
                String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                String tupian = wuPinEntity.getTupian();
                String shiquren = wuPinEntity.getShiquren();
                String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                Date shiquriqi = wuPinEntity.getShiquriqi();
                Date addtime = wuPinEntity.getAddtime();
                Integer id = wuPinEntity.getId();
                String zhuangtai = wuPinEntity.getZhuangtai();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(addtime);
                String date2 = formatter.format(shiquriqi);
                ggJson.append("{");
                ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                ggJson.append("\"shiquren\":" + "\"" + shiquren + "\"" + ",");
                ggJson.append("\"shiquriqi\":" + "\"" + date2 + "\"" + ",");
                ggJson.append("\"zhuangtai\":" + "\"" + zhuangtai + "\"" + ",");
                ggJson.append("\"zhandianmingcheng\":" + "\"" + zhandianmingcheng + "\"" + ",");
                ggJson.append("\"addtime\":" + "\"" + date + "\"");
                ggJson.append("}");
                if (i < wuPinEntitys.size() - 1) {
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

}
