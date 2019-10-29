package com.rainyalley.architecture.controller;

import com.rainyalley.architecture.vo.ApiError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


/**
 * @author bin.zhang
 */
@ApiResponses({@ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)})
@Api(value = "root", description = "主入口")
@Controller
public class RootController {

    @Autowired
    private BuildProperties buildProperties;

    @ApiOperation(value="html首页")
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView indexHtml(ModelMap modelMap) {
        modelMap.put("version", buildProperties.getVersion());
        modelMap.put("buildTime", new Date(buildProperties.getTime().toEpochMilli()));
        modelMap.put("javaVersion", buildProperties.get("java.version"));
        return new ModelAndView("index", modelMap);
    }


    @ApiOperation(value="json首页")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BuildProperties indexJson() {
        return buildProperties;
    }

    @ApiOperation(value="rest文档")
    @GetMapping(value = "doc/rest", produces = MediaType.TEXT_HTML_VALUE)
    public String docRestHtml(){
        return "redirect:/swagger-ui.html";
    }

    @ApiOperation(value="文档" , produces = MediaType.TEXT_HTML_VALUE)
    @GetMapping(value = "doc")
    public String docHtml(){
        return "redirect:/doc/rest";
    }

}
