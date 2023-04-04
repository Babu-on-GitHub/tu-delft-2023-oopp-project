package client.utils;

import jakarta.ws.rs.core.GenericType;
import javafx.util.Pair;

import java.util.HashSet;

public class TestServerUtils extends ServerUtils {

    private HashSet<Pair<String, String>> records;

    public TestServerUtils() {
        super();
        setSocketUtils(new TestSocketUtils());
        records = new HashSet<>();
    }

    private void save(String request, String endpoint) {
        records.add(new Pair<>(request, endpoint));
    }

    public boolean wasMade(String request, String endpoint) {
        return records.contains(new Pair<>(request, endpoint));
    }

    @Override
    protected  <T> T put(String endpoint, Object body, GenericType<T> type) {
        save("put", endpoint);
        return (T)body;
    }

    @Override
    protected <T> T post(String endpoint, Object body, GenericType<T> type) {
        save("post", endpoint);
        return (T)body;
    }

    @Override
    protected  <T> T get(String endpoint, GenericType<T> type) {
        save("get", endpoint);
        return null;
    }

    @Override
    protected  <T> T delete(String endpoint, GenericType<T> type) {
        save("delete", endpoint);
        return null;
    }
}
