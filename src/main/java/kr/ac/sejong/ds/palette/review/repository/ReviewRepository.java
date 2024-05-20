package kr.ac.sejong.ds.palette.review.repository;

import kr.ac.sejong.ds.palette.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from review r join fetch r.member where r.restaurant.id = :restaurantId")
    List<Review> findAllWithMemberByRestaurantId(Long restaurantId);
}
