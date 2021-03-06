package com.sq.json;

import static io.grpc.stub.ClientCalls.blockingUnaryCall;

import com.sq.grpc.proto.GreeterGrpc;
import com.sq.grpc.proto.HelloReply;
import com.sq.grpc.proto.HelloRequest;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.MethodDescriptor;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.AbstractStub;
import java.util.concurrent.TimeUnit;

/**
 * Advanced example of how to swap out the serialization logic. Normal users do not need to do this. This code is not
 * intended to be a production-ready implementation, since JSON encoding is slow. Additionally, JSON serialization as
 * implemented may be not resilient to malicious input.
 *
 * <p>
 * If you are considering implementing your own serialization logic, contact the grpc team at
 * https://groups.google.com/forum/#!forum/grpc-io
 */
public final class HelloJsonClient {

    private final ManagedChannel channel;
    private final HelloJsonStub blockingStub;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public HelloJsonClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        blockingStub = new HelloJsonStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Say hello to server. */
    public void greet(String name) {
        System.out.println("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed: " + e.getStatus());
            return;
        }
        System.out.println("Greeting: " + response.getMessage());
    }

    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the greeting.
     */
    public static void main(String[] args) throws Exception {
        // Access a service running on the local machine on port 50051
        HelloJsonClient client = new HelloJsonClient("localhost", 50051);
        try {
            String user = "world";
            client.greet(user);
        } finally {
            client.shutdown();
        }
    }

    static final class HelloJsonStub extends AbstractStub<HelloJsonStub> {

        static final MethodDescriptor<HelloRequest, HelloReply> METHOD_SAY_HELLO = GreeterGrpc.getSayHelloMethod()
                .toBuilder(JsonMarshaller.jsonMarshaller(HelloRequest.getDefaultInstance()),
                        JsonMarshaller.jsonMarshaller(HelloReply.getDefaultInstance()))
                .build();

        protected HelloJsonStub(Channel channel) {
            super(channel);
        }

        protected HelloJsonStub(Channel channel, CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected HelloJsonStub build(Channel channel, CallOptions callOptions) {
            return new HelloJsonStub(channel, callOptions);
        }

        public HelloReply sayHello(HelloRequest request) {
            return blockingUnaryCall(getChannel(), METHOD_SAY_HELLO, getCallOptions(), request);
        }
    }
}

