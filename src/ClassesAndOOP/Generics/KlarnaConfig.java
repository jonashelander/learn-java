package ClassesAndOOP.Generics;

// Step 2 — Create a class KlarnaConfig that extends ProviderConfig.
//           Constructor takes a String apiKey and calls super(apiKey).
//           This represents Klarna's specific config — in reality it might have more fields,
//           but apiKey is enough for this exercise.
public class KlarnaConfig extends ProviderConfig{

    public KlarnaConfig(String apikey) {
        super(apikey);
    }
}
