package kr.ac.sejong.ds.palette.review.repository;

import kr.ac.sejong.ds.palette.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
