package kr.ac.sejong.ds.palette.restaurant.repository;

import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantCandidateRepository extends JpaRepository<RestaurantCandidate, Long> {
    @Query("SELECT rc.restaurantId FROM RestaurantCandidate rc")
    List<Long> findAllRestaurantId();
}
