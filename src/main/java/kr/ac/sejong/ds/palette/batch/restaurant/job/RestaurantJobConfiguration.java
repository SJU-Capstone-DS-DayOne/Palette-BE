package kr.ac.sejong.ds.palette.batch.restaurant.job;

import kr.ac.sejong.ds.palette.batch.restaurant.document.MongoRestaurant;
import kr.ac.sejong.ds.palette.common.exception.member.NotFoundMemberException;
import kr.ac.sejong.ds.palette.common.exception.restaurant.NotFoundCategoryException;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import kr.ac.sejong.ds.palette.menu.entity.Menu;
import kr.ac.sejong.ds.palette.menu.repository.MenuRepository;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantCategory;
import kr.ac.sejong.ds.palette.restaurant.repository.CategoryRepository;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantCategoryRepository;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantRepository;
import kr.ac.sejong.ds.palette.review.entity.Review;
import kr.ac.sejong.ds.palette.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Configuration
@RequiredArgsConstructor
public class RestaurantJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;
    private final MongoTemplate mongoTemplate;

    @Bean
    public Job restaurantIntegrationJob() {
        return new JobBuilder("restaurantIntegrationJob", jobRepository)
                .start(cleanDuplicatesStep())
                .next(insertRestaurantStep())
                .build();
    }

    @Bean
    public Step cleanDuplicatesStep() {  // STEP 1
        return new StepBuilder("cleanDuplicatesStep", jobRepository)
                .<MongoRestaurant, Restaurant>chunk(100, platformTransactionManager)
                .reader(mongoRstReader(null))  // district는 itemReader 빈이 Step 실행 시점에 생성되며 @Value를 통해 동적으로 주입됨
                .processor(findDuplicatesProcessor())
                .writer(rstRemover())
                .build();
    }

    @Bean
    public Step insertRestaurantStep() {  // STEP 2
        return new StepBuilder("insertRestaurantStep", jobRepository)
                .<MongoRestaurant, Restaurant>chunk(100, platformTransactionManager)
                .reader(mongoRstReader(null))  // district는 itemReader 빈이 Step 실행 시점에 생성되며 @Value를 통해 동적으로 주입됨
                .processor(mongoRstToRstProcessor())
                .writer(retWriter())
                .build();
    }

    @Bean
    @StepScope
    public MongoPagingItemReader<MongoRestaurant> mongoRstReader(@Value("#{jobParameters['district']}") String district) {  // STEP 1/2 - reader

        return new MongoPagingItemReaderBuilder<MongoRestaurant>()
                .name("mongoRstReader")
                .template(mongoTemplate)
                .targetType(MongoRestaurant.class)
                .collection(district)  // 컬렉션을 district로 설정
                .query(new Query().addCriteria(where("region").is(district))) // district로 필터링 (혹시나 다른 지역의 레스토랑이 섞일 경우 대비)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .pageSize(100)
                .build();
    }

    @Bean
    public ItemProcessor<MongoRestaurant, Restaurant> findDuplicatesProcessor() {  // STEP 1 - processor
        return item -> restaurantRepository.findById(item.getRestaurantId()).orElse(null);  // 중복되지 않는다면 null을 반환하여 writer로 넘기지 않음
    }


    @Bean
    public ItemWriter<Restaurant> rstRemover() {  // STEP 1 - writer
        return chunk -> {
            chunk.getItems().forEach(restaurant -> {
                restaurantCategoryRepository.deleteAllByRestaurantId(restaurant.getId());  // 연관된 레스토랑 카테고리 삭제
                menuRepository.deleteAllByRestaurantId(restaurant.getId());  // 연관된 메뉴 삭제
                reviewRepository.deleteAllByRestaurantId(restaurant.getId());  // 연관된 리뷰 삭제
                restaurantRepository.delete(restaurant);  // 중복된 레스토랑 삭제
            });
        };
    }

    @Bean
    public ItemProcessor<MongoRestaurant, Restaurant> mongoRstToRstProcessor() {  // STEP 2 - processor
        return item -> {
            Restaurant restaurant = item.toRestaurant();  // 레스토랑 엔티티로 변환

            List<RestaurantCategory> restaurantCategoryList = item.getMongoRestaurantCategoryList().stream()  // 레스토랑 카테고리 엔티티로 변환
                    .map(mongoCategory -> mongoCategory.toRestaurantCategory(restaurant, categoryRepository.findById(mongoCategory.getCategoryId())
                            .orElseThrow(NotFoundCategoryException::new)))
                    .toList();
            List<Menu> menuList = item.getMongoMenuList().stream().map(mongoMenu -> mongoMenu.toMenu(restaurant)).toList();  // 메뉴 엔티티로 변환
            List<Review> reviewList = item.getMongoReviewList().stream()  // 리뷰 엔티티로 변환
                    .map(mongoReview -> mongoReview.toReview(restaurant,memberRepository.findById(mongoReview.getMemberId())
                            .orElseThrow(NotFoundMemberException::new)))
                    .toList();

            restaurantCategoryList.stream().forEach(restaurantCategory -> restaurant.getRestaurantCategoryList().add(restaurantCategory));  // 레스토랑에 레스토랑 카테고리 추가 (추후 Writer에서 영속화)
            menuList.stream().forEach(menu -> restaurant.getMenuList().add(menu));  // 레스토랑에 메뉴 추가
            reviewList.stream().forEach(review -> restaurant.getReviewList().add(review));  // 레스토랑에 리뷰 추가

            return restaurant;
        };
    }

    @Bean
    public ItemWriter<Restaurant> retWriter() {  // STEP 3 - writer
        return chunk -> {  // Restaurant 엔티티 및 연관된 엔티티들을 영속화
            chunk.getItems().forEach(restaurant -> {
                restaurantRepository.save(restaurant);
                restaurantCategoryRepository.saveAll(restaurant.getRestaurantCategoryList());
                menuRepository.saveAll(restaurant.getMenuList());
                reviewRepository.saveAll(restaurant.getReviewList());
            });
        };
    }
}