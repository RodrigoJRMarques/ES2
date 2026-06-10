const usersController = require("../../src/controllers/usersController");
const userRepository = require("../../src/repositories/userRepository");

describe("usersController", () => {
  beforeEach(() => {
    jest.restoreAllMocks();
  });

  it("returns the authenticated user on me", async () => {
    const req = {
      user: {
        sub: "user-123"
      }
    };
    const res = {
      status: jest.fn().mockReturnThis(),
      json: jest.fn()
    };
    const next = jest.fn();

    jest.spyOn(userRepository, "findById").mockResolvedValue({
      id: "user-123",
      name: "Controller User",
      email: "controller@greenherb.pt",
      role: "MANAGER",
      createdAt: "2026-05-21T10:00:00.000Z",
      passwordHash: "secret"
    });

    await usersController.me(req, res, next);

    expect(res.status).toHaveBeenCalledWith(200);
    expect(res.json).toHaveBeenCalledWith({
      user: {
        id: "user-123",
        name: "Controller User",
        email: "controller@greenherb.pt",
        role: "MANAGER",
        createdAt: "2026-05-21T10:00:00.000Z"
      }
    });
    expect(next).not.toHaveBeenCalled();
  });

  it("forwards errors from me to the next middleware", async () => {
    const req = {
      user: {
        sub: "user-123"
      }
    };
    const res = {
      status: jest.fn().mockReturnThis(),
      json: jest.fn()
    };
    const next = jest.fn();
    const error = new Error("Repository failure");

    jest.spyOn(userRepository, "findById").mockRejectedValue(error);

    await usersController.me(req, res, next);

    expect(next).toHaveBeenCalledWith(error);
  });

  it("returns the user list on list", async () => {
    const req = {};
    const res = {
      status: jest.fn().mockReturnThis(),
      json: jest.fn()
    };
    const next = jest.fn();

    jest.spyOn(userRepository, "list").mockResolvedValue([
      {
        id: "user-1",
        name: "User One",
        email: "user-one@greenherb.pt",
        role: "ADMIN",
        createdAt: "2026-05-21T10:00:00.000Z"
      }
    ]);

    await usersController.list(req, res, next);

    expect(res.status).toHaveBeenCalledWith(200);
    expect(res.json).toHaveBeenCalledWith({
      users: [
        {
          id: "user-1",
          name: "User One",
          email: "user-one@greenherb.pt",
          role: "ADMIN",
          createdAt: "2026-05-21T10:00:00.000Z"
        }
      ]
    });
    expect(next).not.toHaveBeenCalled();
  });

  it("forwards errors from list to the next middleware", async () => {
    const req = {};
    const res = {
      status: jest.fn().mockReturnThis(),
      json: jest.fn()
    };
    const next = jest.fn();
    const error = new Error("List failure");

    jest.spyOn(userRepository, "list").mockRejectedValue(error);

    await usersController.list(req, res, next);

    expect(next).toHaveBeenCalledWith(error);
  });
});
