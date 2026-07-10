package ClassesAndOOP.Generics;

// Step 1 — Create an abstract class ProviderConfig with one field: String apiKey.
//           Constructor takes a String apiKey and assigns it.
//           This is the base config that all provider configs will extend.
abstract public class ProviderConfig {
    String apikey;

    public ProviderConfig(String apikey) {
        this.apikey = apikey;
    }
}
