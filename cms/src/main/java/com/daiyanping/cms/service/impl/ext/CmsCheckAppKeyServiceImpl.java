package com.daiyanping.cms.service.impl.ext;

import com.alibaba.fastjson.JSONObject;
import com.daiyanping.cms.enums.ExtApiTypeEnum;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Data
@Service
public class CmsCheckAppKeyServiceImpl extends AbstractCheckAppKeyService {


    @Override
    protected boolean checkAppKey(String appkey) {

        return true;
    }

    @Override
    public boolean supportsExtApiType(ExtApiTypeEnum type) {
        return ExtApiTypeEnum.CMS_APP_KEY == type;
    }
}
