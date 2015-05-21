package com.test.crappy;

import com.test.defaultmethods.DefaultInterface;
import com.test.defaultmethods.DefaultableInterface;
import com.test.defaultmethods.ImplementingClass;
import com.test.defaultmethods.OverridingClass;
import com.test.defaultmethods.Vehicle;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Created by Matija Vižintin
 * Date: 08. 05. 2015
 * Time: 14.13
 */
public class DefaultMethodTest {

    @Test
    public void test1() throws Exception {
        for (Method method : DefaultInterface.class.getMethods()) {
            System.out.println("Testing implementing class.");
            method.invoke(new ImplementingClass());

            System.out.println("Testing overriding class");
            method.invoke(new OverridingClass());

            System.out.println();
        }
    }

    @Test
    public void test2() {
        DefaultInterface di = DefaultableInterface.createStatic(ImplementingClass::new);
        di.method1();

        di = DefaultableInterface.createStatic(OverridingClass::new);
        di.method1();
    }

    @Test
    public void test3() {
        Vehicle implementedVehicle = new Vehicle() {
            @Override public void car() {
                System.out.println("Hello I'm a car.");
            }
        };
        implementedVehicle.car();
        implementedVehicle.bus();

        Vehicle implementedVehicle1 = () -> System.out.println("Hello I'm a car.");

        // print
        implementedVehicle1.car();
        implementedVehicle1.bus();

        // static
        Vehicle.plane();
    }
}
