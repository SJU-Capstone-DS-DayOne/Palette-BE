package kr.ac.sejong.ds.palette.review.repository;

import kr.ac.sejong.ds.palette.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r " +
            "FROM Review r " +
            "JOIN FETCH r.member " +
            "WHERE r.restaurant.id = :restaurantId " +
            "ORDER BY r.createdAt DESC")
    Page<Review> findAllWithMemberByRestaurantIdOrderByCreatedAtDesc(@Param("restaurantId") Long restaurantId, Pageable pageable);

    @Query("SELECT r " +
            "FROM Review r " +
            "JOIN FETCH r.member m " +
            "WHERE r.restaurant.id = :restaurantId " +
            "AND r.id IN :reviewIds ORDER BY find_in_set(r.id, :reviewStringIds) LIMIT 30")
    List<Review> findTop30WithMemberByRestaurantIdOrderByIds(@Param("restaurantId") Long restaurantId, @Param("reviewIds") List<Long> reviewIds, @Param("reviewStringIds") String reviewStringIds);

}
