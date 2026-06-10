const AppError = require("../errors/AppError");

module.exports = {
   run: ({ mode, condition }) => {
      if (!mode) {
         throw new AppError(400, "Mode is required.");
      }

      if (mode === "MANUAL" && condition === true) {
         throw new AppError(400, "Cannot run automation in manual mode.");
      }

      if (mode === "AUTOMATIC" && condition === true) {
         return { executed: true };
      }

      return { executed: false };
   }
};
