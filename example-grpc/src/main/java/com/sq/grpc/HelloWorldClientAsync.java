package com.sq.grpc;

import com.sq.grpc.proto.GreeterGrpc;
import com.sq.grpc.proto.HelloReply;
import com.sq.grpc.proto.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

/**
 * A simple client that requests a greeting from the {@link HelloWorldServer}.
 */
public class HelloWorldClientAsync {

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterStub stub;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public HelloWorldClientAsync(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext().build());
    }

    /** Construct client for accessing HelloWorld server using the existing channel. */
    HelloWorldClientAsync(ManagedChannel channel) {
        this.channel = channel;
        stub = GreeterGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Say hello to server. */
    public void greet(String name) {
        System.out.println("send => [" + name + "] ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;

        try {
            // response = stub.echo(request, response);
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: " + e.getStatus());
            return;
        }
        // System.out.println("recv <= " + response.getMessage());
    }

    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the greeting.
     */
    public static void main(String[] args) throws Exception {
        // Access a service running on the local machine on port 50051
        HelloWorldClientAsync client = new HelloWorldClientAsync("localhost", 50051);
        try {
            String user = "world";
            client.greet(user);
        } finally {
            client.shutdown();
        }
    }
}