package shu.fragmenttest.entity;

/**
 * Created by eva on 2017/2/25.
 */

public class Tags {
    private String tagsText = "+、棉服、毛衣、大衣、马甲、皮衣、衬衫、T恤、夹克、法兰绒、卫衣、西服、风衣";
    private String[] tags = tagsText.split("、");
    public Tags(){

    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setTagsText(String tagsText) {
        this.tagsText = tagsText;
    }

    public String[] getTags() {
        return tags;
    }

    public String getTagsText() {
        return tagsText;
    }
}
