package com.example.wynewsobject;

public class NewsTab_Object {

    /**
     * alias : The Truth
     * bannerOrder : 105
     * cid : C1348654575297
     * color : 
     * ename : zhenhua
     * hasCover : false
     * hasIcon : true
     * headLine : false
     * img : http://img2.cache.netease.com/m/newsapp/banner/zhenhua.png
     * isHot : 0
     * isNew : 0
     * recommend : 0
     * recommendOrder : 0
     * showType : comment
     * special : 0
     * subnum : 9.9万
     * template : manual
     * tid : T1370583240249
     * tname : 原创
     * topicid : 00040BGE
     */

    private String alias;
    private int bannerOrder;
    private String cid;
    private String img;
    private String tid;
    private String tname;

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setBannerOrder(int bannerOrder) {
        this.bannerOrder = bannerOrder;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getAlias() {
        return alias;
    }

    public int getBannerOrder() {
        return bannerOrder;
    }

    public String getCid() {
        return cid;
    }

    public String getImg() {
        return img;
    }

    public String getTid() {
        return tid;
    }

    public String getTname() {
        return tname;
    }
}
