package org.example.neuobject.nursinghouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.neuobject.nursinghouse.entity.LevelProject;

import java.util.List;

@Mapper
public interface LevelProjectMapper extends BaseMapper<LevelProject> {

    @Select("SELECT * FROM level_project WHERE level_id = #{levelId}")
    List<LevelProject> selectByLevelId(Long levelId);

    @Select("DELETE FROM level_project WHERE level_id = #{levelId} AND project_id = #{projectId}")
    void deleteByLevelIdAndProjectId(Long levelId, Long projectId);
}
