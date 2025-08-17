package br.inatel.pos.dm111.vfr.persistence.promotion;


import java.util.List;

public record Promotion(String id,
                        String name,
                        String restaurantName,
                        String restaurantId,
                        float discount,
                        List<String> product) {
}
