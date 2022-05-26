package com.online.scheduling.schedule.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.time.Year;
import java.util.List;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannedYear {
    private Year year;
    @ManyToMany
    private List<PlannedMonthContainer> plannedMonths;
}
