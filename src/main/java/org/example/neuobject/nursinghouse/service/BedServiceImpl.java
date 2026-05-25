package org.example.neuobject.nursinghouse.service;

import org.example.neuobject.nursinghouse.entity.Bed;
import org.example.neuobject.nursinghouse.entity.BedUsageHistory;
import org.example.neuobject.nursinghouse.entity.Room;
import org.example.neuobject.nursinghouse.mapper.BedMapper;
import org.example.neuobject.nursinghouse.mapper.BedUsageHistoryMapper;
import org.example.neuobject.nursinghouse.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BedServiceImpl implements BedService {

    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private BedMapper bedMapper;
    @Autowired
    private BedUsageHistoryMapper historyMapper;

    @Override
    public List<Room> findAllRooms() {
        return roomMapper.selectList(null);
    }

    @Override
    public Room findRoomByNumber(String roomNumber) {
        return roomMapper.selectByRoomNumber(roomNumber);
    }

    @Override
    public void updateBedStatus(Long bedId, String status) {
        Bed bed = bedMapper.selectById(bedId);
        if (bed != null) {
            bed.setStatus(status);
            bedMapper.updateById(bed);
        }
    }

    @Override
    public void saveBedUsageHistory(BedUsageHistory history) {
        historyMapper.insert(history);
    }

    @Override
    public List<BedUsageHistory> findAllBedUsageHistories() {
        return historyMapper.selectList(null);
    }
}
