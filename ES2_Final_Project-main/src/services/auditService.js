const AppError = require("../errors/AppError");

module.exports = {
   log: ({ user, action, timestamp }) => {
      if (!user || !action || !timestamp) {
         throw new AppError(400, "Missing audit fields.");
      }

      return {
         user,
         action,
         timestamp
      };
   }
};
