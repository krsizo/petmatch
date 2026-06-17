package com.petmatch.petmatchapi.dto.match;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchReason {
	private String criterion;
	private Integer score;
	private String message;
}
