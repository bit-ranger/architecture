package org.sllx.site.blog.controller;

import org.sllx.site.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.stereotype.Controller
@RequestMapping("blog")
public class BlogController extends BaseController {

    @RequestMapping(method = RequestMethod.GET)
    public String list(){
        return "blog/list";
    }

}
