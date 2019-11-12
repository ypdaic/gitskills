package com.daiyanping.cms.redis;

import lombok.Data;

/**
 * @ClassName Article
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-11-12
 * @Version 0.1
 */
@Data
public class Article {
    //文章id
    private Long id;
    // 文章标题
    private String title;
    // 文章链接
    private String link;
    // 文章发布者
    private String poster;
    // 文章发布时间
    private Long time;
    // 文章投票
    private Integer votes;
}
