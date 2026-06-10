const { createMonitoringService } = require("../../src/services/monitoringService");

describe("monitoringService", () => {
   it("uses a temperature gateway stub to read automatic measurements", async () => {
      const temperatureGatewayStub = {
         read: async () => ({
            temperature: 17,
            humidity: 60,
            luminosity: 14000
         })
      };
      const notificationGatewayMock = {
         send: jest.fn().mockResolvedValue({ delivered: true })
      };
      const monitoringService = createMonitoringService({
         temperatureGateway: temperatureGatewayStub,
         notificationGateway: notificationGatewayMock
      });

      const result = await monitoringService.monitorTemperature({
         min: 18,
         max: 28,
         recipient: "manager@greenherb.pt"
      });

      expect(result.status).toBe("CRITICAL");
      expect(result.measurement.temperature).toBe(17);
      expect(result.notificationSent).toBe(true);
   });

   it("uses a notification gateway mock to verify alert delivery", async () => {
      const temperatureGatewayStub = {
         read: async () => ({
            temperature: 30,
            humidity: 61,
            luminosity: 15500
         })
      };
      const notificationGatewayMock = {
         send: jest.fn().mockResolvedValue({ delivered: true })
      };
      const monitoringService = createMonitoringService({
         temperatureGateway: temperatureGatewayStub,
         notificationGateway: notificationGatewayMock
      });

      await monitoringService.monitorTemperature({
         min: 18,
         max: 28,
         recipient: "manager@greenherb.pt"
      });

      expect(notificationGatewayMock.send).toHaveBeenCalledTimes(1);
      expect(notificationGatewayMock.send).toHaveBeenCalledWith({
         recipient: "manager@greenherb.pt",
         status: "WARNING",
         temperature: 30,
         min: 18,
         max: 28
      });
   });

   it("does not send notifications when the temperature is within range", async () => {
      const temperatureGatewayStub = {
         read: async () => ({
            temperature: 24,
            humidity: 58,
            luminosity: 13000
         })
      };
      const notificationGatewayMock = {
         send: jest.fn().mockResolvedValue({ delivered: true })
      };
      const monitoringService = createMonitoringService({
         temperatureGateway: temperatureGatewayStub,
         notificationGateway: notificationGatewayMock
      });

      const result = await monitoringService.monitorTemperature({
         min: 18,
         max: 28,
         recipient: "manager@greenherb.pt"
      });

      expect(result.status).toBe("OK");
      expect(result.notificationSent).toBe(false);
      expect(notificationGatewayMock.send).not.toHaveBeenCalled();
   });

   it("rejects incomplete automatic measurements returned by the gateway", async () => {
      const temperatureGatewayStub = {
         read: async () => ({
            temperature: 24
         })
      };
      const notificationGatewayMock = {
         send: jest.fn().mockResolvedValue({ delivered: true })
      };
      const monitoringService = createMonitoringService({
         temperatureGateway: temperatureGatewayStub,
         notificationGateway: notificationGatewayMock
      });

      await expect(
         monitoringService.monitorTemperature({
            min: 18,
            max: 28,
            recipient: "manager@greenherb.pt"
         })
      ).rejects.toThrow("Missing measurement data.");
   });
});
