package com.hk.im.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HK意境
 * @ClassName : SearchDocumentRequest
 * @date : 2023/4/23 14:41
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@Accessors(chain = true)
public class SearchDocumentRequest {

    private String q;
    private Integer offset = 0;
    private Integer limit = 20;
    private String[] attributesToRetrieve = new String[]{"*"};
    private String[] attributesToCrop;
    private Integer cropLength = 10;
    private String cropMarker = "...";
    private String highlightPreTag = "<em>";
    private String highlightPostTag = "</em>";
    private String[] attributesToHighlight;
    private String[] filter;
    private String[][] filterArray;
    private Boolean showMatchesPosition;
    private String[] facets;
    private String[] sort;
    private String matchingStrategy = "last";
    protected Integer page = 1;
    protected Integer hitsPerPage = 1;


}
