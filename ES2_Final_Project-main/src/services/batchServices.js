const AppError = require("../errors/AppError");

const VALID_STATUS = ["ACTIVE", "COMPLETED", "FAILED"];

module.exports = {
   create: ({ id, planId, status, productivity }) => {
      if (!id) {
         throw new AppError(400, "Batch id is required.");
      }

      if (!planId) {
         throw new AppError(400, "Plan is required.");
      }

      if (!VALID_STATUS.includes(status)) {
         throw new AppError(400, "Invalid batch status.");
      }

      if (productivity < 0) {
         throw new AppError(400, "Productivity must be positive.");
      }

      return {
         id,
         planId,
         status,
         productivity
      };
   }
};
