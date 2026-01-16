package com.habit.tracker.exception;

public class ResourceAlreadyExistsException extends RuntimeException{
	public ResourceAlreadyExistsException(String message) {
		super(message);
	}
}
