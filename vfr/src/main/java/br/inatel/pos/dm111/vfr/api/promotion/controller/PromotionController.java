package br.inatel.pos.dm111.vfr.api.promotion.controller;

import br.inatel.pos.dm111.vfr.api.core.ApiException;
import br.inatel.pos.dm111.vfr.api.core.AppError;
import br.inatel.pos.dm111.vfr.api.promotion.PromotionRequest;
import br.inatel.pos.dm111.vfr.api.promotion.PromotionResponse;
import br.inatel.pos.dm111.vfr.api.promotion.service.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/valefood/promotion")
public class PromotionController {

    private final PromotionRequestValidator validator;
    private final PromotionService service;

    public PromotionController(PromotionRequestValidator validator, PromotionService service) {
        this.validator = validator;
        this.service = service;
    }

    private static final Logger log = LoggerFactory.getLogger(PromotionController.class);

    @GetMapping
    public ResponseEntity<List<PromotionResponse>> getAllPromotions() throws ApiException {
        log.warn("Request Received to list all the Promotions");

        var response = service.searchPromotions();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(value = "/{promotionId}")
    public ResponseEntity<PromotionResponse> getPromotionById(@PathVariable("promotionId") String id)
            throws ApiException {
        log.warn("Request Received to list the Promotion by id: {}", id);

        var response = service.searchPromotion(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<PromotionResponse>> getPromotionByUser(@PathVariable("userId") String id)
            throws ApiException {
        log.warn("Request Received to list the all the Promotion by user id: {}", id);

        var response = service.searchPromotionByUserId(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<PromotionResponse> postPromotion(@RequestBody PromotionRequest request, BindingResult bindingResult) throws ApiException {
        log.warn("Received a request to create a new Promotion");

        validatePromotionRequest(request, bindingResult);
        var response = service.createPromotion(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping(value = "/{promotionId}")
    public ResponseEntity<PromotionResponse> putPromotion(@RequestBody PromotionRequest request,
                                                          @PathVariable("promotionId") String id,
                                                          BindingResult bindingResult) throws ApiException {

        log.warn("Received a request to update a Promotion");
        validatePromotionRequest(request, bindingResult);
        var response = service.updatePromotion(request, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping(value = "/{promotionId}")
    public ResponseEntity<List<PromotionResponse>> deletePromotion(@PathVariable("promotionId") String id) throws ApiException {
        log.warn("Request Received to delete the Promotion by id: {}", id);

        service.removePromotion(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    private void validatePromotionRequest(PromotionRequest request, BindingResult bindingResult) throws ApiException {

        ValidationUtils.invokeValidator(validator, request, bindingResult);

        if(bindingResult.hasErrors()){
            var errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(fe -> new AppError(fe.getCode(), fe.getDefaultMessage()))
                    .toList();
            throw new ApiException(HttpStatus.BAD_REQUEST, errors);
        }
    }
}