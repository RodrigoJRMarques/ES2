const userRepository = require("../../src/repositories/userRepository");

describe("userRepository", () => {
  beforeEach(async () => {
    await userRepository.clear();
  });

  it("creates a user and normalizes the email", async () => {
    const user = await userRepository.create({
      name: "Repo User",
      email: "Repo.User@GreenHerb.PT",
      passwordHash: "hashed-password",
      role: "TECHNICIAN"
    });

    expect(user.email).toBe("repo.user@greenherb.pt");
    expect(user.id).toEqual(expect.any(String));
    expect(user.createdAt).toEqual(expect.any(String));
  });

  it("finds a user by email regardless of input case", async () => {
    await userRepository.create({
      name: "Repo User",
      email: "case-test@greenherb.pt",
      passwordHash: "hashed-password",
      role: "MANAGER"
    });

    const user = await userRepository.findByEmail("CASE-TEST@GREENHERB.PT");

    expect(user).not.toBeNull();
    expect(user.email).toBe("case-test@greenherb.pt");
    expect(user.passwordHash).toBe("hashed-password");
  });

  it("finds a user by id and returns null when the id does not exist", async () => {
    const createdUser = await userRepository.create({
      name: "Find By Id",
      email: "find-id@greenherb.pt",
      passwordHash: "hashed-password",
      role: "ADMIN"
    });

    const foundUser = await userRepository.findById(createdUser.id);
    const missingUser = await userRepository.findById("missing-id");

    expect(foundUser).not.toBeNull();
    expect(foundUser.email).toBe("find-id@greenherb.pt");
    expect(missingUser).toBeNull();
  });

  it("lists users in sanitized form without password hashes", async () => {
    await userRepository.create({
      name: "User One",
      email: "user-one@greenherb.pt",
      passwordHash: "hash-one",
      role: "TECHNICIAN"
    });
    await userRepository.create({
      name: "User Two",
      email: "user-two@greenherb.pt",
      passwordHash: "hash-two",
      role: "MANAGER"
    });

    const users = await userRepository.list();

    expect(users).toHaveLength(2);
    expect(users[0].passwordHash).toBeUndefined();
    expect(users[1].passwordHash).toBeUndefined();
  });

  it("clears all users from the repository", async () => {
    await userRepository.create({
      name: "To Clear",
      email: "clear@greenherb.pt",
      passwordHash: "hash-clear",
      role: "TECHNICIAN"
    });

    await userRepository.clear();

    const users = await userRepository.list();
    expect(users).toEqual([]);
  });
});
