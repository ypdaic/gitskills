package com.daiyanping.cms.service.impl.ext;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sungo.cms.common.enums.ExtApiTypeEnum;
import sungo.feign.client.remote.AuthRemoteClient;
import sungo.util.result.Result;

import java.util.Objects;

/**
 * CMS 对外服务
 * @author daiyanping
 *
 */
@Data
@Service
public class CmsCheckSignServiceImpl extends AbstractCheckSignService {

	@Autowired
	AuthRemoteClient authRemoteClient;

	@Override
	protected boolean checkSign(String appkey, Long timestamp, String sign) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("app_key", appkey);
		jsonObject.put("timestamp", timestamp);
		jsonObject.put("sign", sign);
		Result result = authRemoteClient.checkSign(jsonObject);
		if (Objects.isNull(result) || !result.isSuccess()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean supportsExtApiType(ExtApiTypeEnum type) {
		return ExtApiTypeEnum.CMS_SIGN == type;
	}

}
