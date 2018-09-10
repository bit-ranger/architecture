package com.rainyalley.architecture.batch.demo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.LineMapper;

public class UserLineMapper implements LineMapper<User> {

    @Override
    public User mapLine(String s, int i) throws Exception {
        if(StringUtils.isBlank(s)){
            return null;
        }
        String[] lineDataList = StringUtils.split(s, ",");
        User user = new User();
        user.setId(Integer.valueOf(lineDataList[0]));
        user.setName(lineDataList[1]);
        user.setPassword(lineDataList[2]);
        return user;
    }
}
