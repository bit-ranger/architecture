package org.sllx.site.user.controller;

import org.sllx.core.util.Page;
import org.sllx.site.overall.common.CommonController;
import org.sllx.site.overall.util.AwareHolder;
import org.sllx.site.user.entity.User;
import org.sllx.site.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController extends CommonController{

    @javax.annotation.Resource(name = "userService")
    private UserService userService;

    @ModelAttribute("jsonHref")
    public String getJsonHref(){
        return selfLinkBuilder.slash("json").withRel("json").getHref();
    }

    @ModelAttribute("fileHref")
    public String getFileHref(){
        return selfLinkBuilder.slash("file").withRel("file").getHref();
    }

    /**
     * 查询一组记录
     * @param request
     * @param page
     * @param model
     * @return
     * @throws IllegalAccessException
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list(ServletRequest request, Page page, ModelMap model) throws IllegalAccessException {
        List<User> users = userService.list(makeParam(request), page);
        model.addAttribute("users", users);
        return "user";
    }


    /**
     * 通过ID查询一条记录
     * @param id
     * @param user
     * @param model
     * @return
     */
    @RequestMapping(value  = "/{id}", method = RequestMethod.GET)
    public String get(@PathVariable int id, User user, ModelMap model){
        user.setId(id);
        user = userService.get(user);
        model.addAttribute("user", user);
        return "user";
    }


    /**
     * 创建新记录
     * @param user
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public String post(User user){
        userService.insert(user);
        return "redirect:/user";
    }


    /**
     * 通过ID更新现有记录
     * @param id
     * @param user
     * @param request
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping(value  = "/{id}", method = RequestMethod.PUT)
    public String put(@PathVariable int id, User user, ServletRequest request) throws InvocationTargetException, IllegalAccessException {
        user.setId(id);
        user = userService.get(user);
        reset(user, makeParam(request));
        userService.update(user);
        return "redirect:/user";
    }


    /**
     * 通过ID删除现有记录
     * @param id
     * @param user
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable int id, User user){
        user.setId(id);
        userService.delete(user);
        return SUCCESS;
    }

    /**
     * 通过ID查询一条记录的JSON格式
     * @param id
     * @param user
     * @return
     */
    @RequestMapping(value  = "/json/{id}", method = RequestMethod.GET)
    public @ResponseBody User json(@PathVariable int id, User user){
        user.setId(id);
        return userService.get(user);
    }

    /**
     * 上传文件
     * @param uploadFile
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public String upload(@RequestParam MultipartFile uploadFile) throws IOException {
        uploadFile.transferTo(new File(AwareHolder.getServletConfig().getServletContext().getRealPath("/") + "/tmp/" + uploadFile.getOriginalFilename()));
        return "redirect:/user";
    }
}
