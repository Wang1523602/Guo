package com.swzl.controller;

import com.swzl.entity.Page;
import com.swzl.entity.User;
import com.swzl.entity.Wupin;
import com.swzl.entity.Zhandian;
import com.swzl.service.UserService;
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
@RequestMapping("/lost")
public class LostController {
    @Autowired
    private WupinService wupinService;

    @Autowired
    private ZhandainService zhandainService;

    @Autowired
    private UserService userService;

    public String wupinmingcheng = "";

    //招领列表
    @RequestMapping("/list")
    @ResponseBody//返回json数据到前端
    public String list(@RequestBody Page page) {
        //当前页数
        int pageCount = page.getCurrPage();
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        //总记录数
        Integer totalCount = wupinService.findZhaoLingTotal();
        //重试次数
        int count = 2;
        while (totalCount == 0 && count > 0) {
            totalCount = wupinService.findZhaoLingTotal();
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
            List<Wupin> wuPinEntitys = wupinService.findZhaoLingList(startIndex, pageSize);
            //重试次数
            int retry = 2;
            while (wuPinEntitys.size() == 0 && retry > 0) {
                wuPinEntitys = wupinService.findZhaoLingList(startIndex, pageSize);
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

    //招领页面搜索信息处理
    @PostMapping("/search")
    public String search(Wupin Wupin, Model model, HttpSession session) {
        wupinmingcheng = Wupin.getWupinmingcheng();
        if (wupinmingcheng.equals("") || wupinmingcheng.contains("?")) {
//            model.addAttribute("errorMsg", "请输入关键词!");
            Object obj = session.getAttribute("dbWupin");
            if (null != obj) {
                session.removeAttribute("dbWupin");
            }
            return "/lost2.jsp";
        }
        wupinmingcheng = "%" + wupinmingcheng + "%";
        List<Wupin> dbWupin = wupinService.findZhaoLingByWuPinName(wupinmingcheng);
        if (dbWupin.size() > 0) {
            return "redirect:/lost/resSearch";
        } else {
//            model.addAttribute("errorMsg", "该物品不存在!");
            Object obj = session.getAttribute("dbWupin");
            if (null != obj) {
                session.removeAttribute("dbWupin");
            }
            return "/lost2.jsp";
        }
    }

    //返回搜索结果
    @GetMapping("/resSearch")
    public String resSearch(HttpSession session) {
        List<Wupin> dbWupin = wupinService.findZhaoLingByWuPinName(wupinmingcheng);
        session.setAttribute("dbWupin", dbWupin);
        return "/lost2.jsp";
    }

    //详情页
    @GetMapping("/content")
    public String content(String id, Map<String, Object> map) {
        Wupin dbWupin = wupinService.findZhaoLingById(id);
        String zhanDianName = dbWupin.getZhandianmingcheng();
        Zhandian dbZhanDian = null;
        if (!zhanDianName.equals("")) {
            dbZhanDian = zhandainService.findByZhanDianName(zhanDianName);
        }
        dbWupin.setLianxidianhua(dbZhanDian.getLianxidianhua());
        map.put("dbWupin", dbWupin);
        return "/lostContent.jsp";
    }

    //添加申领
    @RequestMapping("/SLSH")
    @ResponseBody//返回json数据到前端
    public String SLSH(@RequestBody Wupin wupin) {
        String userid = wupin.getUserid();
        Integer id = wupin.getId();
        String wuPinId = String.valueOf(id);
        Date daozhandianriqi = wupin.getDaozhandianriqi();
        String zhuangtai = wupin.getZhuangtai();
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(wuPinId) || StringUtils.isEmpty(zhuangtai) || daozhandianriqi == null) {
            return "false";
        }
        Wupin findWuPin = wupinService.findSLSHByUserIdAndId(wuPinId,userid);
        if (null!=findWuPin){
            return "0";
        }
        Wupin dbWuPin = wupinService.findZhaoLingById(wuPinId);
        if (null != dbWuPin) {
            Date addtie = new Date();
            wupin.setWupinmingcheng(dbWuPin.getWupinmingcheng());
            wupin.setBianhao(dbWuPin.getBianhao());
            wupin.setTupian(dbWuPin.getTupian());
            wupin.setJiandaoriqi(dbWuPin.getJiandaoriqi());
            wupin.setJiandaodizhi(dbWuPin.getJiandaodizhi());
            wupin.setJianshu(dbWuPin.getJianshu());
            wupin.setZhandianmingcheng(dbWuPin.getZhandianmingcheng());
            wupin.setFuzeren(dbWuPin.getFuzeren());
            wupin.setDizhi(dbWuPin.getDizhi());
            wupin.setShenhejieguo("审核中");
            wupin.setAddtime(addtie);
            wupinService.addSLSH(wupin);
            return "succeful";
        } else {
            return "false";
        }
    }


    //个人中心获取申领审核中信息
    @RequestMapping("/userSLSH")
    @ResponseBody//返回json数据到前端
    public String userSLSH(@RequestBody Wupin wupin) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        String userid = wupin.getUserid();
        userid = userid.replace(" ", "");
        if ("".equals(userid) || userid.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");
        } else {
            List<Wupin> wuPinEntitys = wupinService.findSLSHByUserId(wupin.getUserid());
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                    String tupian = wuPinEntity.getTupian();
                    String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                    Zhandian dbZhanDian = zhandainService.findByZhanDianName(zhandianmingcheng);
                    String lianxidianhua = dbZhanDian.getLianxidianhua();
                    String dizhi = wuPinEntity.getDizhi();
                    String shenhejieguo = wuPinEntity.getShenhejieguo();
                    Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    String date2 = formatter.format(daozhandianriqi);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                    ggJson.append("\"daozhandianriqi\":" + "\"" + date2 + "\"" + ",");
                    ggJson.append("\"lianxidianhua\":" + "\"" + lianxidianhua + "\"" + ",");
                    ggJson.append("\"dizhi\":" + "\"" + dizhi + "\"" + ",");
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

    //个人中心获取申领未通过信息
    @RequestMapping("/userSLJJ")
    @ResponseBody//返回json数据到前端
    public String userSLJJ(@RequestBody Wupin wupin) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        String userid = wupin.getUserid();
        userid = userid.replace(" ", "");
        if ("".equals(userid) || userid.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");
        } else {
            List<Wupin> wuPinEntitys = wupinService.findSLSHByUserIdJJ(wupin.getUserid());
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                    String tupian = wuPinEntity.getTupian();
                    String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                    Zhandian dbZhanDian = zhandainService.findByZhanDianName(zhandianmingcheng);
                    String lianxidianhua = dbZhanDian.getLianxidianhua();
                    String dizhi = wuPinEntity.getDizhi();
                    String shenhejieguo = wuPinEntity.getShenhejieguo();
                    Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    String date2 = formatter.format(daozhandianriqi);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                    ggJson.append("\"daozhandianriqi\":" + "\"" + date2 + "\"" + ",");
                    ggJson.append("\"lianxidianhua\":" + "\"" + lianxidianhua + "\"" + ",");
                    ggJson.append("\"dizhi\":" + "\"" + dizhi + "\"" + ",");
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

    //个人中心获取申领正在进行信息
    @RequestMapping("/userSLJX")
    @ResponseBody//返回json数据到前端
    public String userSLJX(@RequestBody Wupin wupin) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        String userid = wupin.getUserid();
        userid = userid.replace(" ", "");
        if ("".equals(userid) || userid.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");
        } else {
            List<Wupin> wuPinEntitys = wupinService.findSLSHByUserIdJX(wupin.getUserid());
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                    String tupian = wuPinEntity.getTupian();
                    String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                    Zhandian dbZhanDian = zhandainService.findByZhanDianName(zhandianmingcheng);
                    String lianxidianhua = dbZhanDian.getLianxidianhua();
                    String dizhi = wuPinEntity.getDizhi();
                    String shenhejieguo = wuPinEntity.getShenhejieguo();
                    Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    String date2 = formatter.format(daozhandianriqi);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                    ggJson.append("\"daozhandianriqi\":" + "\"" + date2 + "\"" + ",");
                    ggJson.append("\"lianxidianhua\":" + "\"" + lianxidianhua + "\"" + ",");
                    ggJson.append("\"dizhi\":" + "\"" + dizhi + "\"" + ",");
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

    //个人中心获取申领已完成信息
    @RequestMapping("/userSLWC")
    @ResponseBody//返回json数据到前端
    public String userSLWC(@RequestBody Wupin wupin) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        String userid = wupin.getUserid();
        userid = userid.replace(" ", "");
        if ("".equals(userid) || userid.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");
        } else {
            List<Wupin> wuPinEntitys = wupinService.findSLSHByUserIdWC(wupin.getUserid());
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                    String tupian = wuPinEntity.getTupian();
                    String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                    Zhandian dbZhanDian = zhandainService.findByZhanDianName(zhandianmingcheng);
                    String lianxidianhua = dbZhanDian.getLianxidianhua();
                    String dizhi = wuPinEntity.getDizhi();
                    String shenhejieguo = wuPinEntity.getShenhejieguo();
                    Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    String date2 = formatter.format(daozhandianriqi);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                    ggJson.append("\"daozhandianriqi\":" + "\"" + date2 + "\"" + ",");
                    ggJson.append("\"lianxidianhua\":" + "\"" + lianxidianhua + "\"" + ",");
                    ggJson.append("\"dizhi\":" + "\"" + dizhi + "\"" + ",");
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

    //个人中心申领审核物品详情页
    @GetMapping("/SLSHContent")
    public String SLSHContent(String id, Map<String, Object> map) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        Wupin dbWupin = wupinService.findSLSHById(id);
        map.put("dbWupin", dbWupin);
        return "/userSLContent.jsp";
    }

    //个人中心根据物品id删除申领审核物品
    @GetMapping("/SLSHDelete")
    public String SLSHDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.deleteSLSHById(id);
        return "redirect:/userShenLing.jsp";
    }


    //个人中心招领审核中列表
    @RequestMapping("/userZLSH")
    @ResponseBody//返回json数据到前端
    public String userZLSH(@RequestBody Wupin wupin) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        String userid = wupin.getUserid();
        userid = userid.replace(" ", "");
        if ("".equals(userid) || userid.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");

        } else {
            List<Wupin> wuPinEntitys = wupinService.findZLSHByUserId(wupin.getUserid());
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String bianhao = wuPinEntity.getBianhao();
                    String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                    String tupian = wuPinEntity.getTupian();
                    String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                    String shenhejieguo = wuPinEntity.getShenhejieguo();
                    Date jiandaoriqi = wuPinEntity.getJiandaoriqi();
                    Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    String date2 = formatter.format(jiandaoriqi);
                    String date3 = formatter.format(daozhandianriqi);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                    ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                    ggJson.append("\"jiandaoriqi\":" + "\"" + date2 + "\"" + ",");
                    ggJson.append("\"daozhandianriqi\":" + "\"" + date3 + "\"" + ",");
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

    //个人中心招领审核未通过列表
    @RequestMapping("/userZLSHJJ")
    @ResponseBody//返回json数据到前端
    public String userZLSHJJ(@RequestBody Wupin wupin) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        String userid = wupin.getUserid();
        userid = userid.replace(" ", "");
        if ("".equals(userid) || userid.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");

        } else {
            List<Wupin> wuPinEntitys = wupinService.findZLSHByUserIdJJ(wupin.getUserid());
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String bianhao = wuPinEntity.getBianhao();
                    String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                    String tupian = wuPinEntity.getTupian();
                    String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                    String shenhejieguo = wuPinEntity.getShenhejieguo();
                    Date jiandaoriqi = wuPinEntity.getJiandaoriqi();
                    Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    String date2 = formatter.format(jiandaoriqi);
                    String date3 = formatter.format(daozhandianriqi);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                    ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                    ggJson.append("\"jiandaoriqi\":" + "\"" + date2 + "\"" + ",");
                    ggJson.append("\"daozhandianriqi\":" + "\"" + date3 + "\"" + ",");
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

    //个人中心招领审核详情页
    @GetMapping("/ZLSHContent")
    public String ZLSHContent(String id, Map<String, Object> map) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        Wupin dbWupin = wupinService.findZLSHById(id);
        map.put("dbWupin", dbWupin);
        return "/userZLContent.jsp";
    }

    //个人中心招领审核删除
    @GetMapping("/ZLSHDelete")
    public String ZLSHDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.deleteZLSHById(id);
        return "redirect:/userZhaoLing.jsp";
    }

    //个人中心招领公示中列表
    @RequestMapping("/userSWZL")
    @ResponseBody//返回json数据到前端
    public String userSWZL(@RequestBody Wupin wupin) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        String userid = wupin.getUserid();
        userid = userid.replace(" ", "");
        if ("".equals(userid) || userid.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");

        } else {
            List<Wupin> wuPinEntitys = wupinService.findZLByUserId(wupin.getUserid());
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String bianhao = wuPinEntity.getBianhao();
                    String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                    String tupian = wuPinEntity.getTupian();
                    String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                    Date jiandaoriqi = wuPinEntity.getJiandaoriqi();
                    Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    String date2 = formatter.format(jiandaoriqi);
                    String date3 = formatter.format(daozhandianriqi);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                    ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                    ggJson.append("\"jiandaoriqi\":" + "\"" + date2 + "\"" + ",");
                    ggJson.append("\"daozhandianriqi\":" + "\"" + date3 + "\"" + ",");
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

    //个人中心招领已完成列表
    @RequestMapping("/userSWWC")
    @ResponseBody//返回json数据到前端
    public String userSWWC(@RequestBody Wupin wupin) {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        String userid = wupin.getUserid();
        userid = userid.replace(" ", "");
        if ("".equals(userid) || userid.length() == 0) {
            ggJson.append("\"status\":" + "\" false \"");

        } else {
            List<Wupin> wuPinEntitys = wupinService.findZLByUserIdWC(wupin.getUserid());
            if (wuPinEntitys.size() > 0) {
                ggJson.append("\"status\":" + "\" true \"" + ",");
                ggJson.append("\"list\":");
                ggJson.append("[");
                for (int i = 0; i < wuPinEntitys.size(); i++) {
                    Wupin wuPinEntity = wuPinEntitys.get(i);
                    String bianhao = wuPinEntity.getBianhao();
                    String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                    String tupian = wuPinEntity.getTupian();
                    String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                    Date jiandaoriqi = wuPinEntity.getJiandaoriqi();
                    Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                    Date addtime = wuPinEntity.getAddtime();
                    Integer id = wuPinEntity.getId();
                    String zhuangtai = wuPinEntity.getZhuangtai();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String date = formatter.format(addtime);
                    String date2 = formatter.format(jiandaoriqi);
                    String date3 = formatter.format(daozhandianriqi);
                    ggJson.append("{");
                    ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                    ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                    ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                    ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                    ggJson.append("\"jiandaoriqi\":" + "\"" + date2 + "\"" + ",");
                    ggJson.append("\"daozhandianriqi\":" + "\"" + date3 + "\"" + ",");
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

    //个人中心招领详情页
    @GetMapping("/ZLContent")
    public String ZLContent(String id, Map<String, Object> map) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        Wupin dbWupin = wupinService.findZhaoLingById(id);
        map.put("dbWupin", dbWupin);
        return "/userZLContent.jsp";
    }

    //个人中心招领删除
    @GetMapping("/ZLDelete")
    public String ZLDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.deleteZLById(id);
        return "redirect:/userZhaoLing.jsp";
    }

    //管理员招领审核列表
    @RequestMapping("/HTZLSH")
    @ResponseBody//返回json数据到前端
    public String HTZLSH() {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        List<Wupin> wuPinEntitys = wupinService.findAllZLSH();
        if (wuPinEntitys.size() > 0) {
            ggJson.append("\"status\":" + "\" true \"" + ",");
            ggJson.append("\"list\":");
            ggJson.append("[");
            for (int i = 0; i < wuPinEntitys.size(); i++) {
                Wupin wuPinEntity = wuPinEntitys.get(i);
                String bianhao = wuPinEntity.getBianhao();
                String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                String tupian = wuPinEntity.getTupian();
                String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                Date jiandaoriqi = wuPinEntity.getJiandaoriqi();
                Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                Date addtime = wuPinEntity.getAddtime();
                Integer id = wuPinEntity.getId();
                String zhuangtai = wuPinEntity.getZhuangtai();
                String shenhejieguo = wuPinEntity.getShenhejieguo();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(addtime);
                String date2 = formatter.format(jiandaoriqi);
                String date3 = formatter.format(daozhandianriqi);
                ggJson.append("{");
                ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                ggJson.append("\"jiandaoriqi\":" + "\"" + date2 + "\"" + ",");
                ggJson.append("\"daozhandianriqi\":" + "\"" + date3 + "\"" + ",");
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

    //管理员查看招领审核详情页
    @GetMapping("/HTZLSHContent")
    public String HTZLSHContent(String id, Map<String, Object> map) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        Wupin dbWupin = wupinService.findZLSHById(id);
        map.put("dbWupin", dbWupin);
        return "/houTaiZLContent.jsp";
    }

    //管理员招领审核同意
    @GetMapping("/HTZLSHTYUpdate")
    public String HTZLSHTYUpdate(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.updateZLSH(id);
        Wupin dbWupin = wupinService.findZLSHById(id);
        wupinService.addZL(dbWupin);
        return "redirect:/houTaiZhaoLing.jsp";
    }

    //管理员招领审核拒绝
    @GetMapping("/HTZLSHJJUpdate")
    public String HTZLSHJJUpdate(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.updateJJZLSH(id);
        return "redirect:/houTaiZhaoLing.jsp";
    }

    //管理员招领公告公示中列表
    @RequestMapping("/HTSWZL")
    @ResponseBody//返回json数据到前端
    public String HTSWZL() {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        List<Wupin> wuPinEntitys = wupinService.findAllSWZL();
        if (wuPinEntitys.size() > 0) {
            ggJson.append("\"status\":" + "\" true \"" + ",");
            ggJson.append("\"list\":");
            ggJson.append("[");
            for (int i = 0; i < wuPinEntitys.size(); i++) {
                Wupin wuPinEntity = wuPinEntitys.get(i);
                String bianhao = wuPinEntity.getBianhao();
                String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                String tupian = wuPinEntity.getTupian();
                String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                Date jiandaoriqi = wuPinEntity.getJiandaoriqi();
                Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                Date addtime = wuPinEntity.getAddtime();
                Integer id = wuPinEntity.getId();
                String zhuangtai = wuPinEntity.getZhuangtai();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(addtime);
                String date2 = formatter.format(jiandaoriqi);
                String date3 = formatter.format(daozhandianriqi);
                ggJson.append("{");
                ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                ggJson.append("\"jiandaoriqi\":" + "\"" + date2 + "\"" + ",");
                ggJson.append("\"daozhandianriqi\":" + "\"" + date3 + "\"" + ",");
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

    //管理员查看招领公示中公告详情页
    @GetMapping("/HTSWZLContent")
    public String HTSWZLContent(String id, Map<String, Object> map) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        Wupin dbWupin = wupinService.findZhaoLingById(id);
        map.put("dbWupin", dbWupin);
        return "/houTaiZLContent.jsp";
    }

    //管理员提交完成招领公示中的公告
    @GetMapping("/HTSWZLWCUpdate")
    public String HTSWZLWCUpdate(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.updateSWZL(id);
        return "redirect:/houTaiZhaoLing.jsp";
    }

    //管理员删除招领公示公告
    @GetMapping("/HTSWZLDelete")
    public String HTSWZLDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.deleteZLById(id);
        return "redirect:/houTaiZhaoLing.jsp";
    }

    //管理员招领公告已完成列表
    @RequestMapping("/HTZLWC")
    @ResponseBody//返回json数据到前端
    public String HTZLWC() {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        List<Wupin> wuPinEntitys = wupinService.findAllSWZLWC();
        if (wuPinEntitys.size() > 0) {
            ggJson.append("\"status\":" + "\" true \"" + ",");
            ggJson.append("\"list\":");
            ggJson.append("[");
            for (int i = 0; i < wuPinEntitys.size(); i++) {
                Wupin wuPinEntity = wuPinEntitys.get(i);
                String bianhao = wuPinEntity.getBianhao();
                String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                String tupian = wuPinEntity.getTupian();
                String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                Date jiandaoriqi = wuPinEntity.getJiandaoriqi();
                Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                Date addtime = wuPinEntity.getAddtime();
                Integer id = wuPinEntity.getId();
                String zhuangtai = wuPinEntity.getZhuangtai();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(addtime);
                String date2 = formatter.format(jiandaoriqi);
                String date3 = formatter.format(daozhandianriqi);
                ggJson.append("{");
                ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                ggJson.append("\"bianhao\":" + "\"" + bianhao + "\"" + ",");
                ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                ggJson.append("\"jiandaoriqi\":" + "\"" + date2 + "\"" + ",");
                ggJson.append("\"daozhandianriqi\":" + "\"" + date3 + "\"" + ",");
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

    //管理员获取申领审核中信息
    @RequestMapping("/HTSLSH")
    @ResponseBody//返回json数据到前端
    public String HTSLSH() {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        List<Wupin> wuPinEntitys = wupinService.findAllSLSH();
        if (wuPinEntitys.size() > 0) {
            ggJson.append("\"status\":" + "\" true \"" + ",");
            ggJson.append("\"list\":");
            ggJson.append("[");
            for (int i = 0; i < wuPinEntitys.size(); i++) {
                Wupin wuPinEntity = wuPinEntitys.get(i);
                Integer userid = Integer.parseInt(wuPinEntity.getUserid());
                User dbUser =userService.findByUserId(userid);
                String userName =dbUser.getUsername();
                String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                String tupian = wuPinEntity.getTupian();
                String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                Zhandian dbZhanDian = zhandainService.findByZhanDianName(zhandianmingcheng);
                String lianxidianhua = dbZhanDian.getLianxidianhua();
                String dizhi = wuPinEntity.getDizhi();
                String shenhejieguo = wuPinEntity.getShenhejieguo();
                Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                Date addtime = wuPinEntity.getAddtime();
                Integer id = wuPinEntity.getId();
                String zhuangtai = wuPinEntity.getZhuangtai();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(addtime);
                String date2 = formatter.format(daozhandianriqi);
                ggJson.append("{");
                ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                ggJson.append("\"userName\":" + "\"" + userName + "\"" + ",");
                ggJson.append("\"daozhandianriqi\":" + "\"" + date2 + "\"" + ",");
                ggJson.append("\"lianxidianhua\":" + "\"" + lianxidianhua + "\"" + ",");
                ggJson.append("\"dizhi\":" + "\"" + dizhi + "\"" + ",");
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

    //管理员查看申领审核的详情页
    @GetMapping("/HTSLSHContent")
    public String HTSLSHContent(String id, Map<String, Object> map) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        Wupin dbWupin = wupinService.findSLSHById(id);
        Integer userid = Integer.parseInt(dbWupin.getUserid());
        User dbUser =userService.findByUserId(userid);
        String userName =dbUser.getUsername();
        map.put("dbWupin", dbWupin);
        map.put("userName", userName);
        return "/houTaiSLContent.jsp";
    }

    //管理员申领审核同意
    @GetMapping("/HTSLSHTYUpdate")
    public String HTSLSHTYUpdate(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.updateSLSH(id);
        return "redirect:/houTaiShenLing.jsp";
    }

    //管理员申领审核拒绝
    @GetMapping("/HTSLSHJJUpdate")
    public String HTSLSHJJUpdate(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.updateJJSLSH(id);
        return "redirect:/houTaiShenLing.jsp";
    }

    //管理员获取申领正在进行中信息
    @RequestMapping("/HTSLJX")
    @ResponseBody//返回json数据到前端
    public String HTSLJX() {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        List<Wupin> wuPinEntitys = wupinService.findAllSLSHJX();
        if (wuPinEntitys.size() > 0) {
            ggJson.append("\"status\":" + "\" true \"" + ",");
            ggJson.append("\"list\":");
            ggJson.append("[");
            for (int i = 0; i < wuPinEntitys.size(); i++) {
                Wupin wuPinEntity = wuPinEntitys.get(i);
                Integer userid = Integer.parseInt(wuPinEntity.getUserid());
                User dbUser =userService.findByUserId(userid);
                String userName =dbUser.getUsername();
                String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                String tupian = wuPinEntity.getTupian();
                String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                Zhandian dbZhanDian = zhandainService.findByZhanDianName(zhandianmingcheng);
                String lianxidianhua = dbZhanDian.getLianxidianhua();
                String dizhi = wuPinEntity.getDizhi();
                String shenhejieguo = wuPinEntity.getShenhejieguo();
                Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                Date addtime = wuPinEntity.getAddtime();
                Integer id = wuPinEntity.getId();
                String zhuangtai = wuPinEntity.getZhuangtai();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(addtime);
                String date2 = formatter.format(daozhandianriqi);
                ggJson.append("{");
                ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                ggJson.append("\"userName\":" + "\"" + userName + "\"" + ",");
                ggJson.append("\"daozhandianriqi\":" + "\"" + date2 + "\"" + ",");
                ggJson.append("\"lianxidianhua\":" + "\"" + lianxidianhua + "\"" + ",");
                ggJson.append("\"dizhi\":" + "\"" + dizhi + "\"" + ",");
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

    //管理员提交完成申领请求
    @GetMapping("/HTSLSHWCUpdate")
    public String HTSLSHWCUpdate(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.updateSLSHWC(id);
        wupinService.updateSWZL(id);
        return "redirect:/houTaiShenLing.jsp";
    }

    //管理员删除正在进行的申领请求
    @GetMapping("/HTSLSHDelete")
    public String HTSLSHDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        wupinService.deleteSLSHById(id);
        return "redirect:/houTaiShenLing.jsp";
    }

    //管理员获取申领已完成的信息
    @RequestMapping("/HTSLWC")
    @ResponseBody//返回json数据到前端
    public String HTSLWC() {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        List<Wupin> wuPinEntitys = wupinService.findAllSLSHWC();
        if (wuPinEntitys.size() > 0) {
            ggJson.append("\"status\":" + "\" true \"" + ",");
            ggJson.append("\"list\":");
            ggJson.append("[");
            for (int i = 0; i < wuPinEntitys.size(); i++) {
                Wupin wuPinEntity = wuPinEntitys.get(i);
                Integer userid = Integer.parseInt(wuPinEntity.getUserid());
                User dbUser =userService.findByUserId(userid);
                String userName =dbUser.getUsername();
                String wupinmingcheng = wuPinEntity.getWupinmingcheng();
                String tupian = wuPinEntity.getTupian();
                String zhandianmingcheng = wuPinEntity.getZhandianmingcheng();
                Zhandian dbZhanDian = zhandainService.findByZhanDianName(zhandianmingcheng);
                String lianxidianhua = dbZhanDian.getLianxidianhua();
                String dizhi = wuPinEntity.getDizhi();
                String shenhejieguo = wuPinEntity.getShenhejieguo();
                Date daozhandianriqi = wuPinEntity.getDaozhandianriqi();
                Date addtime = wuPinEntity.getAddtime();
                Integer id = wuPinEntity.getId();
                String zhuangtai = wuPinEntity.getZhuangtai();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(addtime);
                String date2 = formatter.format(daozhandianriqi);
                ggJson.append("{");
                ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
                ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
                ggJson.append("\"wupinmingcheng\":" + "\"" + wupinmingcheng + "\"" + ",");
                ggJson.append("\"userName\":" + "\"" + userName + "\"" + ",");
                ggJson.append("\"daozhandianriqi\":" + "\"" + date2 + "\"" + ",");
                ggJson.append("\"lianxidianhua\":" + "\"" + lianxidianhua + "\"" + ",");
                ggJson.append("\"dizhi\":" + "\"" + dizhi + "\"" + ",");
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
}
