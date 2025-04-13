package com.swzl.controller;

import com.swzl.entity.Notice;
import com.swzl.entity.User;
import com.swzl.entity.Wupin;
import com.swzl.service.NoticeService;
import com.swzl.service.UserService;
import com.swzl.service.WupinService;
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
@RequestMapping("/swzl")
public class IndexController {
    @Autowired
    private WupinService wupinService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserService userService;

    public String wupinmingcheng = "";


    //打开主页自动获取公告
    @RequestMapping("/notice")
    @ResponseBody//返回json数据到前端
    public String ajaxIndexGG() {
        StringBuffer ggJson = new StringBuffer("[");
        List<Notice> noticeEntitys = noticeService.findNotice(0, 3);
        for (int i = 0; i < noticeEntitys.size(); i++) {
            Notice noticeEntity = noticeEntitys.get(i);
            String title = noticeEntity.getG_title().replace("\r\n", "<br/>").replace("\r", "<br/>").replace("\n", "<br/>");
            String content = noticeEntity.getG_content().replace("\r\n", "<br/>").replace("\r", "<br/>").replace("\n", "<br/>");
            Integer id = noticeEntity.getG_id();
            ggJson.append("{");
            ggJson.append("\"g_title\":" + "\"" + title + "\"" + ",");
            ggJson.append("\"g_id\":" + "\"" + id + "\"" + ",");
            ggJson.append("\"g_content\":" + "\"" + content + "\"");
            ggJson.append("}");
            if (i < noticeEntitys.size() - 1) {
                ggJson.append(",");
            }
        }
        ggJson.append("]");
        String resJson = ggJson.toString();
        return resJson;
    }


    //打开主页自动获取招领信息
    @RequestMapping("/zhaoling")
    @ResponseBody//返回json数据到前端
    public String ajaxIndexZL() {
        StringBuffer ggJson = new StringBuffer("[");
        List<Wupin> wuPinEntitys = wupinService.findZhaoLingList(0, 3);
        for (int i = 0; i < wuPinEntitys.size(); i++) {
            Wupin wuPinEntity = wuPinEntitys.get(i);
            String title = wuPinEntity.getWupinmingcheng();
            Integer id = wuPinEntity.getId();
            String tupian = wuPinEntity.getTupian();
            String jianshu = wuPinEntity.getJianshu();
            String zhuangtai = wuPinEntity.getZhuangtai();
            if (jianshu.length() > 50) {
                jianshu = jianshu.substring(0, 48) + "...";
            }
            ggJson.append("{");
            ggJson.append("\"title\":" + "\"" + title + "\"" + ",");
            ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
            ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
            ggJson.append("\"zhuangtai\":" + "\"" + zhuangtai + "\"" + ",");
            ggJson.append("\"jianshu\":" + "\"" + jianshu + "\"");
            ggJson.append("}");
            if (i < wuPinEntitys.size() - 1) {
                ggJson.append(",");
            }
        }
        ggJson.append("]");
        String resJson = ggJson.toString();
        return resJson;
    }


    //打开主页自动获取寻物信息
    @RequestMapping("/xunwu")
    @ResponseBody//返回json数据到前端
    public String ajaxIndexXW() {
        StringBuffer ggJson = new StringBuffer("[");
        List<Wupin> wuPinEntitys = wupinService.findXunWuList(0, 3);
        for (int i = 0; i < wuPinEntitys.size(); i++) {
            Wupin wuPinEntity = wuPinEntitys.get(i);
            String title = wuPinEntity.getWupinmingcheng();
            Integer id = wuPinEntity.getId();
            String tupian = wuPinEntity.getTupian();
            String zhuangtai = wuPinEntity.getZhuangtai();
            String jianshu = wuPinEntity.getJianshu();
            if (jianshu.length() > 50) {
                jianshu = jianshu.substring(0, 48) + "...";
            }
            ggJson.append("{");
            ggJson.append("\"title\":" + "\"" + title + "\"" + ",");
            ggJson.append("\"id\":" + "\"" + id + "\"" + ",");
            ggJson.append("\"tupian\":" + "\"" + tupian + "\"" + ",");
            ggJson.append("\"zhuangtai\":" + "\"" + zhuangtai + "\"" + ",");
            ggJson.append("\"jianshu\":" + "\"" + jianshu + "\"");
            ggJson.append("}");
            if (i < wuPinEntitys.size() - 1) {
                ggJson.append(",");
            }
        }
        ggJson.append("]");
        String resJson = ggJson.toString();
        return resJson;
    }

    //显示公告
    @GetMapping("/ggContent")
    public String ggContent(String id, Map map) {
        Notice dbNotice = noticeService.findNoticeById(id);
        map.put("dbNotice", dbNotice);
        return "/ggContent.jsp";
    }

