package dataProvider.api.datamodels;

import lombok.Data;

@Data
public class Inventory {

    private int approved;
    private int placed;
    private int delivered;

}
