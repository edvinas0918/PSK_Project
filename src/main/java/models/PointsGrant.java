package models;

import java.io.Serializable;

/**
 * Created by Dziugas on 4/25/2016.
 */
public class PointsGrant  implements Serializable {
    private Integer memberID;
    private Integer points;
    private String description;

    public PointsGrant(){

    }
    
    public Integer getMemberID() {
        return memberID;
    }

    public void setMemberID(Integer memberID) {
        this.memberID = memberID;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PointsGrant(Integer memberID, Integer points, String description) {

        this.memberID = memberID;
        this.points = points;
        this.description = description;
    }
}
