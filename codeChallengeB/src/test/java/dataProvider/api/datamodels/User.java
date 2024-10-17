package dataProvider.api.datamodels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    @Builder.Default
    private int id = 1;
    private String username;
    @Builder.Default
    private String firstName = "Test";
    @Builder.Default
    private String lastName = "ABC123";
    @Builder.Default
    private String email = "test@test.com";
    @Builder.Default
    private String password = "1234";
    @Builder.Default
    private String phone = "551312555679";
    @Builder.Default
    private int userStatus = 1;

}
