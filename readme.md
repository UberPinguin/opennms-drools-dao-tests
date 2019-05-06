# OpenNMS Correlator DAO Test

This is a test harness whose purpose is to show that
the OpenNMS NodeDao accessed by JUnit is not consistent
with the one accessed by the Drools KieSessions that are
started by the unit testing project.

The Drools KieSessions are initialized using the
CorrelationRulesTestCase and MockCorrelator classes.
The rules and Spring contexts are in
src/test/opennms-home/etc/drools-engine.d/dao-test-engine.
The tests are in src/test/java/org/opennms/drools/dao/test/DroolsNodeDaoTest.java.

Each test prepares an Event object to be correlated,
and another with the property changes that should be effected
by the Drools rules.

One test does a simple lookup using an OnmsNode object's nodeLabel property.

Three of the tests update an existing OnmsNode object,
verify that the updated properties of that object are
available through the DAO, then correlate an event that
should be modified using the updated properties of the OnmsNode
object. Each one uses a slightly different method of DAO access.

One test creates a wholly new node, saves it using the DAO, then correlates
an event that should be modified using the properties of that OnmsNode.

