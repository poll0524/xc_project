package com.example.collecttoolserver.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.collecttoolserver.common.HttpTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ArticleCollectUtil {
    private static final String KEY_NAME = "name"; // 标签名称
    private static final String KEY_TEXT = "text"; // 文本信息
    private static final String KEY_HREF = "href"; // a标签链接

    /**
     * 根据文章链接抓取文章内容
     *
     * @param url 文章链接
     * @return 文章内容
     */
    public static Map<String,Object> getArticle(String url){
        // 请求与响应
        String resp = HttpTool.get(url, getWxHeaderMap());

        // 解析
        Map acticleResp = getWxActicleContent(resp, url);

        return acticleResp;
    }

    /**
     * 微信公众号请求头设置
     */
    public static Map<String, String> getWxHeaderMap() {
        Map<String, String> map = new HashMap<>(new LinkedHashMap<>());
        map.put("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
        map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.8, en-US; q=0.5, en; q=0.3");
        map.put("Host", "mp.weixin.qq.com");
        map.put("If-Modified-Since", "Sat, 04 Jan 2020 12:23:43 GMT");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
        return map;
    }

    /**
     * 解析微信公众号文章
     *
     * @param resp 请求文章响应
     * @param url 文章链接
     * @return 文章信息
     */
    public static Map getWxActicleContent(String resp, String url) {
        try {
            Document document = Jsoup.parse(resp);
            // 文章出处（作者）
            String referName = document.getElementsByClass("profile_nickname").get(0).text();
            // 文章封面图链接
            String coverUrl = document.select("meta[property=\"og:image\"]").get(0).attr("content");
            // 文章标题
            String title = document.getElementById("activity-name").text();
            Element a = document.head();
            JSONArray tags = new JSONArray();
            Elements sections = a.select("*");
            for (Element element : sections) {
                if (element.children().isEmpty()) {
                    getChildTag(element, tags);
                }
            }

            String style = (String) tags.getJSONObject(0).get("text");

            // 文章内容
            Element content = document.getElementsByClass("rich_media_content").get(0);


            Map<String,Object> map = new HashMap<>();

            map.put("title",title);
            map.put("coverImg",coverUrl);
            map.put("content",content.toString()+style);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String,Object> map = new HashMap<>();
            map.put("content","文章解析失败");
            return map;
        }
    }

    public static void getChildTag(Element element, JSONArray tags) {
        JSONObject tag = new JSONObject(new LinkedHashMap<>());
        String tagName = element.tagName();
        tag.put(KEY_NAME, tagName);
        switch (tagName) {
            case "span": {
                tag.put(KEY_TEXT, element.text());
                tags.add(tag);
                break;
            }
            case "img": {
                Attributes attrs = element.attributes();
                if (attrs != null) {
                    for (Attribute attr : attrs) {
                        tag.put(attr.getKey().replace("-", ""), attr.getValue());
                    }
                }
                tags.add(tag);
                break;
            }
            case "a": {
                tag.put(KEY_HREF, element.attr("href"));
                tag.put(KEY_TEXT, element.attr("textvalue"));
                tags.add(tag);
                break;
            }
            case "br": {
                tags.add(tag);
                break;
            }
            case "p": {
                tag.put(KEY_TEXT, element.text());
                tags.add(tag);
                break;
            }
            case "style": {
                tag.put(KEY_TEXT, element.toString());
                tags.add(tag);
                break;
            }
            default:
                break;
        }
    }

}
