package dataProvider.api.datamodels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Tags {

    @Builder.Default
    private int id = 1;
    @Builder.Default
    private String name = "tag1";
}
