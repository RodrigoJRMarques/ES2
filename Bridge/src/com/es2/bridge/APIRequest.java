package com.es2.bridge;

import java.util.HashMap;

public class APIRequest {

    protected HashMap<String, APIServiceInterface> services;

    public APIRequest() {
        this.services = new HashMap<>();
    }

    public String addService(APIServiceInterface service) {
        String serviceId = String.valueOf(this.services.size());
        this.services.put(serviceId, service);
        return serviceId;
    }

    public String getContent(String serviceId, String contentId) throws ServiceNotFoundException {
        APIServiceInterface service = services.get(serviceId);
        if (service == null) {
            throw new ServiceNotFoundException();
        }

        return service.getContent(contentId);
    }

    public String setContent(String serviceId, String content) throws ServiceNotFoundException {
        APIServiceInterface service = services.get(serviceId);
        if (service == null) {
            throw new ServiceNotFoundException();
        }

        return service.setContent(content);
    }
}
