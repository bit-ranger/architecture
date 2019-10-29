package com.rainyalley.architecture.po

import io.swagger.annotations.ApiModelProperty


class UserPo {

    @ApiModelProperty(value = "姓名", required = true)
    var name: String? = null
        set(name) {
            field = this.name
        }

    @ApiModelProperty(value = "密码", required = true)
    var password: String? = null
        set(password) {
            field = this.password
        }

    @ApiModelProperty(value = "邮箱", required = false)
    var email: String? = null
        set(email) {
            field = this.email
        }

    @ApiModelProperty(value = "年龄", required = false)
    var age: Int = 0
        set(age) {
            field = this.age
        }
}
