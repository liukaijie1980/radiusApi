package com.example.radiusapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.ToString;

@ToString
public class Realm {
    @TableId(type = IdType.ASSIGN_UUID )
    private String id;
    private String node_pid;
    private String realm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNode_pid() {
        return node_pid;
    }

    public void setNode_pid(String node_pid) {
        this.node_pid = node_pid;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
}
