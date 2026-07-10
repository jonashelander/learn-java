package ClassesAndOOP.Generics;


// 4. EXTENDING A GENERIC CLASS — this is what you will actually do on the job.
//
// The base class AbstractProviderHandler<C extends ProviderConfig> is already written (exercise 3).
// Now you create a concrete provider by extending it and filling in C with your specific config type.
// This is exactly the pattern used in real provider codebases — the company writes the base class,
// you extend it for your provider.
//
// Create a class KlarnaHandler that extends AbstractProviderHandler<KlarnaConfig>.
//   Note: by writing <KlarnaConfig> here, you are filling in C — so the inherited field
//         "config" is now typed as KlarnaConfig, not just ProviderConfig.
//   - constructor takes a KlarnaConfig and calls super(config)
//   - implements callApi to return: "Klarna API [" + config.apiKey + "] charged " + amount
public class KlarnaHandler extends AbstractProviderHandler<KlarnaConfig>{

    public KlarnaHandler(KlarnaConfig config) {
        super(config);
    }

    @Override
    String callApi(double amount) {
        return "Klarna API [" + config.apikey + "] charged " + amount;
    }

}
