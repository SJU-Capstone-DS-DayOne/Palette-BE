package kr.ac.sejong.ds.palette.review.repository;

import kr.ac.sejong.ds.palette.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r join fetch r.member where r.restaurant.id = :restaurantId")
    List<Review> findAllWithMemberByRestaurantId(@Param("restaurantId") Long restaurantId);
}
