package br.inatel.pos.dm111.vfr.api.promotion;

import java.util.List;

public record PromotionRequest(String name,
                               String restaurantName,
                               String restaurantId,
                               float discount,
                               List<String> product) {
}
