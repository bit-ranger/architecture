package com.rainyalley.architecture.controller;

import com.rainyalley.architecture.model.User;
import com.rainyalley.architecture.service.UserService;
import com.rainyalley.architecture.vo.UserVo;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController{

    @Resource
    private UserService userService;

    @RequestMapping(value = "/{id:[0-9]{1,9}}", method = RequestMethod.GET)
    public ResponseEntity<UserVo> get(@PathVariable("id") String id){
        User user = userService.get(id);
        if(user != null){
            UserVo userVo = new UserVo();
            try {
                PropertyUtils.copyProperties(userVo, user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok().body(userVo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> list(){
        return ResponseEntity.ok(Collections.emptyList());
    }
}