    //主页搜索
    @PostMapping("/search")
    public String search(Wupin Wupin, Model model, HttpSession session) {
        wupinmingcheng = Wupin.getWupinmingcheng();
        if (wupinmingcheng.equals("")) {
//            model.addAttribute("errorMsg", "请输入关键词!");
            Object obj = session.getAttribute("dbWupin");
            if (null != obj) {
                session.removeAttribute("dbWupin");
            }
            return "/findall.jsp";
        }
        wupinmingcheng = "%" + wupinmingcheng + "%";
        List<Wupin> dbWupin = wupinService.findXunWuByWuPinName(wupinmingcheng);
        List<Wupin> dbWupin2 = wupinService.findZhaoLingByWuPinName(wupinmingcheng);
        if (dbWupin.size() > 0 || dbWupin2.size() > 0) {
            return "redirect:/swzl/resSearch";
        } else {
//            model.addAttribute("errorMsg", "该物品不存在!");
            Object obj = session.getAttribute("dbWupin");
            if (null != obj) {
                session.removeAttribute("dbWupin");
            }
            return "/findall.jsp";
        }
    }

    //显示主页搜索结果
    @GetMapping("/resSearch")
    public String resSearch(HttpSession session) {
        List<Wupin> dbWupin = wupinService.findXunWuByWuPinName(wupinmingcheng);
        List<Wupin> dbWupin2 = wupinService.findZhaoLingByWuPinName(wupinmingcheng);
        dbWupin.addAll(dbWupin2);
        session.setAttribute("dbWupin", dbWupin);
        return "/findall.jsp";
    }

    //后台获取公告列表
    @RequestMapping("/HTGGList")
    @ResponseBody//返回json数据到前端
    public String HTGGList() {
        StringBuffer ggJson = new StringBuffer();
        ggJson.append("{");
        List<Notice> noticeEntitys = noticeService.findAllNotice();
        if (noticeEntitys.size() > 0) {
            ggJson.append("\"status\":" + "\" true \"" + ",");
            ggJson.append("\"list\":");
            ggJson.append("[");
            for (int i = 0; i < noticeEntitys.size(); i++) {
                Notice noticeEntity = noticeEntitys.get(i);
                String title = noticeEntity.getG_title().replace("\r\n", "<br/>").replace("\r", "<br/>").replace("\n", "<br/>");
                String content = noticeEntity.getG_content().replace("\r\n", "<br/>").replace("\r", "<br/>").replace("\n", "<br/>");
                String g_username = noticeEntity.getG_username();
                String g_authority = noticeEntity.getG_authority();
                Date g_addtime = noticeEntity.getG_addTime();
                Integer id = noticeEntity.getG_id();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(g_addtime);
                ggJson.append("{");
                ggJson.append("\"g_title\":" + "\"" + title + "\"" + ",");
                ggJson.append("\"g_id\":" + "\"" + id + "\"" + ",");
                ggJson.append("\"g_username\":" + "\"" + g_username + "\"" + ",");
                ggJson.append("\"g_authority\":" + "\"" + g_authority + "\"" + ",");
                ggJson.append("\"date\":" + "\"" + date + "\"" + ",");
                ggJson.append("\"g_content\":" + "\"" + content + "\"");
                ggJson.append("}");
                if (i < noticeEntitys.size() - 1) {
                    ggJson.append(",");
                }
            }
            ggJson.append("]");
        } else {
            ggJson.append("\"status\":" + "\" false \"");
        }
        ggJson.append("}");
        String resJson = ggJson.toString();
        return resJson;
    }

    //后台显示公告详情
    @GetMapping("/HTZDContent")
    public String HTZDContent(String id, Map map) {
        Notice dbNotice = noticeService.findNoticeById(id);
        map.put("dbNotice", dbNotice);
        return "/houTaiggContent.jsp";
    }

    //后台删除公告
    @GetMapping("/HTZDDelete")
    public String HTZDDelete(String id) {
        if ("".equals(id) || id.length() == 0) {
            return "false";
        }
        noticeService.deleteGGById(id);
        return "redirect:/houTaiGongGao.jsp";
    }

    //上传公告
    @RequestMapping("/information")
    @ResponseBody
    public String liuYanAdd(@RequestBody Notice notice) {
        Integer userid = notice.getG_userid();
        String g_username = notice.getG_username();
        if ( StringUtils.isEmpty(userid)|| StringUtils.isEmpty(g_username) ||
                StringUtils.isEmpty(notice.getG_title()) ||
                StringUtils.isEmpty(notice.getG_content())) {
            return "false";
        }
        User dbuser = userService.findByUserId(userid);
        Date addTime = new Date();
        notice.setG_addTime(addTime);
        notice.setG_authority(dbuser.getAuthority());
        noticeService.addGG(notice);
        return "succeful";
    }
}
