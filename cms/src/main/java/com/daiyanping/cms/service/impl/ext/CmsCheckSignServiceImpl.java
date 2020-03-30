package com.daiyanping.cms.service.impl.ext;

import com.alibaba.fastjson.JSONObject;
import com.daiyanping.cms.enums.ExtApiTypeEnum;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * CMS 对外服务
 * @author daiyanping
 *
 */
@Data
@Service
public class CmsCheckSignServiceImpl extends AbstractCheckSignService {


	@Override
	protected boolean checkSign(String appkey, Long timestamp, String sign) {

		return true;
	}

	@Override
	public boolean supportsExtApiType(ExtApiTypeEnum type) {
		return ExtApiTypeEnum.CMS_SIGN == type;
	}

}
