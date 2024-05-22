// IExampleInterface.aidl
package com.s4ltf1sh.aidl_motherapp;
import com.s4ltf1sh.aidl_motherapp.model.MyMessage;

// Declare any non-default types here with import statements

interface IExampleInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    String getMessageFromMother();

    void sendMessageToMother(String message);

    void sendMessageObjToMother(in MyMessage message);
}