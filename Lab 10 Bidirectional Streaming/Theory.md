# gRPC Bidirectional Streaming — Concepts & Theory

This document outlines the core concepts, architecture, and implementation mechanics covered in **Lab 10: Bidirectional Streaming**.

---

## 1. Introduction to gRPC Streaming Models

gRPC supports four distinct communication paradigms over HTTP/2:

1. **Unary RPC**: Traditional request-response model (one request, one response).
2. **Server Streaming RPC**: The client sends a single request, and the server returns a stream of multiple responses.
3. **Client Streaming RPC**: The client sends a stream of multiple requests, and the server returns a single response upon completion.
4. **Bidirectional Streaming RPC**: Both client and server independently send a stream of messages using read-write streams over a single connection.

---

## 2. Core Concepts of Bidirectional Streaming

### 2.1 Independent and Full-Duplex Streams
- In bidirectional streaming (`rpc CheckNumbers(stream NumberRequest) returns (stream NumberResponse);`), the two communication channels are **completely decoupled and full-duplex**.
- The client and server can read and write in whatever order they choose:
  - **Ping-Pong / Lockstep**: Server reads a request and immediately sends back a response (as implemented in this lab).
  - **Buffered / Batching**: Server waits to collect multiple requests before sending any responses.
  - **Completely Asynchronous**: Server streams responses independent of when client requests arrive (e.g., chat applications, real-time telemetry).

### 2.2 Underlying Transport: HTTP/2 Multiplexing
gRPC leverages **HTTP/2 frames and streams**:
- **Multiplexing**: Bidirectional communication happens over a single persistent TCP connection.
- **Frames (`DATA`, `HEADERS`, `RST_STREAM`)**: Individual messages are serialized Protocol Buffer payloads framed over HTTP/2 data frames.
- **Low Overhead**: Avoids continuous TCP handshake and HTTP header parsing overhead associated with repeated HTTP/1.1 calls.

---

## 3. Protocol Buffers (`.proto`) Definition

In [number.proto](file:///c:/Users/aryan/OneDrive/Desktop/S2%202026/CS324/CS324-Projects/Lab%2010%20Bidirectional%20Streaming/src/main/proto/number.proto), bidirectional streaming is indicated by the `stream` keyword on **both** the parameter and return type:

```protobuf
syntax = "proto3";

option java_package = "com.example.grpcstream";
option java_multiple_files = true;

service NumberService {
    // Both input and output use 'stream' keyword
    rpc CheckNumbers(stream NumberRequest) returns (stream NumberResponse);
}

message NumberRequest {
    int32 number = 1;
}

message NumberResponse {
    int32 number = 1;
    string result = 2;
}
```

---

## 4. Asynchronous Reactive Pattern: `StreamObserver<T>`

In Java gRPC, non-blocking asynchronous streaming relies on the `StreamObserver<T>` interface, following the Observer / Reactive Stream pattern:

| Method | Role / Description |
|---|---|
| `onNext(T value)` | Delivers the next message in the stream. Can be called zero or many times. |
| `onError(Throwable t)` | Notifies that an error occurred, terminating the stream prematurely. |
| `onCompleted()` | Signals normal stream completion. No further items will be sent. |

---

## 5. Implementation Analysis in Lab 10

### 5.1 Server-Side Mechanics ([NumberServer.java](file:///c:/Users/aryan/OneDrive/Desktop/S2%202026/CS324/CS324-Projects/Lab%2010%20Bidirectional%20Streaming/src/main/java/com/example/grpcstream/NumberServer.java))

```java
@Override
public StreamObserver<NumberRequest> checkNumbers(StreamObserver<NumberResponse> responseObserver) {
    return new StreamObserver<NumberRequest>() {
        // ...
    };
}
```

1. **Inverted Control Flow**:
   - The server method receives `StreamObserver<NumberResponse> responseObserver` for sending outgoing responses to the client.
   - It returns a `StreamObserver<NumberRequest>` to handle incoming requests from the client.
2. **Event Handling**:
   - `onNext(request)`: Triggered for every incoming number. The server inspects if the integer is odd or even, increments counters atomically (`AtomicInteger`), and calls `responseObserver.onNext(response)` immediately.
   - `onCompleted()`: Triggered when the client invokes `requestObserver.onCompleted()`. The server prints summary statistics and informs the client it is done via `responseObserver.onCompleted()`.

### 5.2 Client-Side Mechanics ([NumberClient.java](file:///c:/Users/aryan/OneDrive/Desktop/S2%202026/CS324/CS324-Projects/Lab%2010%20Bidirectional%20Streaming/src/main/java/com/example/grpcstream/NumberClient.java))

1. **Asynchronous Stub**:
   - Uses `NumberServiceGrpc.newStub(channel)` (non-blocking async stub) rather than a blocking stub (`newBlockingStub`), since blocking stubs do not support client-side or bidirectional streaming in Java.
2. **Initiating the Stream**:
   ```java
   StreamObserver<NumberRequest> requestObserver = stub.checkNumbers(responseObserver);
   ```
   The client passes a `responseObserver` to receive server responses asynchronously on a background listener thread and gets back `requestObserver` to push requests.
3. **High-Throughput Burst & Rate Control**:
   - Streams 10,000 numbers per second over 10 seconds (totaling 100,000 requests).
   - Demonstrates that high message volumes are handled with minimal latency over a single stream.
4. **Synchronization with `CountDownLatch`**:
   - Because response processing is asynchronous on another thread, the main thread awaits completion with `completed.await(30, TimeUnit.SECONDS)` after signaling `requestObserver.onCompleted()`.

---

## 6. Key Advantages & Use Cases

- **Real-Time Data Processing**: Sensor data streaming, financial ticker updates, log shipping.
- **Low Latency & High Throughput**: Amortizes connection setup costs; supports pipelining over HTTP/2.
- **Concurrent Dialogue**: Interactive protocols (e.g., chat applications, multiplayer gaming, collaborative editing).
