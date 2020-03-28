package com.daiyanping.cms.service.impl.ext;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sungo.cms.common.enums.ExtApiTypeEnum;
import sungo.feign.client.remote.AuthRemoteClient;
import sungo.util.result.Result;

import java.util.Objects;

@Data
@Service
public class CmsCheckAppKeyServiceImpl extends AbstractCheckAppKeyService {

    @Autowired
    AuthRemoteClient authRemoteClient;

    @Override
    protected boolean checkAppKey(String appkey) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("app_key", appkey);
        Result result = authRemoteClient.checkAppKey(jsonObject);
        if (Objects.isNull(result) || !result.isSuccess()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supportsExtApiType(ExtApiTypeEnum type) {
        return ExtApiTypeEnum.CMS_APP_KEY == type;
    }
}
