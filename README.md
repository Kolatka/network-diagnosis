# Network-diagnosis

A desktop application that allows to diagnose networks to find faulty nodes. It uses MM* method developed by Maeng and Malek.

# What is MM* method
The MM * comparative method involves network diagnosis based on a set of samples comparison. There are always 3 sensors in each test. One comparator and two others, which are comparative pair. All three are comparative test. The comparator orders test execution and then it compares received data and saves the result in the global syndrome. If the comparator works correctly and the data from two sensors are the same, then the result is 0, otherwise 1. If the comparator is broken, it will enter a random value from the set {0,1}. After this, the syndromes are sent out between all sensors and then they are combined. The result of this combination is a global syndrome consisting of all possible combinations of comparative tests. The next step in this diagnostic cycle is a comprarison of global syndrome with the pattern of global syndrome, which is a matrix consisting of all combinations of broken sensors. The row in the pattern differs from the global syndrome in that it also has "2" values. This value is assigned when the comparator is a damaged node. During comparison, pairs with "2" are omitted. If no differences were found and only one matching row was found, it identifies which sensors are working incorrectly.


Below is an example of global pattern for a 5-node network, for m = 2 (maximum 2 faulty sensors). Column labels indicate a comparative test. The first value is a comparator, and the other two are a comparative pair. The labels of the rows tells which sensors are broken in which row. The first line is d0, or zero syndrome for all properly functioning sensors.
![Example pattern](https://i.imgur.com/zonnr0z.png)

# How to use this app
The main program window consists of 4 parts.

![Main Windows](https://i.imgur.com/LkrNbye.png)

* The first one, located on the top left consists of a drop-down list that allows you to choose one of several implemented networks. And the buttons below allow to manage the simulation. You can manually start, diagnose, interrupt or terminate the simulation.
* The next part allows to generate a syndrome pattern for a given m and check the network's diagnosability.
* On the right, at the top, there is a panel that allows manual repair or damage specific sensors.
* The largest area of this window is a panel in which contains a graph of the network selected by the user.


# How to run this code
To run this code you need Java 8 and also 2 additional libraries:
* JavaFX
* GraphStream 1.3 (gs-algo-1.3.jar, gs-core-1.3.jar & gs-ui-1.3.jar)
(They can be imported with Maven)

JavaFX was used for the gui and GraphStream is really cool library that allows to create various graphs.

