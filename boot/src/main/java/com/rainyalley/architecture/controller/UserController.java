package com.rainyalley.architecture.controller;

import com.rainyalley.architecture.model.User;
import com.rainyalley.architecture.po.UserPo;
import com.rainyalley.architecture.service.UserService;
import com.rainyalley.architecture.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Api(value = "user", description = "用户信息管理")
@RestController
@RequestMapping(value = "/user", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController{

    @Resource
    private UserService userService;

    @ApiOperation(value="获取用户信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
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

    @ApiOperation(value="获取用户列表")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserVo>> list(){
        return ResponseEntity.ok(Collections.emptyList());
    }


    @ApiOperation(value="提交用户信息")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserVo> list(@RequestBody UserPo userPo){
        return ResponseEntity.ok(new UserVo());
    }

    @ApiOperation(value="删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @RequestMapping(value = "/{id:[0-9]{1,9}}", method = RequestMethod.DELETE)
    public ResponseEntity<UserVo> delete(@PathVariable("id") String id){
        return ResponseEntity.ok(new UserVo());
    }


    @ApiOperation(value="修改用户信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @RequestMapping(value = "/{id:[0-9]{1,9}}", method = RequestMethod.PUT)
    public ResponseEntity<UserVo> put(@PathVariable("id") String id, @RequestBody UserPo userPo){
        return ResponseEntity.ok(new UserVo());
    }
}
