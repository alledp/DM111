package br.inatel.pos.dm111.vfr.api.promotion.service;

import br.inatel.pos.dm111.vfr.api.core.AppErrorCode;
import br.inatel.pos.dm111.vfr.api.promotion.PromotionRequest;
import br.inatel.pos.dm111.vfr.api.promotion.PromotionResponse;
import br.inatel.pos.dm111.vfr.api.restaurant.ProductResponse;
import br.inatel.pos.dm111.vfr.persistence.promotion.Promotion;
import br.inatel.pos.dm111.vfr.persistence.promotion.PromotionRepository;
import br.inatel.pos.dm111.vfr.api.core.ApiException;
import br.inatel.pos.dm111.vfr.persistence.restaurant.Product;
import br.inatel.pos.dm111.vfr.persistence.restaurant.Restaurant;
import br.inatel.pos.dm111.vfr.persistence.restaurant.RestaurantRepository;
import br.inatel.pos.dm111.vfr.persistence.user.User;
import br.inatel.pos.dm111.vfr.persistence.user.UserRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class PromotionService {

    private static final Logger log = LoggerFactory.getLogger(PromotionService.class);

    private final PromotionRepository promotionRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public PromotionService(PromotionRepository repository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.promotionRepository = repository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public PromotionResponse createPromotion(PromotionRequest request) throws ApiException {

        validateRestaurant(request);

        var promotion = buildPromotion(request);
        promotionRepository.save(promotion);
        log.info("Promotion was successfully created. Id: {}", promotion.id());

        return buildPromotionResponse(promotion);

    }

    public List<PromotionResponse> searchPromotions() throws ApiException {
        return retrievePromotions().stream()
                .map(this::buildPromotionResponse)
                .toList();
    }

    public PromotionResponse searchPromotion(String id) throws ApiException{
        return retrievePromotionById(id)
                .map(this::buildPromotionResponse)
                .orElseThrow(() -> {
                    log.warn("Promotion was not found. Id: {}", id);
                    return new ApiException(AppErrorCode.RESTAURANT_NOT_FOUND);
                });
    }

    public List<PromotionResponse> searchPromotionByUserId(String id) throws ApiException {

        var user = validateUser(id);
        var allPromotion = searchPromotions();
        List<PromotionResponse> promotionsUser = new ArrayList<>();
        
        for(PromotionResponse promotion: allPromotion){
            
            var restaurant = retrieveRestaurantById(promotion.restaurantId()).get();

            var userCategory = user.category().stream().toList();
            
            for(String category: userCategory){
                
                var restaurantProduct = restaurant.products()
                        .stream()
                        .map(this::buildProductResponse)
                        .filter(p -> p.category().equals(category));

                if(!restaurantProduct.toList().isEmpty()) {
                    if (!promotionsUser.contains(promotion)) {
                        promotionsUser.add(promotion);
                    }
                }
            }
        }

        return promotionsUser;
    }

    public PromotionResponse updatePromotion(PromotionRequest request, String id) throws ApiException {

        // check if promotion exist by using the ID
        var promotionOpt = retrievePromotionById(id);

        if(promotionOpt.isEmpty()) {
            log.warn("Promotion was not found. Id: {}", id);
            throw  new ApiException(AppErrorCode.PROMOTION_NOT_FOUND);
        } else {
            var promotion = promotionOpt.get();

            if(request.restaurantName() != null && !promotion.name().equals(request.name())){
                //validate Promotion unique by restaurant
                var promotionNameOpt = promotionRepository.getByName(request.name());
                if(promotionNameOpt.isPresent()) {
                    log.warn("Promotion already exist.");
                    throw new ApiException(AppErrorCode.CONFLICTED_PROMOTION_NAME);
                }
            }
        }

        var promotion = buildPromotion(request, id);
        promotionRepository.save(promotion);

        return buildPromotionResponse(promotion);
    }

    public void removePromotion(String id) throws ApiException {
        // check if promotion exist by using the ID
        var promotionOpt = retrievePromotionById(id);

        if (promotionOpt.isPresent()) {
            try {
                promotionRepository.delete(id);
            } catch (ExecutionException | InterruptedException e) {
                log.error("Failed to delete a Promotion from by id {}.", id, e);
                throw new ApiException(AppErrorCode.INTERNAL_DATABASE_COMMUNICATION_ERROR);
            }
        } else {
            log.info("The Provided Promotion Id was not found. id: {}", id);
        }

    }

    private void validateRestaurant(PromotionRequest request) throws ApiException {

        var restaurantOpt = retrieveRestaurantById(request.restaurantId());

        if (restaurantOpt.isEmpty()) {
            log.warn("Restaurant was not found. Id: {}", request.restaurantId());
            throw new ApiException(AppErrorCode.USER_NOT_FOUND);
        } else {
            var restaurant = restaurantOpt.get();

            var promotionsProducts = request.product().stream().toList();

            for (String promotionProduct : promotionsProducts){

                var restaurantProduct = restaurant.products()
                        .stream()
                        .map(this::buildProductResponse)
                        .filter(p -> p.name().equals(promotionProduct));

                if(restaurantProduct.toList().isEmpty()){
                    throw new ApiException(AppErrorCode.PRODUCT_NOT_FOUND);
                }
            }
        }
    }

    private User validateUser(String userId) throws ApiException {
        var userOpt = retrieveUserById(userId);

        if (userOpt.isEmpty()) {
            log.warn("User was not found. Id: {}", userId);
            throw new ApiException(AppErrorCode.USER_NOT_FOUND);
        } else {
            return userOpt.get();
        }
    }

    private Promotion buildPromotion( PromotionRequest request){
        var promotionId = UUID.randomUUID().toString();
        return buildPromotion(request, promotionId);
    }

    private Promotion buildPromotion( PromotionRequest request, String id){
        return new Promotion(id,
                request.name(),
                request.restaurantName(),
                request.restaurantId(),
                request.discount(),
                request.product());
    }

    private List<Promotion> retrievePromotions() throws ApiException {
        try {
            return promotionRepository.getAll();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failed to read all restaurants.", e);
            throw new ApiException(AppErrorCode.INTERNAL_DATABASE_COMMUNICATION_ERROR);
        }
    }

    private Optional<Promotion> retrievePromotionById(String id) throws ApiException {
        try {
            return promotionRepository.getById(id);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failed to read a restaurant from DB by id {}.", id, e);
            throw new ApiException(AppErrorCode.INTERNAL_DATABASE_COMMUNICATION_ERROR);
        }
    }

    private Optional<Promotion> retrievePromotionByUserId(String id) throws ApiException {
        try {
            return promotionRepository.getById(id);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failed to read a restaurant from DB by id {}.", id, e);
            throw new ApiException(AppErrorCode.INTERNAL_DATABASE_COMMUNICATION_ERROR);
        }
    }

    private PromotionResponse buildPromotionResponse(Promotion promotion){
        return new PromotionResponse(promotion.id(),
                promotion.name(),
                promotion.restaurantName(),
                promotion.restaurantId(),
                promotion.discount(),
                promotion.product());
    }

    private ProductResponse buildProductResponse(Product product) {
        return new ProductResponse(product.id(),
                product.name(),
                product.description(),
                product.category(),
                product.price());
    }

    private Optional<Restaurant> retrieveRestaurantById(String id) throws ApiException {
        try {
            return restaurantRepository.getById(id);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failed to read a restaurant by id {}.", id, e);
            throw new ApiException(AppErrorCode.INTERNAL_DATABASE_COMMUNICATION_ERROR);
        }
    }

    private Optional<User> retrieveUserById(String id) throws ApiException {
        try {
            return userRepository.getById(id);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failed to read an user from DB by id {}.", id, e);
            throw new ApiException(AppErrorCode.INTERNAL_DATABASE_COMMUNICATION_ERROR);
        }
    }

}