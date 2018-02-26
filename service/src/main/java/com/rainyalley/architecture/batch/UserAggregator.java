package com.rainyalley.architecture.batch;

import com.rainyalley.architecture.model.entity.User;
import org.springframework.batch.item.file.transform.LineAggregator;

public class UserAggregator implements LineAggregator<User> {
    @Override
    public String aggregate(User user) {
        if(user == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(user.getId()).append(",");
        sb.append(user.getName()).append(",");
        sb.append(user.getPassword()).append(",");
        return sb.toString();
    }
}
