package com.itschool.hotelResvMgt.repositories;

import com.itschool.hotelResvMgt.models.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    public List<Room> findByAvailability(boolean availability);
}