const AppError = require("../errors/AppError");

function parseCSV(csv) {
   const lines = csv
      .trim()
      .split("\n")
      .map(l => l.trim())
      .filter(l => l.length > 0);

      if (lines.length <= 1) {
      throw new AppError(400, "File is empty.");
   }

   const dataLines = lines.slice(1);

   let valid = 0;
   let invalid = 0;

   for (const line of dataLines) {
      const [name, scientificName] = line.split(",").map(v => v?.trim());

      if (name && scientificName) {
      valid++;
      } else {
      invalid++;
      }
   }

   return {
      valid,
      invalid,
      total: valid + invalid
   };
}

module.exports = {
   import: async (csv) => {
      if (!csv || csv.trim() === "") {
         throw new AppError(400, "File is empty.");
      }

      return parseCSV(csv);
   }
};
