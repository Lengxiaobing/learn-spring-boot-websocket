package cn.cloud.websocket.mapper;

import cn.cloud.websocket.model.Func;

public interface FuncMapper {
    /**
     * 根据主键删除func
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入func
     *
     * @param record
     * @return
     */
    int insert(Func record);

    /**
     * 选择性插入func
     *
     * @param record
     * @return
     */
    int insertSelective(Func record);

    /**
     * 根据主键查询func
     *
     * @param id
     * @return
     */
    Func selectByPrimaryKey(Integer id);

    /**
     * 根据主键选择更新func
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Func record);

    /**
     * 根据主键更新func
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(Func record);
}