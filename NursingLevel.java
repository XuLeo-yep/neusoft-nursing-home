import java.io.Serializable;

/**
         * <p>护理级别是颐养中心为老人提供的不同服务等级，每个级别包含一组护理项目（如一级护理包含翻身、喂药、血压监测等）。通过为老人分配护理级别，可以批量设置其享有对应的护理服务套餐。</p>
         *
         * @author li253
         * @date 2026/5/18 18:57
         * @version 1.0
         */

public class NursingLevel implements Serializable {
    private Long levelId;
    private String name;
    private String status;

    NursingLevel(){}

    NursingLevel(Long levelId, String name, String status){
        this.levelId = levelId;
        this.name = name;
        this.status = status;
    }

    void addLevel(NursingLevel level){
       this.levelId = level.levelId;
       this.name = level.name;
       this.status = level.status;
       System.out.println("添加护理级别成功！");
    }

    void updateLevel(NursingLevel level){
        this.name = level.name;
        this.status = level.status;
        System.out.println("更新护理级别成功！");
    }

    public Long getLevelId() { return levelId; }
    public void setLevelId(Long levelId) { this.levelId = levelId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
