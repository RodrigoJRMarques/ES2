function errorHandler(error, _req, res, _next) {
  if (error instanceof SyntaxError && error.status === 400 && "body" in error) {
    return res.status(400).json({
      error: {
        message: "Malformed JSON payload.",
        statusCode: 400
      }
    });
  }

  const statusCode = error.statusCode || 500;
  const message =
    statusCode === 500 ? "Internal server error." : error.message;

  return res.status(statusCode).json({
    error: {
      message,
      statusCode
    }
  });
}

module.exports = errorHandler;
