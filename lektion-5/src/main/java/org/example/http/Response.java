package org.example.http;

public class Response {
    private final boolean success;
    private final Object data;

    private Response(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public static Response ok(Object data) {
        return new Response(true, data);
    }

    public static Response error(Object errorData) {
        return new Response(false, errorData);
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Response{success=" + success + ", data=" + data + "}";
    }
}
