package cn.cloud.websocket.mapper;

import cn.cloud.websocket.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    /**
     * 根据主键删除用户
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入用户
     *
     * @param user
     * @return
     */
    int insert(User user);

    /**
     * 选择性插入用户
     *
     * @param user
     * @return
     */
    int insertSelective(User user);

    /**
     * 根据主键查询用户
     *
     * @param id
     * @return
     */
    User selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新用户
     *
     * @param user
     * @return
     */
    int updateByPrimaryKeySelective(User user);

    /**
     * 根据主键更新用户
     *
     * @param user
     * @return
     */
    int updateByPrimaryKey(User user);

    /**
     * 根据用户名查询
     *
     * @param username
     * @return
     */
    User selectByUsername(@Param("username") String username);

}