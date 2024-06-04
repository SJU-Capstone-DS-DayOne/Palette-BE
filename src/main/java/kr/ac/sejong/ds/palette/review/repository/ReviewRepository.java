package kr.ac.sejong.ds.palette.review.repository;

import kr.ac.sejong.ds.palette.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r " +
            "FROM Review r " +
            "JOIN FETCH r.member " +
            "WHERE r.restaurant.id = :restaurantId " +
            "ORDER BY r.createdAt DESC")
    Page<Review> findAllWithMemberByRestaurantIdOrderByCreatedAtDesc(@Param("restaurantId") Long restaurantId, Pageable pageable);
}
