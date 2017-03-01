package shu.fragmenttest.entity;

import java.util.Calendar;


/**
 * Created by eva on 2017/1/28.
 */

public class Cloth {
    private String cloth_title;
    private String description;
    private String user_name;
    private String imgUrl;
    private String sdkPath;
    private String uuid;
    private String Tags;
    public Cloth()
    {
        cloth_title="none";
        user_name="admin";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour =calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second  = calendar.get(Calendar.SECOND);
        imgUrl = "http://pica.nipic.com/2007-10-09/200710994020530_2.jpg";
    }
    public Cloth(int id,String title,String user)
    {
        cloth_title = title;
        user_name=user;
        imgUrl = "http://pica.nipic.com/2007-10-09/200710994020530_2.jpg";
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setCloth_title(String cloth_title) {
        this.cloth_title = cloth_title;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setSdkPath(String sdkPath) {
        this.sdkPath = sdkPath;
    }

    public String getDescription() {
        return description;
    }

    public String getTags() {
        return Tags;
    }

    public String getSdkPath() {
        return sdkPath;
    }

    public String getUuid() {
        return uuid;
    }

    public String toString() {
        return user_name;
    }

    public String getCloth_title() {
        return cloth_title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getUser_name() {
        return user_name;
    }

}
