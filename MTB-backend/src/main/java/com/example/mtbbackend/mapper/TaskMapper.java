package com.example.mtbbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mtbbackend.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    // MyBatis-Plus自动提供基础CRUD方法，如 selectList, insert, updateById等

    //自定义查询：按日期统计每日新增任务数量
    @Select("SELECT create_time AS date,COUNT(*) AS count FROM task GROUP BY create_time ORDER BY create_time")
    List<Map<String, Object>> countByDate();
}
