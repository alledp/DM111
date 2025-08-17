package br.inatel.pos.dm111.vfr.api.promotion.controller;

import br.inatel.pos.dm111.vfr.api.promotion.PromotionRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PromotionRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PromotionRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Name is required!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "restaurantName", "restaurantName.empty", "Restaurant NAME is required!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "restaurantId", "restaurantId.empty", "Restaurant ID is required!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "discount", "discount.empty", "Discount is required!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "product", "product.empty", "Product is required!");
    }
}
