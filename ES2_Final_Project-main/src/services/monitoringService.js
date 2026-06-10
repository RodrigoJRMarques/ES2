const AppError = require("../errors/AppError");
const temperatureGateway = require("../gateways/temperatureGateway");
const notificationGateway = require("../gateways/notificationGateway");
const measurementService = require("./measurementService");
const alertService = require("./alertService");

function validateGateway(gateway, methodName, gatewayName) {
   if (!gateway || typeof gateway[methodName] !== "function") {
      throw new AppError(500, `${gatewayName} gateway is invalid.`);
   }
}

function createMonitoringService({
   temperatureGateway: injectedTemperatureGateway = temperatureGateway,
   notificationGateway: injectedNotificationGateway = notificationGateway
} = {}) {
   validateGateway(injectedTemperatureGateway, "read", "Temperature");
   validateGateway(injectedNotificationGateway, "send", "Notification");

   return {
      async monitorTemperature({ min, max, recipient }) {
         if (min === undefined || max === undefined) {
            throw new AppError(400, "Temperature limits are required.");
         }

         if (!recipient) {
            throw new AppError(400, "Notification recipient is required.");
         }

         const measurement = await injectedTemperatureGateway.read();
         const validatedMeasurement = measurementService.validate(measurement);
         const status = alertService.classify({
            value: validatedMeasurement.temperature,
            min,
            max
         });

         const result = {
            measurement: validatedMeasurement,
            status,
            notificationSent: false
         };

         if (status !== "OK") {
            await injectedNotificationGateway.send({
               recipient,
               status,
               temperature: validatedMeasurement.temperature,
               min,
               max
            });

            result.notificationSent = true;
         }

         return result;
      }
   };
}

module.exports = {
   createMonitoringService
};
