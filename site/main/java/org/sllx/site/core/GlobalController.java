package org.sllx.site.core;

import org.sllx.core.util.StringUtils;
import org.sllx.site.core.base.BaseController;
import org.sllx.site.user.entity.User;
import org.sllx.site.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by sllx on 14-11-28.
 */
@Controller
@RequestMapping("/")
public class GlobalController extends BaseController{

    @javax.annotation.Resource(name = "userService")
    private UserService userService;

    @RequestMapping
    public String index(){
        return "forward:/blog";
    }

    @RequestMapping("404")
    public String redirect404(){
        return "redirect:/";
    }

    /**
     * 跳转至登陆页
     * @param modelMap
     * @return
     */
    @RequestMapping("login")
    public String login(ModelMap modelMap, String redirectURL){
        modelMap.addAttribute("redirectURL",redirectURL);
        modelMap.addAttribute("landingURL",selfURL("landing","landingURL"));
        return "/login";
    }

    /**
     * 处理登陆请求
     * @return
     */
    @RequestMapping(value = "landing",method = RequestMethod.POST)
    public String landing(User user, HttpSession session, String redirectURL) throws UnsupportedEncodingException {
        user = userService.get(user);
        boolean hasRedirect = StringUtils.isNotBlank(redirectURL);
        if(user == null){
            String datas = "";
            if(hasRedirect){
                redirectURL = URLEncoder.encode(redirectURL,"UTF-8");
                datas += "?redirectURL=" + redirectURL;
            }
            return "redirect:/login" + datas;
        }
        session.setAttribute("user",user);
        //登陆成功后的跳转
        redirectURL = hasRedirect ? redirectURL : "/user";
        return "redirect:" + redirectURL;
    }

    @RequestMapping("loginCheck")
    public @ResponseBody boolean loginCheck(HttpSession session){
        return session.getAttribute("user") != null;
    }

}
