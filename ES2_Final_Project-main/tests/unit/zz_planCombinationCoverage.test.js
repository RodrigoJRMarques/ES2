const planService = require("../../src/services/planService");

describe("planService - additional logical combination coverage", () => {
  it("accepts a non-punctual plan even when manager approval is explicitly true", () => {
    expect(() =>
      planService.validate({
        type: "regular",
        approvedByManager: true
      })
    ).not.toThrow();
  });
});
