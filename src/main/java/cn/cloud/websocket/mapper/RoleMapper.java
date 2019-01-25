package cn.cloud.websocket.mapper;

import cn.cloud.websocket.model.Role;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    /**
     * 根据主键删除role
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入role
     *
     * @param record
     * @return
     */
    int insert(Role record);

    /**
     * 选择性插入role
     *
     * @param record
     * @return
     */
    int insertSelective(Role record);

    /**
     * 根据主键查询role
     *
     * @param id
     * @return
     */
    Role selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新role
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Role record);

    /**
     * 根据主键更新role
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(Role record);

    /**
     * 根据rolename查询
     *
     * @param roleName
     * @return
     */
    Role selectByRoleName(@Param("roleName") String roleName);

}