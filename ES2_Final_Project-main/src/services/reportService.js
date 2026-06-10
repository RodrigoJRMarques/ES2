const AppError = require("../errors/AppError");

module.exports = {
   generate: ({ format, data }) => {
      if (!data || data.length === 0) {
         throw new AppError(400, "No data to export.");
      }

      if (!["CSV", "EXCEL"].includes(format)) {
         throw new AppError(400, "Invalid format.");
      }

      return {
         format,
         exported: true
      };
   }
};
