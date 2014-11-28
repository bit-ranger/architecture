package org.sllx.site.core;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by sllx on 14-11-28.
 */
@Controller
@RequestMapping("/")
public class IndexController {
    @RequestMapping
    public String index(){
        return "forward:/blog";
    }

    @RequestMapping("404")
    public String redirect404(){
        return "redirect:/";
    }
}
