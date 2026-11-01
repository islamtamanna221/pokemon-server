package org.example.repository;

import org.example.entity.BattleTurn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleTurnRepository extends JpaRepository<BattleTurn, Long> {
    // প্রতিটি চাল বা টার্ন হিস্ট্রি এখানে সেভ হবে
}