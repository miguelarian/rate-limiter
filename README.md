# Rate limiter with Redis

Fixed-window rate-limiter using Java Spring Boot and Redis.

This is a dummy API with a GET foo/bar endpoint to implement an HTTP request rate limiter.

## How does it work?

When the counter reaches a value of 10 requests in a fixed time window of 30 seconds, a 429 HTTP response is returned.

## Implementation details

A Java Servlet Filter is used to increase a Redis value as counter.  The filter uses a Redis Template instance connected to a local Redis server instance using a Jedis connection.

