package org.example.neuobject.nursinghouse.service;

import org.example.neuobject.nursinghouse.entity.BedUsageHistory;
import org.example.neuobject.nursinghouse.entity.Room;

import java.util.List;

public interface BedService {

    List<Room> findAllRooms();

    Room findRoomByNumber(String roomNumber);

    void updateBedStatus(Long bedId, String status);

    void saveBedUsageHistory(BedUsageHistory history);

    List<BedUsageHistory> findAllBedUsageHistories();
}
