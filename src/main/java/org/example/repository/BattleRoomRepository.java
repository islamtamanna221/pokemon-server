package org.example.repository;

import org.example.entity.BattleRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleRoomRepository extends JpaRepository<BattleRoom, Long> {
    // প্রয়োজন হলে এখানে কাস্টম কুয়েরি লিখতে পারেন
}