const AppError = require("../errors/AppError");

const VALID_TYPES = ["regular", "emergency", "punctual"];

function validateRange(value, min, max, field) {
   if (value < min || value > max) {
      throw new AppError(400, `${field} out of range.`);
   }
}

module.exports = {
   validate: (data) => {
      if (!data || !data.type) {
         throw new AppError(400, "Invalid plan data.");
      }

      const {
         type,
         temperature,
         humidity,
         luminosity,
         duration,
         approvedByManager
      } = data;

      // Tipo
      if (!VALID_TYPES.includes(type)) {
         throw new AppError(400, "Invalid plan type");
      }

      // Condição múltipla (plano pontual)
      if (type === "punctual" && !approvedByManager) {
         throw new AppError(403, "Authorization required");
      }

      // Valores limite (se existirem)
      if (temperature) {
         validateRange(temperature.min, 18, 28, "Temperature min");
         validateRange(temperature.max, 18, 28, "Temperature max");
      }

      if (humidity) {
         validateRange(humidity.min, 40, 80, "Humidity min");
         validateRange(humidity.max, 40, 80, "Humidity max");
      }

      if (luminosity) {
         validateRange(luminosity.min, 5000, 25000, "Luminosity min");
         validateRange(luminosity.max, 5000, 25000, "Luminosity max");
      }

      if (duration !== undefined) {
         validateRange(duration, 1, 365, "Duration");
      }

      return {
         ...data,
         validated: true
      };
   }
};
