package com.daiyanping.cms.service.impl.ext;

import lombok.extern.slf4j.Slf4j;
import sungo.cms.common.enums.ExtApiTypeEnum;
import sungo.cms.common.service.IExtApiService;
import sungo.util.enums.FailureEnum;
import sungo.util.result.Result;
import sungo.util.result.ResultUtil;

import java.util.Map;

/**
 * appkey 校验支持
 * @author daiyanping
 */
@Slf4j
public abstract class AbstractCheckAppKeyService implements IExtApiService {

    @Override
    public Result<Object> check(ExtApiTypeEnum type, Map<String, Object> urlParamMap, String requestPath, Map<String, String> reqParamMap) {
        // 进行签名判断
        if (urlParamMap != null && urlParamMap.containsKey("app_key")) {
            String appKey = (String) urlParamMap.get("app_key");
            if (log.isDebugEnabled()) {
                // todo 后续添加到记录表
                log.debug("第三方系统请求：{}, ext-param: {}", appKey, reqParamMap.get("body"));
                // todo 防攻击可以加上nonce随机数 校验app_key,timestamp,nonce是否重复
            }

            boolean check = checkAppKey(appKey);
            if (!check) {
                return ResultUtil.failure(FailureEnum.API_AUTH_ERROR);
            }
            return ResultUtil.success();
        } else {
            return ResultUtil.failure(FailureEnum.API_AUTH_FAILURE);
        }
    }

    protected abstract boolean checkAppKey(String appkey);
}
