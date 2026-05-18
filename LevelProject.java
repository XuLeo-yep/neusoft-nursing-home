import java.io.Serializable;

/**
         * <p>LevelProject 是连接 护理级别（NursingLevel） 与 护理项目（NursingProject） 的关联实体，用于表示某个护理级别包含哪些护理项目。由于一个级别可以包含多个项目，一个项目也可以被多个级别引用，因此引入此中间实体实现多对多关系。</p>
         *
         * @author li253
         * @date 2026/5/18 18:56
         * @version 1.0
         */

public class LevelProject implements Serializable {
    private Long id;
    private Long levelId;
    private Long projectId;

    LevelProject(){}

    LevelProject(Long id, Long levelId, Long projectId){
        this.id = id;
        this.levelId = levelId;
        this.projectId = projectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

}