package top.rainynight.site.user.controller;

import org.springframework.stereotype.Controller;
import top.rainynight.core.util.Page;
import com.rainyalley.common.user.model.entity.User;
import com.rainyalley.common.user.service.UserService;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController{

    @javax.annotation.Resource(name = "userService")
    private UserService userService;

    /**
     * 查询一组记录
     * @param user
     * @param page
     * @param model
     * @return
     * @throws IllegalAccessException
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list(User user, Page page, ModelMap model) throws IllegalAccessException {
        List<User> users = userService.get(user, page);
        model.addAttribute("users", users);
        return "user/user";
    }


    /**
     * 通过ID查询一条记录
     * @param id
     * @param user
     * @param model
     * @return
     */
    @RequestMapping(value  = "{id:[0-9]{1,9}}", method = RequestMethod.GET)
    public String get(@PathVariable Integer id, User user, ModelMap model){
        user.setId(id);
        user = userService.get(user);
        model.addAttribute("user", user);
        return "user/user";
    }


    /**
     * 创建新记录
     * @param user
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public String post(User user, ModelMap modelMap){
        modelMap.clear();
        userService.save(user);
        return "redirect:/user";
    }


    /**
     * 通过ID更新现有记录
     * @param id
     * @param user
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping(value  = "{id:[0-9]{1,9}}", method = RequestMethod.PUT)
    public String put(@PathVariable int id, User user, ModelMap modelMap) throws InvocationTargetException, IllegalAccessException {
        modelMap.clear();
        user.setId(id);
        userService.save(user);
        return "redirect:/user";
    }


    /**
     * 通过ID删除现有记录
     * @param id
     * @param user
     * @return
     */
    @RequestMapping(value = "{id:[0-9]{1,9}}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable Integer id, User user){
        user.setId(id);
        userService.remove(user);
        return "success";
    }

    /**
     * 通过ID查询一条记录的JSON格式
     * @param id
     * @param user
     * @return
     */
    @RequestMapping(value  = "json/{id:[0-9]{1,9}}", method = RequestMethod.GET)
    public @ResponseBody User json(@PathVariable Integer id, User user){
        user.setId(id);
        return userService.get(user);
    }

}
