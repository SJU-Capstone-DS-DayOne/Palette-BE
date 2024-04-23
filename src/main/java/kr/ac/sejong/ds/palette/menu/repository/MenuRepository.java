package kr.ac.sejong.ds.palette.menu.repository;

import kr.ac.sejong.ds.palette.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
