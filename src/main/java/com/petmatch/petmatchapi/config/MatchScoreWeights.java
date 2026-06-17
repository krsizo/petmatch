/*
 * Copyright 2026 by HireRight, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 *
 * History:
 *  K.Sizova   2026-05-06    Created
 */
package com.petmatch.petmatchapi.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MatchScoreWeights {
	private final int petType = 25;
	private final int gender = 10;
	private final int size = 15;
	private final int age = 15;
	private final int adoptionFee = 15;
	private final int distance = 15;
	private final int temperament = 5;
}
