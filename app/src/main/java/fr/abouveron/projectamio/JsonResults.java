package fr.abouveron.projectamio;

import java.util.ArrayList;
import java.util.List;

import fr.abouveron.projectamio.JsonModel.JsonResult;

public class JsonResults {
    private static List<JsonResult> jsonResults = new ArrayList<>();

    public static List<JsonResult> getJsonResults() {
        return jsonResults;
    }

    public static void addJsonResult(JsonResult jsonResult) {
        jsonResults.add(jsonResult);
    }
}
