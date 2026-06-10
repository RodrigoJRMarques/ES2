const jwt = require("jsonwebtoken");
const tokenService = require("../../src/services/tokenService");
const { jwt: jwtConfig } = require("../../src/config/env");

describe("tokenService", () => {
  const user = {
    id: "user-123",
    email: "token@greenherb.pt",
    role: "MANAGER"
  };

  beforeEach(() => {
    tokenService.clearRefreshTokens();
  });

  it("issues access and refresh tokens with the expected payload", () => {
    const result = tokenService.issueTokens(user);

    expect(result.accessToken).toEqual(expect.any(String));
    expect(result.refreshToken).toEqual(expect.any(String));

    const accessPayload = jwt.verify(result.accessToken, jwtConfig.accessSecret);
    const refreshPayload = jwt.verify(
      result.refreshToken,
      jwtConfig.refreshSecret
    );

    expect(accessPayload.sub).toBe(user.id);
    expect(accessPayload.email).toBe(user.email);
    expect(accessPayload.role).toBe(user.role);
    expect(refreshPayload.sub).toBe(user.id);
    expect(refreshPayload.jti).toEqual(expect.any(String));
  });

  it("rotates a valid refresh token into a new token pair", () => {
    const firstTokens = tokenService.issueTokens(user);
    const rotatedTokens = tokenService.rotateRefreshToken(firstTokens.refreshToken);

    expect(rotatedTokens).not.toBeNull();
    expect(rotatedTokens.accessToken).toEqual(expect.any(String));
    expect(rotatedTokens.refreshToken).toEqual(expect.any(String));
    expect(rotatedTokens.refreshToken).not.toBe(firstTokens.refreshToken);
  });

  it("returns null when trying to reuse a rotated refresh token", () => {
    const firstTokens = tokenService.issueTokens(user);

    tokenService.rotateRefreshToken(firstTokens.refreshToken);
    const reusedResult = tokenService.rotateRefreshToken(firstTokens.refreshToken);

    expect(reusedResult).toBeNull();
  });

  it("verifies a valid access token", () => {
    const tokens = tokenService.issueTokens(user);
    const payload = tokenService.verifyAccessToken(tokens.accessToken);

    expect(payload.sub).toBe(user.id);
    expect(payload.email).toBe(user.email);
    expect(payload.role).toBe(user.role);
  });

  it("clears refresh tokens so that old refresh tokens stop being valid", () => {
    const tokens = tokenService.issueTokens(user);

    tokenService.clearRefreshTokens();

    expect(tokenService.rotateRefreshToken(tokens.refreshToken)).toBeNull();
  });
});
