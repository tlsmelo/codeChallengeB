package dataProvider.api.datamodels;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class Pet {

    @Builder.Default
    private int id = 11;
    @Builder.Default
    private String name = "Rex";
    private Category category;
    @Builder.Default
    private List<String> photoUrls = Arrays.asList("url1", "url2");
    private List<Tags> tags;
    @Builder.Default
    private String status = "available";

}
