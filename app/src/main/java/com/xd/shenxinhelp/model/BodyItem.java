package com.xd.shenxinhelp.model;

/**
 * Created by weiyuyang on 17-3-10.
 */

public class BodyItem {
    private String id;
    private String buwei;
    private String name;
    private String reosurce_url;
    private String total_time;
    private String fee_cridits;
    private String heat;
    private String get_degree;

    public void setId(String id) {
        this.id = id;
    }

    public void setBuwei(String buwei) {
        this.buwei = buwei;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReosurce_url(String reosurce_url) {
        this.reosurce_url = reosurce_url;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    public void setFee_cridits(String fee_cridits) {
        this.fee_cridits = fee_cridits;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    public void setGet_degree(String get_degree) {
        this.get_degree = get_degree;
    }

    public void setDiffculty(String diffculty) {
        this.diffculty = diffculty;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    private String diffculty;
    private String webUrl;


    public String getId() {
        return id;
    }

    public String getBuwei() {
        return buwei;
    }

    public String getName() {
        return name;
    }

    public String getReosurce_url() {
        return reosurce_url;
    }

    public String getTotal_time() {
        return total_time;
    }

    public String getFee_cridits() {
        return fee_cridits;
    }

    public String getHeat() {
        return heat;
    }

    public String getGet_degree() {
        return get_degree;
    }

    public String getDiffculty() {
        return diffculty;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
