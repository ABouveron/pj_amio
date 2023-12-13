package fr.abouveron.projectamio.Utilities;

import java.util.ArrayList;
import java.util.List;

public class JsonResultsManager {
    private static List<JsonResults> jsonResults = new ArrayList<>();

    public static List<JsonResults> getJsonResults() {
        return jsonResults;
    }

    public static void addJsonResult(JsonResults jsonResults) {
        JsonResultsManager.jsonResults.add(jsonResults);
    }
}
