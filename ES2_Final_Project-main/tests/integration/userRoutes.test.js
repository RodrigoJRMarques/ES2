const request = require("supertest");
const app = require("../../src/app");
const userRepository = require("../../src/repositories/userRepository");
const tokenService = require("../../src/services/tokenService");

describe("user routes integration", () => {
  beforeEach(async () => {
    await userRepository.clear();
    tokenService.clearRefreshTokens();
  });

  async function registerUser(user) {
    return request(app).post("/auth/register").send(user);
  }

  it("allows authenticated access to /users/me with a valid bearer token", async () => {
    const registerResponse = await registerUser({
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

  it("rejects protected routes without a bearer token", async () => {
    const response = await request(app).get("/users/me");

    expect(response.statusCode).toBe(401);
    expect(response.body.error.message).toBe("Bearer token is required.");
  });

  it("rejects protected routes with the wrong authorization scheme", async () => {
    const response = await request(app)
      .get("/users/me")
      .set("Authorization", "Basic abc123");

    expect(response.statusCode).toBe(401);
    expect(response.body.error.message).toBe("Bearer token is required.");
  });

  it("rejects an invalid JWT bearer token", async () => {
    const response = await request(app)
      .get("/users/me")
      .set("Authorization", "Bearer invalid.jwt.token");

    expect(response.statusCode).toBe(401);
    expect(response.body.error.message).toBe(
      "Access token is invalid or expired."
    );
  });

  it("returns the user list for an authenticated admin", async () => {
    const adminResponse = await registerUser({
      name: "Admin User",
      email: "admin-list@greenherb.pt",
      password: "StrongPass1",
      role: "ADMIN"
    });

    await registerUser({
      name: "Tech User",
      email: "tech-list@greenherb.pt",
      password: "StrongPass1",
      role: "TECHNICIAN"
    });

    const response = await request(app)
      .get("/users")
      .set("Authorization", `Bearer ${adminResponse.body.accessToken}`);

    expect(response.statusCode).toBe(200);
    expect(response.body.users).toHaveLength(2);
    expect(response.body.users.map((user) => user.email)).toEqual(
      expect.arrayContaining(["admin-list@greenherb.pt", "tech-list@greenherb.pt"])
    );
  });

  it("rejects /users for an authenticated non-admin user", async () => {
    const userResponse = await registerUser({
      name: "Manager User",
      email: "manager-list@greenherb.pt",
      password: "StrongPass1",
      role: "MANAGER"
    });

    const response = await request(app)
      .get("/users")
      .set("Authorization", `Bearer ${userResponse.body.accessToken}`);

    expect(response.statusCode).toBe(403);
    expect(response.body.error.message).toBe("Insufficient permissions.");
  });
});
