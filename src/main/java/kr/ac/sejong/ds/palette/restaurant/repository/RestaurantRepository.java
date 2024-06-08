package kr.ac.sejong.ds.palette.restaurant.repository;

import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query(value = "SELECT r FROM Restaurant r " +
            "LEFT JOIN FETCH r.restaurantCategoryList rc " +
            "LEFT JOIN FETCH rc.category c " +
            "LEFT JOIN FETCH r.menuList m " +
            "WHERE r.id IN :restaurantIds ORDER BY find_in_set(r.id, :restaurantStringIds)")
    List<Restaurant> findAllByIdOrderByIdsWithMenuAndCategory(@Param("restaurantIds") List<Long> restaurantIds, @Param("restaurantStringIds") String restaurantStringIds);

    @Query(value = "SELECT r FROM Restaurant r " +
            "LEFT JOIN FETCH r.restaurantCategoryList rc " +
            "LEFT JOIN FETCH rc.category c " +
            "LEFT JOIN FETCH r.menuList m " +
            "WHERE r.id IN :restaurantIds")
    List<Restaurant> findAllByIdInWithMenuAndCategory(@Param("restaurantIds") List<Long> restaurantIds);
}
