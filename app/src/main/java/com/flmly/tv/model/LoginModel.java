package com.flmly.tv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("birth_year")
    @Expose
    private String birthYear;
    @SerializedName("is_episode_review_message_seen")
    @Expose
    private Boolean isEpisodeReviewMessageSeen;
    @SerializedName("is_tutorial_page_seen")
    @Expose
    private Boolean isTutorialPageSeen;
    @SerializedName("is_tutorial_scene_page_seen")
    @Expose
    private Boolean isTutorialScenePageSeen;
    @SerializedName("is_tutorial_video_seen")
    @Expose
    private Boolean isTutorialVideoSeen;
    @SerializedName("is_mobile_tutorial_video_seen")
    @Expose
    private Boolean isMobileTutorialVideoSeen;
    @SerializedName("is_scene_iphone_alert_seen")
    @Expose
    private Boolean isSceneIphoneAlertSeen;
    @SerializedName("email_address")
    @Expose
    private String emailAddress;
    @SerializedName("f_name")
    @Expose
    private String fName;
    @SerializedName("l_name")
    @Expose
    private String lName;
    @SerializedName("otp_verified")
    @Expose
    private Boolean otpVerified;
    @SerializedName("thumb_image")
    @Expose
    private String thumbImage;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("birth_month")
    @Expose
    private String birthMonth;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("x_auth_token")
    @Expose
    private String xAuthToken;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public Boolean getIsEpisodeReviewMessageSeen() {
        return isEpisodeReviewMessageSeen;
    }

    public void setIsEpisodeReviewMessageSeen(Boolean isEpisodeReviewMessageSeen) {
        this.isEpisodeReviewMessageSeen = isEpisodeReviewMessageSeen;
    }

    public Boolean getIsTutorialPageSeen() {
        return isTutorialPageSeen;
    }

    public void setIsTutorialPageSeen(Boolean isTutorialPageSeen) {
        this.isTutorialPageSeen = isTutorialPageSeen;
    }

    public Boolean getIsTutorialScenePageSeen() {
        return isTutorialScenePageSeen;
    }

    public void setIsTutorialScenePageSeen(Boolean isTutorialScenePageSeen) {
        this.isTutorialScenePageSeen = isTutorialScenePageSeen;
    }

    public Boolean getIsTutorialVideoSeen() {
        return isTutorialVideoSeen;
    }

    public void setIsTutorialVideoSeen(Boolean isTutorialVideoSeen) {
        this.isTutorialVideoSeen = isTutorialVideoSeen;
    }

    public Boolean getIsMobileTutorialVideoSeen() {
        return isMobileTutorialVideoSeen;
    }

    public void setIsMobileTutorialVideoSeen(Boolean isMobileTutorialVideoSeen) {
        this.isMobileTutorialVideoSeen = isMobileTutorialVideoSeen;
    }

    public Boolean getIsSceneIphoneAlertSeen() {
        return isSceneIphoneAlertSeen;
    }

    public void setIsSceneIphoneAlertSeen(Boolean isSceneIphoneAlertSeen) {
        this.isSceneIphoneAlertSeen = isSceneIphoneAlertSeen;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public Boolean getOtpVerified() {
        return otpVerified;
    }

    public void setOtpVerified(Boolean otpVerified) {
        this.otpVerified = otpVerified;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getXAuthToken() {
        return xAuthToken;
    }

    public void setXAuthToken(String xAuthToken) {
        this.xAuthToken = xAuthToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}