package com.daiyanping.cms.service.impl.ext;

import com.daiyanping.cms.enums.ExtApiTypeEnum;
import com.daiyanping.cms.enums.FailureEnum;
import com.daiyanping.cms.result.Result;
import com.daiyanping.cms.result.ResultUtil;
import com.daiyanping.cms.service.IExtApiService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * sign 校验支持
 * @author daiyanping
 */
@Slf4j
public abstract class AbstractCheckSignService implements IExtApiService {

	@Override
	public Result<Object> check(ExtApiTypeEnum type, Map<String, Object> urlParamMap, String requestPath, Map<String, String> reqParamMap) {
		// 进行签名判断
        if (urlParamMap != null && urlParamMap.containsKey("app_key") && urlParamMap.containsKey("timestamp") && urlParamMap.containsKey("sign")) {
            String appkey = (String) urlParamMap.get("app_key");
            Long timestamp = Long.parseLong((String) urlParamMap.get("timestamp"));
            if (log.isDebugEnabled()) {
            	// todo 后续添加到记录表
                log.debug("第三方系统请求：{}, {} , {} , ext-param: {}", appkey, requestPath, timestamp, reqParamMap.get("body"));
            }
            
            // todo 防攻击可以加上nonce随机数 校验app_key,timestamp,nonce是否重复
            String sign = (String) urlParamMap.get("sign");
            long beg = System.currentTimeMillis();
            if (beg - timestamp > 10 * 60 * 1000) {
                return ResultUtil.failure(FailureEnum.API_TIME_ERROR);
            }
            boolean check = checkSign(appkey, timestamp, sign);
            if (!check) {
                return ResultUtil.failure(FailureEnum.API_AUTH_ERROR);
            }
            return ResultUtil.success();
        } 
        return ResultUtil.failure(FailureEnum.API_AUTH_FAILURE);
	}
	
	protected abstract boolean checkSign(String appkey, Long timestamp, String sign);

	
}
