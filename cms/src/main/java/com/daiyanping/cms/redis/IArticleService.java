package com.daiyanping.cms.redis;

import java.util.List;

/**
 * @ClassName IArticleService
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-11-12
 * @Version 0.1
 */
public interface IArticleService {

    /**
     * 发布文章
     * @param user
     * @return
     */
    Article publishArticle(User user);

    /**
     * 投票
     * @param user
     * @param article
     */
    Article vote(User user, Article article);

    /**
     * 根据投票时间查询文章
     * @return
     */
    List<Article> queryArticlesForTime();

    /**
     * 根据投票数查询文章
     * @return
     */
    List<Article> queryArticlesForVoteNumbers();
}
