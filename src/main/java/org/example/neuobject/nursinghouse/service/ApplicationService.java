package org.example.neuobject.nursinghouse.service;

import org.example.neuobject.nursinghouse.entity.CheckOutApplication;
import org.example.neuobject.nursinghouse.entity.OutingApplication;

import java.util.List;

public interface ApplicationService {

    void saveCheckOutApplication(CheckOutApplication application);

    List<CheckOutApplication> findAllCheckOutApplications();

    CheckOutApplication findCheckOutApplicationById(String applicationId);

    void saveOutingApplication(OutingApplication application);

    List<OutingApplication> findAllOutingApplications();

    OutingApplication findOutingApplicationById(String applicationId);
}
