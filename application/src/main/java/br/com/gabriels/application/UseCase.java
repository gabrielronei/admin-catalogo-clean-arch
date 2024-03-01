package br.com.gabriels.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN in);
}
