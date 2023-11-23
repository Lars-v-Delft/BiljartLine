package com.biljartline.billiardsapi.competition;

import java.util.HashSet;
import java.util.Objects;

public class Compare {

    public static boolean equalsCompetitionDTO(CompetitionDTO a, CompetitionDTO b) {
        if (a == b) return true;
        return a.getId() == b.getId() &&
                a.getFederationId() == b.getFederationId() &&
                a.isPublished() == b.isPublished() &&
                Objects.equals(a.getName(), b.getName()) &&
                Objects.equals(a.getGameType(), b.getGameType()) &&
                Objects.equals(a.getStartDate(), b.getStartDate()) &&
                Objects.equals(a.getEndDate(), b.getEndDate()) &&
                a.getTeamIds().size() == b.getTeamIds().size() &&
                new HashSet<>(a.getTeamIds()).equals(new HashSet<>(b.getTeamIds())); // Ignore order
    }
}
