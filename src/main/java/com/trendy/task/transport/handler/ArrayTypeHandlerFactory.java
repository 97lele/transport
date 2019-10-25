package com.trendy.task.transport.handler;

import java.util.function.Function;

/**
 * @author lulu
 * @Date 2019/10/25 22:33
 */
public interface ArrayTypeHandlerFactory {

     class IntegerArrayTypeHandler extends AbstractArrayTypeHandler<Integer>{
        public IntegerArrayTypeHandler() {
            super(Integer::parseInt);
        }
    }
     class DoubleArrayTypeHandler extends AbstractArrayTypeHandler<Double>{
        public DoubleArrayTypeHandler(){
            super(Double::parseDouble);
        }
    }
     class LongArrayTypeHandler extends AbstractArrayTypeHandler<Long>{
        public LongArrayTypeHandler(){
            super(Long::parseLong);
        }
    }
     class StringArrayTypeHandler extends AbstractArrayTypeHandler<String>{
        public StringArrayTypeHandler(){
            super(null);
        }
    }



}


