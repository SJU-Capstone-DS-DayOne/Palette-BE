package kr.ac.sejong.ds.palette.restaurant.repository;

import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {
}
