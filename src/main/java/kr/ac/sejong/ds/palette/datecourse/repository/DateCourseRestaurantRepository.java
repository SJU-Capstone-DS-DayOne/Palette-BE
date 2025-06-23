package kr.ac.sejong.ds.palette.datecourse.repository;

import kr.ac.sejong.ds.palette.datecourse.entity.DateCourseRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DateCourseRestaurantRepository extends JpaRepository<DateCourseRestaurant, Long> {
    @Modifying
    @Query("DELETE FROM DateCourseRestaurant dcr WHERE dcr.restaurant.id = :restaurantId")
    void deleteAllByRestaurantId(@Param("restaurantId") Long restaurantId);

    Optional<DateCourseRestaurant> findByReviewId(Long reviewId);
}
