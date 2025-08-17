package br.inatel.pos.dm111.vfr.api.promotion;

import java.util.List;

public record PromotionResponse(String id,
                                String name,
                                String restaurantName,
                                String restaurantId,
                                float discount,
                                List<String> product) {
}