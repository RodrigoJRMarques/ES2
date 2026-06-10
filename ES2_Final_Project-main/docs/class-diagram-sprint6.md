# Sprint 6 - Diagrama de Classes Global

O diagrama abaixo representa a solucao adotada no Sprint 6 para isolamento das dependencias externas com duplos de teste.

```mermaid
classDiagram
   class MonitoringService {
      +monitorTemperature(min, max, recipient)
   }

   class MeasurementService {
      +validate(temperature, humidity, luminosity)
   }

   class AlertService {
      +classify(value, min, max)
   }

   class TemperatureGateway {
      <<gateway>>
      +read()
   }

   class NotificationGateway {
      <<gateway>>
      +send(notification)
   }

   class TemperatureGatewayStub {
      <<stub>>
      +read()
   }

   class NotificationGatewayMock {
      <<mock>>
      +send(notification)
   }

   MonitoringService --> TemperatureGateway : obtem medicao automatica
   MonitoringService --> NotificationGateway : envia notificacao
   MonitoringService --> MeasurementService : valida medicao
   MonitoringService --> AlertService : classifica temperatura

   TemperatureGatewayStub ..|> TemperatureGateway
   NotificationGatewayMock ..|> NotificationGateway
```

## Resumo para o relatorio

- O gateway de temperatura foi substituido por um `stub`, porque a leitura automatica precisa apenas de devolver valores controlados para exercitar os cenarios de negocio.
- O gateway de notificacoes foi substituido por um `mock`, porque o objetivo do teste e confirmar a interacao e garantir que a logica de envio e realmente executada quando existe alerta.
