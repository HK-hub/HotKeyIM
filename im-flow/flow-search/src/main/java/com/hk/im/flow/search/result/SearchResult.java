package com.hk.im.flow.search.result;

import java.util.List;

/**
 * @ClassName : SearchResult
 * @author : HK意境
 * @date : 2023/4/16 14:43
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class SearchResult<T> {

    private String query;
    private long offset;
    private long limit;
    private long processingTimeMs;
    private long nbHits;

    private boolean exhaustiveNbHits;

    private List<T> hits;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    public long getNbHits() {
        return nbHits;
    }

    public void setNbHits(long nbHits) {
        this.nbHits = nbHits;
    }

    public boolean isExhaustiveNbHits() {
        return exhaustiveNbHits;
    }

    public void setExhaustiveNbHits(boolean exhaustiveNbHits) {
        this.exhaustiveNbHits = exhaustiveNbHits;
    }

    public List<T> getHits() {
        return hits;
    }

    public void setHits(List<T> hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "query='" + query + '\'' +
                ", offset=" + offset +
                ", limit=" + limit +
                ", processingTimeMs=" + processingTimeMs +
                ", nbHits=" + nbHits +
                ", exhaustiveNbHits=" + exhaustiveNbHits +
                ", hits=" + hits +
                '}';
    }
}