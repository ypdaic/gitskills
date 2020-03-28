package com.daiyanping.cms.service.impl.ext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import sungo.cms.common.enums.ExtApiTypeEnum;
import sungo.cms.common.service.IExtApiService;
import sungo.util.result.Result;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Primary
public class ExtApiComposite implements IExtApiService {

	@Autowired
    List<IExtApiService> extApiServices;

	private final Map<String, IExtApiService> extApiServiceCache = new ConcurrentHashMap<>(4);

	private IExtApiService getExtApiService(String key, ExtApiTypeEnum extApiTypeEnum) {
        IExtApiService currentExtApiService = this.extApiServiceCache.get(key);
        if (Objects.isNull(currentExtApiService)) {
            for (IExtApiService extApiService : this.extApiServices) {
                if (extApiService.supportsExtApiType(extApiTypeEnum)) {
                    currentExtApiService = extApiService;
                    this.extApiServiceCache.put(key, currentExtApiService);
                    break;
                }
            }
        }
        return currentExtApiService;
    }

    @Override
    public boolean supportsExtApiType(ExtApiTypeEnum extApiTypeEnum) {
        return !Objects.isNull(getExtApiService(extApiTypeEnum.name(), extApiTypeEnum));
    }

    @Override
    public Result<Object> check(ExtApiTypeEnum type, Map<String, Object> urlParamMap, String requestPath, Map<String, String> reqParamMap) {
        IExtApiService extApiService = getExtApiService(type.name(), type);
        if (Objects.isNull(extApiService)) {
            throw new IllegalArgumentException("不支持" + type.name() + "类型 的对外接口！");
        }
        return extApiService.check(type, urlParamMap, requestPath, reqParamMap);
    }
}
