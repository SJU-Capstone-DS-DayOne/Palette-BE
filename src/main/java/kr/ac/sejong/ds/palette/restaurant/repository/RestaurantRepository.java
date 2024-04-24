package kr.ac.sejong.ds.palette.restaurant.repository;

import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
