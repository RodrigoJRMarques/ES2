const { authenticate, authorize } = require("../../src/middlewares/authMiddleware");
const tokenService = require("../../src/services/tokenService");

describe("authMiddleware", () => {
  beforeEach(() => {
    tokenService.clearRefreshTokens();
  });

  it("authenticates a request with a valid bearer token", () => {
    const tokens = tokenService.issueTokens({
      id: "middleware-user",
      email: "middleware@greenherb.pt",
      role: "ADMIN"
    });
    const req = {
      headers: {
        authorization: `Bearer ${tokens.accessToken}`
      }
    };
    const next = jest.fn();

    authenticate(req, {}, next);

    expect(req.user.sub).toBe("middleware-user");
    expect(next).toHaveBeenCalledWith();
  });

  it("rejects a request without a bearer token", () => {
    const req = { headers: {} };
    const next = jest.fn();

    authenticate(req, {}, next);

    expect(next).toHaveBeenCalledWith(
      expect.objectContaining({
        statusCode: 401,
        message: "Bearer token is required."
      })
    );
  });

  it("rejects a request with an invalid bearer token", () => {
    const req = {
      headers: {
        authorization: "Bearer invalid.jwt.token"
      }
    };
    const next = jest.fn();

    authenticate(req, {}, next);

    expect(next).toHaveBeenCalledWith(
      expect.objectContaining({
        statusCode: 401,
        message: "Access token is invalid or expired."
      })
    );
  });

  it("authorizes a user with the required role", () => {
    const middleware = authorize("ADMIN");
    const req = {
      user: {
        role: "ADMIN"
      }
    };
    const next = jest.fn();

    middleware(req, {}, next);

    expect(next).toHaveBeenCalledWith();
  });

  it("rejects a user without the required role", () => {
    const middleware = authorize("ADMIN");
    const req = {
      user: {
        role: "TECHNICIAN"
      }
    };
    const next = jest.fn();

    middleware(req, {}, next);

    expect(next).toHaveBeenCalledWith(
      expect.objectContaining({
        statusCode: 403,
        message: "Insufficient permissions."
      })
    );
  });
});
