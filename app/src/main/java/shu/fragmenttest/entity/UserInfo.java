package shu.fragmenttest.entity;

/**
 * Created by eva on 2017/2/23.
 */

public class UserInfo {
    private String accountName;
    private String email;
    private String nickName;
    private String avatar;
    private String id;

    public UserInfo(){
    }
    public UserInfo(String accountName){
        this.accountName = accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getAccountName() {
        return accountName;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getId() {
        return id;
    }
}
