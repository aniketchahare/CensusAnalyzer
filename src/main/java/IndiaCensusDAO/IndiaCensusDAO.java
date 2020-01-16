package IndiaCensusDAO;

import censusanalyser.IndiaCensusCSV;
import censusanalyser.IndiaStateCodeCSV;

public class IndiaCensusDAO {
    public String state;
    public int population;
    public int densityPerSqkm;
    public int areaInSqKm;

    public IndiaCensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        areaInSqKm = indiaCensusCSV.areaInSqKm;
        densityPerSqkm = indiaCensusCSV.densityPerSqKm;
        population = indiaCensusCSV.population;
    }

    public IndiaCensusDAO(IndiaStateCodeCSV stateCSV) {
        state = stateCSV.StateName;
    }

    @Override
    public String toString() {
        return "IndiaCensusDAO{" +
                "state='" + state + '\'' +
                ", population=" + population +
                ", densityPerSqkm=" + densityPerSqkm +
                ", areaInSqKm=" + areaInSqKm +
                '}';
    }
}
