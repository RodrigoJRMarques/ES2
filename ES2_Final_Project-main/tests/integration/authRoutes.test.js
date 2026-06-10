const request = require("supertest");
const app = require("../../src/app");
const userRepository = require("../../src/repositories/userRepository");
const tokenService = require("../../src/services/tokenService");

describe("auth routes", () => {
  beforeEach(async () => {
    await userRepository.clear();
    tokenService.clearRefreshTokens();
  });

  it("returns API health information", async () => {
    const response = await request(app).get("/health");

    expect(response.statusCode).toBe(200);
    expect(response.body).toEqual({
      status: "ok",
      service: "greenherb-api"
    });
  });

  it("creates a user and allows authenticated access to /users/me", async () => {
    const registerResponse = await request(app).post("/auth/register").send({
      name: "API Admin",
      email: "api-admin@greenherb.pt",
      password: "StrongPass1",
      role: "ADMIN"
    });

    expect(registerResponse.statusCode).toBe(201);
    expect(registerResponse.body.user.email).toBe("api-admin@greenherb.pt");

    const meResponse = await request(app)
      .get("/users/me")
      .set(
        "Authorization",
        `Bearer ${registerResponse.body.accessToken}`
      );

    expect(meResponse.statusCode).toBe(200);
    expect(meResponse.body.user.role).toBe("ADMIN");
  });

  it("accepts a valid JWT bearer token on a protected route", async () => {
    const registerResponse = await request(app).post("/auth/register").send({
      name: "JWT User",
      email: "jwt-user@greenherb.pt",
      password: "StrongPass1",
      role: "TECHNICIAN"
    });

    const response = await request(app)
      .get("/users/me")
      .set("Authorization", `Bearer ${registerResponse.body.accessToken}`);

    expect(response.statusCode).toBe(200);
    expect(response.body.user.email).toBe("jwt-user@greenherb.pt");
  });

  it("rejects an invalid JWT bearer token on a protected route", async () => {
    const response = await request(app)
      .get("/users/me")
      .set("Authorization", "Bearer invalid.jwt.token");

    expect(response.statusCode).toBe(401);
    expect(response.body.error.message).toBe(
      "Access token is invalid or expired."
    );
  });

  it("rejects protected routes without a bearer token", async () => {
    const response = await request(app).get("/users/me");

    expect(response.statusCode).toBe(401);
    expect(response.body.error.message).toBe("Bearer token is required.");
  });

  it("returns 500 when an unexpected internal error occurs", async () => {
    const registerResponse = await request(app).post("/auth/register").send({
      name: "Error User",
      email: "error-user@greenherb.pt",
      password: "StrongPass1",
      role: "TECHNICIAN"
    });

    const findByIdSpy = jest
      .spyOn(userRepository, "findById")
      .mockRejectedValueOnce(new Error("Unexpected repository failure"));

    const response = await request(app)
      .get("/users/me")
      .set("Authorization", `Bearer ${registerResponse.body.accessToken}`);

    expect(response.statusCode).toBe(500);
    expect(response.body.error.message).toBe("Internal server error.");

    findByIdSpy.mockRestore();
  });

  it("rejects malformed JSON on registration", async () => {
    const response = await request(app)
      .post("/auth/register")
      .set("Content-Type", "application/json")
      .send('{"name":"Broken JSON"');

    expect(response.statusCode).toBe(400);
    expect(response.body.error.message).toBe("Malformed JSON payload.");
  });

  it("rejects login when the request body is missing credentials", async () => {
    const response = await request(app).post("/auth/login").send({});

    expect(response.statusCode).toBe(400);
    expect(response.body.error.message).toBe("Email and password are required.");
  });

  it("refreshes tokens with a valid refresh token and rejects token reuse", async () => {
    const registerResponse = await request(app).post("/auth/register").send({
      name: "Refresh User",
      email: "refresh-user@greenherb.pt",
      password: "StrongPass1",
      role: "MANAGER"
    });

    const refreshResponse = await request(app).post("/auth/refresh").send({
      refreshToken: registerResponse.body.refreshToken
    });

    expect(refreshResponse.statusCode).toBe(200);
    expect(refreshResponse.body.accessToken).toEqual(expect.any(String));
    expect(refreshResponse.body.refreshToken).toEqual(expect.any(String));
    expect(refreshResponse.body.refreshToken).not.toBe(
      registerResponse.body.refreshToken
    );

    const reusedTokenResponse = await request(app).post("/auth/refresh").send({
      refreshToken: registerResponse.body.refreshToken
    });

    expect(reusedTokenResponse.statusCode).toBe(401);
    expect(reusedTokenResponse.body.error.message).toBe(
      "Refresh token is invalid or expired."
    );
  });

  it("rejects refresh with an invalid token", async () => {
    const response = await request(app).post("/auth/refresh").send({
      refreshToken: "invalid.refresh.token"
    });

    expect(response.statusCode).toBe(401);
    expect(response.body.error.message).toBe(
      "Refresh token is invalid or expired."
    );
  });

  it("rejects unsupported HTTP methods on the authentication endpoints", async () => {
    const response = await request(app).get("/auth/login");

    expect(response.statusCode).toBe(404);
  });
});
