package cn.cloud.websocket.mapper;

import cn.cloud.websocket.model.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper {

    /**
     * 根据主键删除用户角色信息
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入用户角色信息
     *
     * @param record
     * @return
     */
    int insert(UserRole record);

    /**
     * 选择性插入用户角色信息
     *
     * @param record
     * @return
     */
    int insertSelective(UserRole record);

    /**
     * 根据主键查询用户角色信息
     *
     * @param id
     * @return
     */
    UserRole selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新用户角色信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UserRole record);

    /**
     * 根据主键更新用户角色信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(UserRole record);

    /**
     * 通过用户ID和角色ID查询用户角色信息
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return cn.cloud.websocket.model.UserRole
     * @author zx
     * @date 2018/8/18 11:10
     * @since 1.0.0
     */
    UserRole selectByUserIdRoleId(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    /**
     * 通过角色名查询用户角色信息
     *
     * @param roleName 角色名
     * @return cn.cloud.websocket.model.UserRole
     * @author zx
     * @date 2018/8/18 11:10
     * @since 1.0.0
     */
    UserRole selectByRoleName(@Param("roleName") String roleName);

    /**
     * 通过用户ID查询用户角色信息
     *
     * @param userId 用户ID
     * @return cn.cloud.websocket.model.UserRole
     * @author zx
     * @date 2018/8/18 11:10
     * @since 1.0.0
     */
    List<UserRole> selectByUserId(@Param("userId") Integer userId);
}