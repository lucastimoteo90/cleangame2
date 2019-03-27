package com.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.domain.Room;
import com.demo.domain.Score;
import com.demo.domain.User;


@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer>{
  	List<Score> findByUserAndRoom(User user, Room room);
    List<Score> findByRoomOrderByScoreDesc(Room room);
  	
}
