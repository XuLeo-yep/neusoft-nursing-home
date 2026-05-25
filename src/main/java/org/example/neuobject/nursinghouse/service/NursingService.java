package org.example.neuobject.nursinghouse.service;

import org.example.neuobject.nursinghouse.entity.*;

import java.util.List;

public interface NursingService {

    // 护理级别
    List<NursingLevel> findAllNursingLevels();
    NursingLevel findNursingLevelById(Long levelId);
    void saveNursingLevel(NursingLevel level);
    void updateNursingLevelStatus(Long levelId, String status);

    // 护理项目
    List<NursingProject> findAllNursingProjects();
    NursingProject findNursingProjectById(Long projectId);
    void saveNursingProject(NursingProject project);
    void deleteNursingProject(Long projectId);

    // 级别-项目关联
    List<LevelProject> findLevelProjectsByLevelId(Long levelId);
    void addLevelProject(LevelProject lp);
    void removeLevelProject(Long levelId, Long projectId);

    // 客户护理项目
    List<CustomerNursingProject> findCustomerNursingProjectsByCustomerId(Long customerId);
    void saveCustomerNursingProject(CustomerNursingProject cnp);

    // 护理记录
    List<NursingRecord> findNursingRecordsByCustomerId(Long customerId);
    void saveNursingRecord(NursingRecord record);
    void deleteNursingRecord(Long recordId);
}
