package cn.cloud.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRole {

    private Integer id;

    private Integer userId;

    private Integer roleId;

    public UserRole(Integer userId,Integer roleId){
        this.userId = userId;
        this.roleId = roleId;

    }

}