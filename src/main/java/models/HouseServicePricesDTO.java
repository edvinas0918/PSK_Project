package models;

import java.util.List;

/**
 * Created by Mindaugas on 29/05/16.
 */
public class HouseServicePricesDTO {

    private List<HouseServicePriceDTO> houseServicePriceDTOList;
    private Integer houseID;

    public List<HouseServicePriceDTO> getHouseServicePriceDTOList() {
        return houseServicePriceDTOList;
    }

    public void setHouseServicePriceDTOList(List<HouseServicePriceDTO> houseServicePriceDTOList) {
        this.houseServicePriceDTOList = houseServicePriceDTOList;
    }

    public Integer getHouseID() {
        return houseID;
    }

    public void setHouseID(Integer houseID) {
        this.houseID = houseID;
    }

}
