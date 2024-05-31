package kr.ac.sejong.ds.palette.restaurant.repository;

import kr.ac.sejong.ds.palette.restaurant.entity.Category;
import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {
    @Query("SELECT rc.category FROM RestaurantCategory rc WHERE rc.restaurant.id = :restaurantId")
    List<Category> findAllCategoryByRestaurantId(@Param("restaurantId") Long restaurantId);
}
