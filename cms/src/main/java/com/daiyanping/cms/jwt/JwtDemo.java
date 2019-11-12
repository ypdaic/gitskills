package com.daiyanping.cms.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;

import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * @ClassName JwtDemo
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-11-12
 * @Version 0.1
 */
public class JwtDemo {

    private int expireTime = 60 * 60 * 24 * 7;


    public static void main(String[] args) {
        create();
    }

    public static void create() {
        // 签名算法
        SignatureAlgorithm hs256 = SignatureAlgorithm.HS256;

        Date date = new Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DATE, 7);
        Map<String, Object> playLoad = new HashMap<>();
        playLoad.put("oid", UUID.randomUUID().toString());
        playLoad.put("org", "www.dai.com");

        SecretKeySpec key = key();

        JwtBuilder jwtBuilder = Jwts.builder().setClaims(playLoad)
                .setId("JWT") //设置类型
                .setIssuedAt(date) //设置创建时间
                .setIssuer("test") //设置创建人
                .signWith(hs256, key) // 设置签名
                .setExpiration(instance.getTime()); //设置过期时间
        // token 生成
        String compact = jwtBuilder.compact();

        System.out.println(compact);


        Claims parse = parse(compact);

        Object iss = parse.get("iss");
        System.out.println(iss);


    }

    public static SecretKeySpec key() {
        byte[] bytes = Base64Codec.BASE64.decode("kSUdVKL0j0JGTAIo8uY5ZnwO9nZAemg6ehgOHozK");
        SecretKeySpec key = new SecretKeySpec(bytes, 0, bytes.length, "AES");
        return key;
    }

    public static Claims parse(String token) {
        SecretKeySpec key = key();
        Claims body = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

        System.out.println(body.toString());

        return body;
    }

    /**
     * 为了防止用户 JWT 令牌泄露而威胁系统安全，你可以在以下几个方面完善系统功能：
     *
     * 清除已泄露的令牌：此方案最直接，也容易实现，你需将 JWT 令牌在服务端也存储一份，若发现有异常的令牌存在，则从服务端令牌列表中将此异常令牌清除。当用户发起请求时，强制用户重新进行身份验证，直至验证成功。对于服务端的令牌存储，可以借助 Redis 等缓存服务器进行管理，也可以使用 Ehcache 将令牌信息存储在内存中。
     *
     * 敏感操作保护：在涉及到诸如新增，修改，删除，上传，下载等敏感性操作时，定期(30分钟，15分钟甚至更短)检查用户身份，如手机验证码，扫描二维码等手段，确认操作者是用户本人。如果身份验证不通过，则终止请求，并要求重新验证用户身份信息。
     *
     * 地域检查：通常用户会在一个相对固定的地理范围内访问应用程序，可以将地理位置信息作为一个辅助来甄别用户的 JWT 令牌是否存在问题。如果发现用户A由经常所在的地区 1 变到了相对较远的地区 2 ，或者频繁在多个地区间切换，不管用户有没有可能在短时间内在多个地域活动(一般不可能)，都应当终止当前请求，强制用户重新进行验证身份，颁发新的 JWT 令牌，并提醒(或要求)用户重置密码。
     *
     * 监控请求频率：如果 JWT 密令被盗取，攻击者或通过某些工具伪造用户身份，高频次的对系统发送请求，以套取用户数据。针对这种情况，可以监控用户在单位时间内的请求次数，当单位时间内的请求次数超出预定阈值值，则判定该用户密令是有问题的。例如 1 秒内连续超过 5 次请求，则视为用户身份非法，服务端终止请求并强制将该用户的 JWT 密令清除，然后回跳到认证中心对用户身份进行验证。
     *
     * 客户端环境检查：对于一些移动端应用来说，可以将用户信息与设备(手机,平板)的机器码进行绑定，并存储于服务端中，当客户端发起请求时，可以先校验客户端的机器码与服务端的是否匹配，如果不匹配，则视为非法请求，并终止用户的后续请求。
     */
}
