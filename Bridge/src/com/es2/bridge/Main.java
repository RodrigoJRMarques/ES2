package com.es2.bridge;

public class Main {

    public static void main(String[] args) {
        try {
            APIRequest request = new APIRequest();
            String serviceId = request.addService(new APIMoodle());

            String c1 = request.setContent(serviceId, "Primeiro conteúdo. ");
            String c2 = request.setContent(serviceId, "Segundo conteúdo.");

            System.out.println("Service ID: " + serviceId);
            System.out.println("Content ID 1: " + c1 + " -> " + request.getContent(serviceId, c1));
            System.out.println("Content ID 2: " + c2 + " -> " + request.getContent(serviceId, c2));

            APIRequestContentAggregator aggregator = new APIRequestContentAggregator();
            String aggServiceId = aggregator.addService(new APIMoodle());
            aggregator.setContent(aggServiceId, "A ");
            aggregator.setContent(aggServiceId, "B ");
            aggregator.setContent(aggServiceId, "C");

            System.out.println("Aggregated: " + aggregator.getContent(aggServiceId, "ignored"));
        } catch (ServiceNotFoundException exception) {
            System.err.println("Erro: serviço não encontrado.");
        }
    }
}
