Simple SMSC program

Requirements:
-Java 8
-Maven 3

Simple maven project which has tests in src/test/java and implementation in src/main/java.
There's a sample input file in src/test/resources which can be used as a run argument to the console application.

Implemented features:
-allow a number to subscribe/unsubsribe
-subscription/unsubscription to SMSC
-send a message to one or more other subscribers

Still in TODO:
-send a message to group (in the sample input file, it is commented out)
-send a broadcast (in the sample input file, it is commented out)