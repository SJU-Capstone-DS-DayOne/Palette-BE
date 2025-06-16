package kr.ac.sejong.ds.palette.menu.repository;

import kr.ac.sejong.ds.palette.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findAllByRestaurantId(Long restaurantId);

    @Modifying
    @Query("DELETE FROM Menu m " +
            "WHERE m.restaurant.id = :restaurantId")
    void deleteByRestaurantId(@Param("restaurantId") Long restaurantId);
}