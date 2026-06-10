const AppError = require("../errors/AppError");

const VALID_TYPES = ["irrigation", "fertilization", "harvest", "monitoring"];

module.exports = {
   execute: ({ batchId, type, executed, date }) => {
      if (!batchId) {
         throw new AppError(400, "Batch is required.");
      }

      if (!VALID_TYPES.includes(type)) {
         throw new AppError(400, "Invalid task type.");
      }

      if (executed) {
         throw new AppError(400, "Task already executed.");
      }

      if (!date) {
         throw new AppError(400, "Execution date is required.");
      }

      return {
         batchId,
         type,
         executed: true,
         date
      };
   }
};
