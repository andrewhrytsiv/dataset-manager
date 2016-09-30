package com.datasets.validators;

public abstract class Validator<Model> {
	
	public abstract boolean validate(Model data);
}
