package com.es2.bridge;

public class APIRequestContentAggregator extends APIRequest {

    public APIRequestContentAggregator() {
        super();
    }

    @Override
    public String getContent(String serviceId, String contentId) throws ServiceNotFoundException {
        APIServiceInterface service = services.get(serviceId);
        if (service == null) {
            throw new ServiceNotFoundException();
        }

        return service.getContent(contentId);
    }
}
