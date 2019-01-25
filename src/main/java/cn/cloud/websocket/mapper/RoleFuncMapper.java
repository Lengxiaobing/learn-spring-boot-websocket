package cn.cloud.websocket.mapper;

import cn.cloud.websocket.model.RoleFunc;

public interface RoleFuncMapper {
    /**
     * 根据主键删除role和func的关联
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入role和func的关联
     *
     * @param record
     * @return
     */
    int insert(RoleFunc record);

    /**
     * 选择性插入role和func的关联
     *
     * @param record
     * @return
     */
    int insertSelective(RoleFunc record);

    /**
     * 根据主键查询role和func的关联
     *
     * @param id
     * @return
     */
    RoleFunc selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择性更新role和func的关联
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(RoleFunc record);

    /**
     * 根据主键更新role和func的关联
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(RoleFunc record);
}