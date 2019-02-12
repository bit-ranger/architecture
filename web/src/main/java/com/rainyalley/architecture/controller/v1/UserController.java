package com.rainyalley.architecture.controller.v1;

import com.rainyalley.architecture.Page;
import com.rainyalley.architecture.enums.ResourceEnum;
import com.rainyalley.architecture.exception.NotFoundException;
import com.rainyalley.architecture.impl.UserServiceImpl;
import com.rainyalley.architecture.model.User;
import com.rainyalley.architecture.po.UserAdd;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.Duration;

@Api(value = "v1/user", description = "用户信息管理")
@RestController
@RequestMapping(value = "v1/user", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController{

    @Resource
    private UserServiceImpl userService;

    @ApiOperation(value="获取用户信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @GetMapping(value = "/{id:[0-9]{1,13}}")
    public Mono<User> get(@PathVariable("id") Long id){
        return userService.get(Mono.just(id)).switchIfEmpty(Mono.error(new NotFoundException(ResourceEnum.USER)));
    }

    @ApiOperation(value="获取用户列表")
    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<User> list(Page page){
        return userService.list(Mono.just(page)).delayElements(Duration.ofSeconds(1));
    }


    @ApiOperation(value="提交用户信息")
    @PostMapping
    public Mono<User> post(@RequestBody UserAdd userAdd){
        return userService.add(Mono.just(userAdd));
    }

    @ApiOperation(value="删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id:[0-9]{1,13}}")
    public Mono<User> delete(@PathVariable("id") Long id){
        return userService.remove(Mono.just(id)).switchIfEmpty(Mono.error(new NotFoundException(ResourceEnum.USER)));
    }

}
