package kr.ac.sejong.ds.palette.admin.restaurant.service;

import kr.ac.sejong.ds.palette.admin.restaurant.dto.request.MainPageRestaurantUpdateRequest;
import kr.ac.sejong.ds.palette.admin.restaurant.dto.request.RestaurantCreateRequest;
import kr.ac.sejong.ds.palette.admin.restaurant.dto.request.RestaurantUpdateRequest;
import kr.ac.sejong.ds.palette.common.exception.restaurant.DuplicateRestaurantException;
import kr.ac.sejong.ds.palette.common.exception.restaurant.NotFoundCategoryException;
import kr.ac.sejong.ds.palette.common.exception.restaurant.NotFoundRestaurantException;
import kr.ac.sejong.ds.palette.datecourse.repository.DateCourseRestaurantRepository;
import kr.ac.sejong.ds.palette.menu.entity.Menu;
import kr.ac.sejong.ds.palette.menu.repository.MenuRepository;
import kr.ac.sejong.ds.palette.restaurant.entity.Category;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantCategory;
import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantSuggestion;
import kr.ac.sejong.ds.palette.restaurant.repository.CategoryRepository;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantCategoryRepository;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantRepository;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantSuggestionRepository;
import kr.ac.sejong.ds.palette.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;
    private final RestaurantSuggestionRepository restaurantSuggestionRepository;
    private final ReviewRepository reviewRepository;
    private final DateCourseRestaurantRepository dateCourseRestaurantRepository;

    @Transactional
    public void createRestaurant(RestaurantCreateRequest request) {

        // Restaurant (id는 크롤링 서버에서 관리. 따라서 함께 전달되며, 해당 id로 레스토랑 생성)
        if (restaurantRepository.findById(request.id()).isPresent())  // id 중복 체크
            throw new DuplicateRestaurantException();

        Restaurant restaurant = request.toEntity();
        restaurantRepository.save(restaurant);

        // RestaurantCategory
        List<Category> categoryList = categoryRepository.findAllById(request.categoryIdList());

        if (categoryList.size() != request.categoryIdList().size())  // 카테고리 존재 여부 체크
            throw new NotFoundCategoryException();


        List<RestaurantCategory> restaurantCategoryList = categoryList.stream()
                .map(category -> new RestaurantCategory(restaurant, category))
                .toList();
        restaurantCategoryRepository.saveAll(restaurantCategoryList);

        // Menu
        List<Menu> menuList = request.menuCreateRequestList().stream()
                .map(menuCreateRequest -> menuCreateRequest.toEntity(restaurant))
                .toList();
        menuRepository.saveAll(menuList);
    }

    @Transactional
    public void updateRestaurant(RestaurantUpdateRequest request) {

        // Restaurant 업데이트
        Restaurant restaurant = restaurantRepository.findById(request.id())
                .orElseThrow(NotFoundRestaurantException::new);

        restaurant.update(
                request.name(),
                request.restaurantType(),
                request.summary(),
                request.district(),
                request.address(),
                request.lat(),
                request.lng(),
                request.distFromStation(),
                request.openingHours(),
                request.phone(),
                request.reviewCount()
        );

        // RestaurantCategory 업데이트
        List<Category> categoryList = categoryRepository.findAllById(request.categoryIdList());

        if (categoryList.size() != request.categoryIdList().size())  // 카테고리 존재 여부 체크
            throw new NotFoundCategoryException();

        List<RestaurantCategory> restaurantCategoryList = categoryList.stream()
                .map(category -> new RestaurantCategory(restaurant, category))
                .toList();
        restaurantCategoryRepository.deleteAllByRestaurantId(restaurant.getId());  // 기존 RestaurantCategory 삭제
        restaurantCategoryRepository.saveAll(restaurantCategoryList);

        // Menu 업데이트
        List<Menu> menuList = request.menuCreateRequestList().stream()
                .map(menuCreateRequest -> menuCreateRequest.toEntity(restaurant))
                .toList();
        menuRepository.deleteAllByRestaurantId(restaurant.getId());  // 기존 Menu 삭제
        menuRepository.saveAll(menuList);
    }

    @Transactional
    public void deleteRestaurant(Long restaurantId) {

        if (!restaurantRepository.existsById(restaurantId))
            throw new NotFoundRestaurantException();

        restaurantCategoryRepository.deleteAllByRestaurantId(restaurantId);  // 레스토랑 카테고리 삭제
        menuRepository.deleteAllByRestaurantId(restaurantId);  // 메뉴 삭제
        reviewRepository.deleteAllByRestaurantId(restaurantId);  // 리뷰 삭제
        dateCourseRestaurantRepository.deleteAllByRestaurantId(restaurantId);  // 데이트 코스 레스토랑 삭제
        restaurantRepository.deleteById(restaurantId);  // 레스토랑 삭제
    }

    @Transactional
    public void updateMainPageRestaurants(MainPageRestaurantUpdateRequest request) {

        if (restaurantRepository.countByIdIn(request.restaurantIdList()) != request.restaurantIdList().size())  // 레스토랑 존재 여부 체크
            throw new NotFoundRestaurantException();

        List<RestaurantSuggestion> restaurantSuggestionList = request.restaurantIdList().stream()
                .map(RestaurantSuggestion::new)
                .toList();

        restaurantSuggestionRepository.deleteAll(); // 기존 메인 페이지 레스토랑 초기화
        restaurantSuggestionRepository.saveAll(restaurantSuggestionList);
    }
}
