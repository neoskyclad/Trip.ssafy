package com.ssafy.tripssafy.model.dto;

import java.util.List;

import lombok.Data;

//api DetailCommon Response
@Data
public class TourSpotDetailResponse {
    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Data
    public static class Items {
        private List<Item> item;
    }

    @Data
    public static class Item {
        private Long contentid;
        private Long contenttypeid;
        private String title;
        private String createdtime;
        private String modifiedtime;
        private String tel;
        private String telname;
        private String homepage;
        private String booktour;
        private String firstimage;
        private String firstimage2;
        private String cpyrhtDivCd;
        private String addr1;
        private String addr2;
        private String zipcode;
        private Double mapx;
        private Double mapy;
        private String mlevel;
        private String overview;

    }
}
