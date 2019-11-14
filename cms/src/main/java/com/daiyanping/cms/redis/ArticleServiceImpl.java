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

    private final String ARTICLE_INFO = "article:%d";

    private final String VOTE = "vote:%d";

    private final String ARTICLE_TIME_SORT = "article_time_sort:";

    private final String ARTICLE_VOTE_SORT = "article_vote_sort:";

    private final String ARTICLE = "article:";

    private final int DEFAULT_VOTE_SCORE = 400;

    private final int ONE_WEEK_IN_SECONDS = 60 * 60 * 24 * 7;

    private final int PAGE_SIZE = 25;

    @Override
    public Article publishArticle(User user, Article article) {
        Long incr = jedis.incr(ARTICLE);
        // 设置文章id
        article.setId(incr);

        Map<String, String> articleInfo = new HashMap<>();
        articleInfo.put("id", article.getId().toString());
        articleInfo.put("title", article.getTitle());
        articleInfo.put("link", article.getLink());
        articleInfo.put("poster", article.getPoster());
        articleInfo.put("time", article.getTime().toString());
        articleInfo.put("votes", "1");

        // 存放文章的具体信息
        jedis.hmset(String.format(ARTICLE_INFO, incr), articleInfo);

        //每个文章投票的用户id
        jedis.sadd(String.format(VOTE, article.getId()), user.getId().toString());
        jedis.expire(String.format(VOTE, article.getId()), ONE_WEEK_IN_SECONDS);

        // 按文章发布时间存放
        jedis.zadd(ARTICLE_TIME_SORT, article.getTime(), article.getId().toString());

        // 按文章的投票数存放
        jedis.zadd(ARTICLE_VOTE_SORT, DEFAULT_VOTE_SCORE, article.getId().toString());

        return article;
    }

    @Override
    public Article vote(User user, Article article) {
        //计算投票截止时间
        long cutoff = (System.currentTimeMillis() / 1000) - ONE_WEEK_IN_SECONDS;
        if (jedis.zscore(ARTICLE_TIME_SORT, article.getId().toString()) > cutoff) {

            if (jedis.sadd(String.format(VOTE, article.getId()), user.getId().toString()) == 1) {
                // 更新文章投票数
                jedis.hincrBy(String.format(ARTICLE_INFO, article.getId()), "votes", 1);
                // 更新投票排行
                jedis.zincrby(ARTICLE_VOTE_SORT, DEFAULT_VOTE_SCORE, article.getId().toString());
            }
        }

//        String hashKey = String.format(ARTICLE_INFO, article.getId());
//        Boolean exists = jedis.exists(hashKey);
//        if (exists) {
//
//            // 判断用户是否投过票
//            Boolean sismember = jedis.sismember(String.format(VOTE, article.getId()), String.format(USER_ID, user.getId().toString()));
//            if (!sismember) {
//                // 更新文章投票数
//                jedis.hincrBy(hashKey, "votes", DEFAULT_VOTE_SCORE);
//                //新增文章投票的用户id
//                jedis.sadd(String.format(VOTE, article.getId()), user.getId().toString());
//                // 更新投票排行
//                jedis.zincrby(ARTICLE_VOTE_SORT, DEFAULT_VOTE_SCORE, String.format(ARTICLE_KEY, article.getId()));
//            }

//        }
        return article;
    }

    /**
     * 根据投票时间查询
     * @param page
     * @return
     */
    @Override
    public List<Article> queryArticlesForTime(int page) {
        int start = (page - 1) * PAGE_SIZE;
        int end = page * PAGE_SIZE - 1;

        Set<String> zrevrange = jedis.zrevrange(ARTICLE_TIME_SORT, start, end);
        ArrayList<Article> articles = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(zrevrange)) {

            zrevrange.forEach(article -> {
                Map<String, String> articleMap = jedis.hgetAll(article);
                Article article1 = new Article();
                article1.setId(Long.valueOf(articleMap.get("id")));
                article1.setLink(articleMap.get("link"));
                article1.setPoster(articleMap.get("poster"));
                article1.setTime(Long.valueOf(articleMap.get("time")));
                article1.setVotes(Integer.valueOf(articleMap.get("votes")));

                articles.add(article1);

            });
        }
        return articles;
    }

    /**
     * 根据投票分数查询
     * @param page
     * @return
     */
    @Override
    public List<Article> queryArticlesForVoteNumbers(int page) {
        int start = (page - 1) * PAGE_SIZE;
        int end = page * PAGE_SIZE - 1;

        Set<String> zrevrange = jedis.zrevrange(ARTICLE_VOTE_SORT, start, end);
        ArrayList<Article> articles = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(zrevrange)) {

            zrevrange.forEach(article -> {
                Map<String, String> articleMap = jedis.hgetAll(ARTICLE + article);
                Article article1 = new Article();
                article1.setId(Long.valueOf(articleMap.get("id")));
                article1.setLink(articleMap.get("link"));
                article1.setPoster(articleMap.get("poster"));
                article1.setTime(Long.valueOf(articleMap.get("time")));
                article1.setVotes(Integer.valueOf(articleMap.get("votes")));

                articles.add(article1);

            });
        }
        return articles;
    }

    public static void main(String[] args) {
        User user = new User();
        user.setId(2);
        Article article = new Article();
        article.setTime(new Date().getTime());
        article.setVotes(1);
        article.setPoster("user:" + user.getId());
        article.setLink("http://xxx");
        article.setTitle("hahah");

        Jedis jedis = new Jedis("localhost", 6379);
        ArticleServiceImpl articleService = new ArticleServiceImpl(jedis);
        // 发布文章
        articleService.publishArticle(user, article);

        User user1 = new User();
        user1.setId(3);

        // 投票
        articleService.vote(user1, article);

        List<Article> articles = articleService.queryArticlesForVoteNumbers(1);
        articles.forEach(System.out::println);

    }
}
