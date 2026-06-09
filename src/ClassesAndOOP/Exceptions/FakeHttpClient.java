package ClassesAndOOP.Exceptions;

public class FakeHttpClient implements AutoCloseable {

    public FakeHttpClient() {
        System.out.println("Client opened");
    }

    public String call() {
        return "200 OK";
    }

    @Override
    public void close() throws Exception {
        System.out.println("Client closed");
    }
}
