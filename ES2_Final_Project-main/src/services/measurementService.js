const AppError = require("../errors/AppError");

module.exports = {
   validate: ({ temperature, humidity, luminosity }) => {
      if (temperature === undefined || humidity === undefined || luminosity === undefined) {
         throw new AppError(400, "Missing measurement data.");
      }

      return {
         temperature,
         humidity,
         luminosity,
         valid: true
      };
   }
};
