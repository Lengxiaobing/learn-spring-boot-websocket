package cn.cloud.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleFunc {
    private Integer id;

    private Integer roleId;

    private Integer funcId;

}