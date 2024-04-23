package kr.ac.sejong.ds.palette.category.repository;

import kr.ac.sejong.ds.palette.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
