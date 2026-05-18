import java.io.Serializable;
import java.math.BigDecimal;

/**
         * <p>护理项目是颐养中心提供给老人的可购买、可执行的最小护理服务单元。每个护理项目有独立的定价、执行周期和执行次数，管理员可动态启用/停用。</p>
         *
         * @author li253
         * @date 2026/5/18 18:45
         * @version 1.0
         */

public class NursingProject implements Serializable {
    private Long projectId;
    private String code;
    private String name;
    private BigDecimal price;
    private String status;
    private String execCycle;
    private Integer execTimes;
    private String description;

    NursingProject(){}

    NursingProject(Long projectId, String code, String name, BigDecimal price, String status, String execCycle, Integer execTimes, String description){
        this.projectId = projectId;
        this.code = code;
        this.name = name;
        this.price = price;
        this.status = status;
        this.execCycle = execCycle;
        this.execTimes = execTimes;
        this.description = description;
    }

// 添加
    void addProject(NursingProject project){
       this.projectId = project.projectId;
       this.code = project.code;
       this.name = project.name;
       this.price = project.price;
       this.status = project.status;
       this.execCycle = project.execCycle;
       this.execTimes = project.execTimes;
       this.description = project.description;
       System.out.println("添加护理项目成功！");
    }

// 更新
    void updateProject(NursingProject project){
       this.code = project.code;
       this.name = project.name;
       this.price = project.price;
       this.status = project.status;
       this.execCycle = project.execCycle;
       this.execTimes = project.execTimes;
       this.description = project.description;
       System.out.println("更新护理项目成功！");
    }

// 逻辑删除
    void logicalDelete(NursingProject project){
       project.status = "已删除";
       System.out.println("删除护理项目成功！");
    }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getExecCycle() { return execCycle; }
    public void setExecCycle(String executeCycle) { this.execCycle = executeCycle; }
    public Integer getExecTimes() { return execTimes; }
    public void setExecTimes(Integer executeTimes) { this.execTimes = executeTimes; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
