package kr.ac.sejong.ds.palette.datecourse.repository;

import kr.ac.sejong.ds.palette.couple.entity.Couple;
import kr.ac.sejong.ds.palette.datecourse.entity.DateCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DateCourseRepository extends JpaRepository<DateCourse, Long> {

    @Query(value = "SELECT dc " +
            "FROM DateCourse dc " +
            "JOIN FETCH dc.dateCourseRestaurants dcr " +
            "JOIN FETCH dcr.restaurant rst " +
            "LEFT JOIN FETCH dcr.review r " +
            "WHERE dc.couple.id = :coupleId " +
            "ORDER BY dc.createdAt DESC")
    List<DateCourse> findAllByCoupleIdWithRestaurantAndReviewOrderByCreatedAtDesc(@Param(value = "coupleId") Long coupleId);
}
