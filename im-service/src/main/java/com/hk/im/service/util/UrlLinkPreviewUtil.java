package com.hk.im.service.util;

import com.hk.im.domain.bo.PreviewLinkBO;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.*;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : UrlLinkPreviewUtil
 * @date : 2023/4/14 21:11
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class UrlLinkPreviewUtil {

    private static final int TIMEOUT_MS = 5000;


    /**
     * 获取预览信息
     * @param url
     * @return
     * @throws IOException
     */
    public static PreviewLinkBO scrapeLink(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setConnectTimeout(TIMEOUT_MS);
        connection.setReadTimeout(TIMEOUT_MS);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

        Document document = Jsoup.parse(connection.getInputStream(), null, url);
        Elements metaElements = document.select("meta");
        Elements titleElements = document.select("title");
        Elements imageElements = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");

        PreviewLinkBO linkPreviewData = new PreviewLinkBO();
        linkPreviewData.setUrl(url);
        linkPreviewData.setTitle(titleElements.text());
        linkPreviewData.setDomain(extractDomainName(url));

        for (Element element : metaElements) {
            String property = element.attr("property").toLowerCase();
            if ("og:title".equals(property) && linkPreviewData.getTitle().isEmpty()) {
                linkPreviewData.setTitle(element.attr("content"));
            }
            if ("og:description".equals(property)) {
                linkPreviewData.setDescription(element.attr("content"));
            }
            if ("og:image".equals(property)) {
                linkPreviewData.setImageUrl(element.attr("content"));
            }
        }

        if (StringUtils.isEmpty(linkPreviewData.getImageUrl()) && !imageElements.isEmpty()) {
            if (Objects.nonNull(imageElements.first())) {
                // 图片存在：使用图片
                linkPreviewData.setImageUrl(imageElements.first().attr("src"));
            } else {
                // 使用图标logo
                Element element = document.head().select("link[href~=.*\\.(ico|png)]").first();
                if (element != null) {
                    linkPreviewData.setImageUrl(element.attr("href"));
                }
            }
        }

        if (StringUtils.isEmpty(linkPreviewData.getDescription())) {
            linkPreviewData.setDescription(document.body().text().substring(0, Math.min(document.body().text().length(), 200)));
        }

        return linkPreviewData;
    }


    /**
     * 获取域名
     *
     * @param url
     *
     * @return
     *
     * @throws MalformedURLException
     */
    private static String extractDomainName(String url) throws MalformedURLException {
        URL u = new URL(url);
        String host = u.getHost().toLowerCase();
        if (host.startsWith("www.")) {
            host = host.substring(4);
        }
        int port = u.getPort();
        if (port != -1) {
            host = host + ":" + port;
        }
        return host;
    }

}

