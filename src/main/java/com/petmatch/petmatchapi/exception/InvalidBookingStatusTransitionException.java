/*
 * Copyright 2026 by HireRight, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HireRight, Inc. Use is subject to license terms.
 *
 * History:
 *  K.Sizova   2026-05-07    Created
 */
package com.petmatch.petmatchapi.exception;

public class InvalidBookingStatusTransitionException extends RuntimeException
{
	public InvalidBookingStatusTransitionException(String message)
	{
		super(message);
	}
}
