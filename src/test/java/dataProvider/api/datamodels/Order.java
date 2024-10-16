package dataProvider.api.datamodels;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Order {

    @Builder.Default
    private int id = 1;
    @Builder.Default
    private int petId = 1000;
    private int quantity;
    private String shipDate;
    private String status;
    @Builder.Default
    private boolean complete = true;

}
