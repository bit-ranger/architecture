package com.rainyalley.architecture.controller.v1

import com.rainyalley.architecture.po.InfoPo
import io.swagger.annotations.Api
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(value = "v1/info", description = "资讯")
@RestController
@RequestMapping(value = ["v1/info"], produces = [MediaType.APPLICATION_JSON_VALUE])
class InfoController{

    @RequestMapping
    fun post(info: InfoPo) {
        print(info)
    }


}
