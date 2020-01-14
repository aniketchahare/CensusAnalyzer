package CSVBuilderFactory;

import OpenCSVBuilder.OpenCSVBuilder;
import censusanalyser.ICSVBuilder;

public class CSVBuilderFactory {
    public static ICSVBuilder createCSVBuilder() {
        return new OpenCSVBuilder();
    }
}
