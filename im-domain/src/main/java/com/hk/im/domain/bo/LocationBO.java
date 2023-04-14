package com.hk.im.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author : HK意境
 * @ClassName : LocationBO
 * @date : 2023/4/13 21:20
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class LocationBO {

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;


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
    private String area;


    /**
     * 街道
     */
    private String streets;

    /**
     * 详细地址：如 重庆理工大学花溪校区
     */
    private String address;


    /**
     * 地点code
     */
    private Integer code;


}
