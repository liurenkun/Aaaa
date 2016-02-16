package com.openIOT.neuLife;

/**
 * Created by hyin on 2/11/16.
 */
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
/*
 * A holder for lambda functions
 */
public interface MyInterface {

    /**
     * Invoke lambda function "echo". The function name is the method name
     */
    @LambdaFunction
    ResponseClass Auth(RequestClass request);


}