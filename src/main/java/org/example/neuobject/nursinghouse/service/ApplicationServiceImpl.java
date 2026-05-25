package org.example.neuobject.nursinghouse.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.neuobject.nursinghouse.entity.CheckOutApplication;
import org.example.neuobject.nursinghouse.entity.OutingApplication;
import org.example.neuobject.nursinghouse.mapper.CheckOutApplicationMapper;
import org.example.neuobject.nursinghouse.mapper.OutingApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private CheckOutApplicationMapper checkOutMapper;
    @Autowired
    private OutingApplicationMapper outingMapper;

    @Override
    public void saveCheckOutApplication(CheckOutApplication app) {
        checkOutMapper.insert(app);
    }

    @Override
    public List<CheckOutApplication> findAllCheckOutApplications() {
        return checkOutMapper.selectList(null);
    }

    @Override
    public CheckOutApplication findCheckOutApplicationById(String applicationId) {
        return checkOutMapper.selectOne(
            new LambdaQueryWrapper<CheckOutApplication>()
                .eq(CheckOutApplication::getApplicationId, applicationId)
        );
    }

    @Override
    public void saveOutingApplication(OutingApplication app) {
        outingMapper.insert(app);
    }

    @Override
    public List<OutingApplication> findAllOutingApplications() {
        return outingMapper.selectList(null);
    }

    @Override
    public OutingApplication findOutingApplicationById(String applicationId) {
        return outingMapper.selectOne(
            new LambdaQueryWrapper<OutingApplication>()
                .eq(OutingApplication::getApplicationId, applicationId)
        );
    }
}
