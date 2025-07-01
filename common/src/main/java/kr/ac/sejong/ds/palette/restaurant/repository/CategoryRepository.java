package kr.ac.sejong.ds.palette.restaurant.repository;

import kr.ac.sejong.ds.palette.restaurant.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
