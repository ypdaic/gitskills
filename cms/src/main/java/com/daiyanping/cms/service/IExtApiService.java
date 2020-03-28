package com.daiyanping.cms.service;




import com.daiyanping.cms.enums.ExtApiTypeEnum;
import com.daiyanping.cms.result.Result;

import java.util.Map;

public interface IExtApiService {
	
	/**
	 * 对外接口类型
	 * @param type
	 * @return 是否支持
	 */
	boolean supportsExtApiType(ExtApiTypeEnum type);
	
	/**
	 * 外部接口校验
	 * @return
	 */
	Result<Object> check(ExtApiTypeEnum type, Map<String, Object> urlParamMap, String requestPath, Map<String, String> reqParamMap);
}
