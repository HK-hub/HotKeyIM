package com.hk.im.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : CodeMessageExtra
 * @date : 2023/3/16 16:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class LocationMessageExtra extends BaseMessageExtra{

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 国家
     */
    private String country;

    /**
     * 省分: 如重庆市
     */
    private String province;

    /**
     * 城市：如重庆
     */
    private String city;


    /**
     * 区：如巴南区
     */
    private String district;


    /**
     * 街道
     */
    private String streets;


    /**
     * 多少号
     */
    private String number;

    /**
     * 街道编码
     */
    private String towncode;


    /**
     * 街道
     */
    private String township;

    /**
     * 详细地址：如 重庆理工大学花溪校区
     */
    private String address;


    /**
     * 地点code
     */
    private Integer code;


}
