package kr.ac.sejong.ds.palette.restaurant.repository;

import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantCandidate;
import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantSuggestionRepository extends JpaRepository<RestaurantSuggestion, Long> {
    @Query("SELECT rs.restaurantId FROM RestaurantSuggestion rs")
    List<Long> findAllRestaurantId();
}
