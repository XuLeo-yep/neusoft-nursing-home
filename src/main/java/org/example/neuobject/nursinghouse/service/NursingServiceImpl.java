package org.example.neuobject.nursinghouse.service;

import org.example.neuobject.nursinghouse.entity.*;
import org.example.neuobject.nursinghouse.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NursingServiceImpl implements NursingService {

    @Autowired
    private NursingLevelMapper levelMapper;
    @Autowired
    private NursingProjectMapper projectMapper;
    @Autowired
    private LevelProjectMapper lpMapper;
    @Autowired
    private CustomerNursingProjectMapper cnpMapper;
    @Autowired
    private NursingRecordMapper recordMapper;

    // ========== 护理级别 ==========
    @Override
    public List<NursingLevel> findAllNursingLevels() {
        return levelMapper.selectList(null);
    }

    @Override
    public NursingLevel findNursingLevelById(Long levelId) {
        return levelMapper.selectById(levelId);
    }

    @Override
    public void saveNursingLevel(NursingLevel level) {
        levelMapper.insert(level);
    }

    @Override
    public void updateNursingLevelStatus(Long levelId, String status) {
        NursingLevel level = levelMapper.selectById(levelId);
        if (level != null) {
            level.setStatus(status);
            levelMapper.updateById(level);
        }
    }

    // ========== 护理项目 ==========
    @Override
    public List<NursingProject> findAllNursingProjects() {
        return projectMapper.selectList(null);
    }

    @Override
    public NursingProject findNursingProjectById(Long projectId) {
        return projectMapper.selectById(projectId);
    }

    @Override
    public void saveNursingProject(NursingProject project) {
        projectMapper.insert(project);
    }

    @Override
    public void deleteNursingProject(Long projectId) {
        projectMapper.deleteById(projectId);
    }

    // ========== 级别-项目关联 ==========
    @Override
    public List<LevelProject> findLevelProjectsByLevelId(Long levelId) {
        return lpMapper.selectByLevelId(levelId);
    }

    @Override
    public void addLevelProject(LevelProject lp) {
        lpMapper.insert(lp);
    }

    @Override
    public void removeLevelProject(Long levelId, Long projectId) {
        lpMapper.deleteByLevelIdAndProjectId(levelId, projectId);
    }

    // ========== 客户护理项目 ==========
    @Override
    public List<CustomerNursingProject> findCustomerNursingProjectsByCustomerId(Long customerId) {
        return cnpMapper.selectByCustomerId(customerId);
    }

    @Override
    public void saveCustomerNursingProject(CustomerNursingProject cnp) {
        cnpMapper.insert(cnp);
    }

    // ========== 护理记录 ==========
    @Override
    public List<NursingRecord> findNursingRecordsByCustomerId(Long customerId) {
        return recordMapper.selectByCustomerId(customerId);
    }

    @Override
    public void saveNursingRecord(NursingRecord record) {
        recordMapper.insert(record);
    }

    @Override
    public void deleteNursingRecord(Long recordId) {
        recordMapper.deleteById(recordId);
    }
}
