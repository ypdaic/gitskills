package com.daiyanping.cms.redis;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * @ClassName ArticleServiceImpl
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-11-12
 * @Version 0.1
 */
@AllArgsConstructor
public class ArticleServiceImpl implements IArticleService {

    Jedis jedis;

    private final String ARTICLE_INFO = "article_info:%d";

    private final String VOTE = "vote:%d";

    private final String ARTICLE_KEY = "article:%d";

    private final String ARTICLES_KEY = "articles_key";

    private final String ARTICLE_TIME_SORT = "article_time_sort";

    private final String ARTICLE_VOTE_SORT = "article_vote_sort";

    private final String ARTICLE = "article";

    private final String USER_ID = "user_id:%d";

    private final int DEFAULT_VOTE_SCORE = 400;

    @Override
    public Article publishArticle(User user) {
        Long incr = jedis.incr(ARTICLE);
        Article article = new Article();
        article.setId(incr);
        article.setTitle("test");
        article.setLink("http://xxxx");
        article.setPoster(String.format(USER_ID, user.getId()));
        article.setTime(new Date().getTime());
        article.setVotes(DEFAULT_VOTE_SCORE);

        Map<String, String> articleInfo = new HashMap<>();
        articleInfo.put("id", article.getId().toString());
        articleInfo.put("title", article.getTitle());
        articleInfo.put("link", article.getLink());
        articleInfo.put("poster", article.getPoster());
        articleInfo.put("time", article.getTime().toString());
        articleInfo.put("votes", article.getVotes().toString());

        // 存放文章的具体信息
        jedis.hmset(String.format(ARTICLE_INFO, incr), articleInfo);

        // 设置文章过期时间
        jedis.expire(String.format(ARTICLE_INFO, incr), 60 * 60 * 24 * 7);

        // 存放已发布的文章id
        jedis.lpush(ARTICLES_KEY, String.format(ARTICLE_KEY, article.getId()));

        //每个文章投票的用户id
        jedis.sadd(String.format(VOTE, article.getId()), String.format(USER_ID, user.getId().toString()));

        // 按文章发布时间存放
        jedis.zadd(ARTICLE_TIME_SORT, new Date().getTime(), String.format(ARTICLE_KEY, article.getId()));

        // 按文章的投票数存放
        jedis.zadd(ARTICLE_VOTE_SORT, DEFAULT_VOTE_SCORE, String.format(ARTICLE_KEY, article.getId()));

        return null;
    }

    @Override
    public Article vote(User user, Article article) {
        String hashKey = String.format(ARTICLE_INFO, article.getId());
        Boolean exists = jedis.exists(hashKey);
        if (exists) {

            // 判断用户是否投过票
            Boolean sismember = jedis.sismember(String.format(VOTE, article.getId()), String.format(USER_ID, user.getId().toString()));
            if (!sismember) {
                // 更新文章投票数
                jedis.hincrBy(hashKey, "votes", DEFAULT_VOTE_SCORE);
                //新增文章投票的用户id
                jedis.sadd(String.format(VOTE, article.getId()), user.getId().toString());
                // 更新投票排行
                jedis.zincrby(ARTICLE_VOTE_SORT, DEFAULT_VOTE_SCORE, String.format(ARTICLE_KEY, article.getId()));
            }

        }
        return null;
    }

    @Override
    public List<Article> queryArticlesForTime() {

        Set<String> zrevrange = jedis.zrevrange(ARTICLE_TIME_SORT, 0, 10);
        if (CollectionUtils.isNotEmpty(zrevrange)) {
            zrevrange.forEach(article -> {
                Map<String, String> stringStringMap = jedis.hgetAll(article);

            });
        }
        return null;
    }

    @Override
    public List<Article> queryArticlesForVoteNumbers() {
        Set<String> zrevrange = jedis.zrevrange(ARTICLE_VOTE_SORT, 0, 10);
        if (CollectionUtils.isNotEmpty(zrevrange)) {
            zrevrange.forEach(article -> {
                Map<String, String> stringStringMap = jedis.hgetAll(article);

            });
        }
        return null;
    }
}
