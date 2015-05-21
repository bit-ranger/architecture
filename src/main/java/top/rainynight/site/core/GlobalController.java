package top.rainynight.site.core;

import org.apache.commons.lang3.StringUtils;
import top.rainynight.site.core.util.StaticResourceHolder;
import top.rainynight.site.user.entity.User;
import top.rainynight.site.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/")
public class GlobalController  {

    @javax.annotation.Resource(name = "userService")
    private UserService userService;

    @RequestMapping
    public String index(){
        return "forward:/blog";
    }

    @RequestMapping("404")
    public String redirect404(ModelMap modelMap){
        modelMap.clear();
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
        return "/login";
    }

    /**
     * 处理登陆请求
     * @return
     */
    @RequestMapping(value = "landing",method = RequestMethod.POST)
    public String landing(User user, HttpSession session, String redirectURL, ModelMap modelMap) throws UnsupportedEncodingException {
        modelMap.clear();
        user = userService.get(user);
        boolean hasRedirect = StringUtils.isNotBlank(redirectURL);
        if(user == null){
            String datas = "";
            if(hasRedirect){
                redirectURL = URLEncoder.encode(redirectURL, StaticResourceHolder.URL_ENCODING);
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
        return session.getAttribute(StaticResourceHolder.USER_SESSION_NAME) != null;
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpSession session, ModelMap modelMap){
        modelMap.clear();
        session.removeAttribute(StaticResourceHolder.USER_SESSION_NAME);
        return "redirect:/";
    }

}
