package org.opennms.drools.dao.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.opennms.core.soa.ServiceRegistry;
import org.opennms.core.spring.BeanUtils;
import org.opennms.core.test.db.MockDatabase;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.core.xml.JaxbUtils;
import org.opennms.netmgt.correlation.drools.DroolsCorrelationEngine;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.monitoringLocations.OnmsMonitoringLocation;
import org.opennms.netmgt.xml.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@JUnitTemporaryDatabase(dirtiesContext=false, tempDbClass=MockDatabase.class)
public class DroolsNodeDaoTest extends CorrelationRulesTestCase {

    @Autowired
    private ServiceRegistry m_serviceRegistry;

    @Autowired
    private NodeDao m_nodeDao;

    @Autowired
    private NodeDaoMockDataPopulator m_nodeDaoPopulator;

    private static boolean m_populated = false;
    private DroolsCorrelationEngine m_engine_cloud;
    private DroolsCorrelationEngine m_engine_stream;

    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    protected ServiceRegistry getServiceRegistry() {
        return m_serviceRegistry;
    }

    @Before
    public void setUp() {
        getAnticipator().reset();
        m_engine_cloud = findEngineByName("dao-test-cloud");
        m_engine_stream = findEngineByName("dao-test-stream");
        m_nodeDaoPopulator.setUpMock();
    }

    @Test
    @Transactional
    public void testIdentifyNodeByLabelCloudMode() {
        OnmsNode node = m_nodeDao.get(1);

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByNodeLabel","true").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(node.getId().longValue());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_cloud.correlate(event);
        verify(m_engine_cloud);
    }

    @Test
    @Transactional
    public void testIdentifyNodeByForeignIdWithSimpleDaoCloudMode() {
        OnmsNode node = m_nodeDao.get(2);
        node.setForeignSource("unit-test-foreign-source");
        node.setForeignId(node.getLabel());
        m_nodeDao.saveOrUpdate(node);
        m_nodeDao.flush();

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByForeignId","true").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(node.getId().longValue());

        OnmsNode nodeFoundByDao = m_nodeDao.findByForeignId("unit-test-foreign-source",node.getLabel());
        assertEquals(node.getId(),nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_cloud.correlate(event);
        verify(m_engine_cloud);
    }

    @Test
    @Transactional
    public void testIdentifyNodeByForeignIdWithDatabasePopulatorDaoCloudMode() {
        OnmsNode node = m_nodeDao.get(3);
        node.setForeignSource("unit-test-foreign-source");
        node.setForeignId(node.getLabel());
        m_nodeDaoPopulator.getNodeDao().saveOrUpdate(node);
        m_nodeDaoPopulator.getNodeDao().flush();

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByForeignId","true").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(node.getId().longValue());

        OnmsNode nodeFoundByDao = m_nodeDao.findByForeignId("unit-test-foreign-source",node.getLabel());
        assertEquals(node.getId(),nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_cloud.correlate(event);
        verify(m_engine_cloud);
    }

    @Test
    @Transactional
    public void testIdentifyNodeByForeignIdWithKieSessionDaoCloudMode() {
        OnmsNode node = m_nodeDao.get(4);
        node.setForeignSource("unit-test-foreign-source");
        node.setForeignId(node.getLabel());
        NodeDao sessionNodeDao = (NodeDao)m_engine_cloud.getKieSession().getGlobal("nodeDao");
        sessionNodeDao.saveOrUpdate(node);
        sessionNodeDao.flush();

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByForeignId","true").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(node.getId().longValue());

        OnmsNode nodeFoundByDao = m_nodeDao.findByForeignId("unit-test-foreign-source",node.getLabel());
        assertEquals(node.getId(),nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_cloud.correlate(event);
        verify(m_engine_cloud);
    }

    @Test
    @Transactional
    public void testCreateNewOnmsNodeAndCorrelateEventWithItCloudMode() {
        OnmsNode node = new OnmsNode(new OnmsMonitoringLocation(),
                                     "unit-test-foreign-id-new-node-cloud");
        node.setForeignSource("unit-test-foreign-source");
        node.setForeignId("unit-test-foreign-id-new-node-cloud");
        NodeDao sessionNodeDao = (NodeDao)m_engine_cloud.getKieSession().getGlobal("nodeDao");
        sessionNodeDao.saveOrUpdate(node);
        sessionNodeDao.flush();

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByForeignId","true").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(node.getId().longValue());

        OnmsNode nodeFoundByDao = m_nodeDao.findByForeignId("unit-test-foreign-source","unit-test-foreign-id-new-node" +
                "-cloud");
        assertEquals(node.getId(),nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_cloud.correlate(event);
        verify(m_engine_cloud);
    }

	@Test
	public void mooCow() {
		m_nodeDao.findAll().forEach(n -> {
		    String nodeId = n.getNodeId();
		    String nodeLabel = n.getLabel();
		    String foreignSource = n.getForeignSource();
		    String foreignId = n.getForeignId();
		    System.err.println("Node: "+nodeId+" Label: "+nodeLabel+" foreignSource: "+foreignSource+" foreignId: "+foreignId);
        });
	}

	@Test
	public void cowMoo() {
        OnmsNode node = m_nodeDao.get(1);
        node.setLabel("modifiedNode1");
        m_nodeDao.saveOrUpdate(node);
        m_nodeDao.flush();
	}

    @Test
    @Transactional
    public void testIdentifyNodeByLabelStreamMode() throws InterruptedException {
        OnmsNode node = m_nodeDao.get(1);

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByNodeLabel","true").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(node.getId().longValue());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_stream.correlate(event);
        Thread.sleep(200);
        verify(m_engine_stream);
    }

    @Test
    @Transactional
    public void testIdentifyNodeByForeignIdWithSimpleDaoStreamMode() throws InterruptedException {
        OnmsNode node = m_nodeDao.get(2);
        node.setForeignSource("unit-test-foreign-source");
        node.setForeignId(node.getLabel());
        m_nodeDao.saveOrUpdate(node);
        m_nodeDao.flush();

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByForeignId","true").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(node.getId().longValue());

        OnmsNode nodeFoundByDao = m_nodeDao.findByForeignId("unit-test-foreign-source",node.getLabel());
        assertEquals(node.getId(),nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_stream.correlate(event);
        Thread.sleep(200);
        verify(m_engine_stream);
    }

    @Test
    @Transactional
    public void testIdentifyNodeByForeignIdWithDatabasePopulatorDaoStreamMode() throws InterruptedException {
        OnmsNode node = m_nodeDao.get(3);
        node.setForeignSource("unit-test-foreign-source");
        node.setForeignId(node.getLabel());
        m_nodeDaoPopulator.getNodeDao().saveOrUpdate(node);
        m_nodeDaoPopulator.getNodeDao().flush();

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByForeignId","true").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(node.getId().longValue());

        OnmsNode nodeFoundByDao = m_nodeDao.findByForeignId("unit-test-foreign-source",node.getLabel());
        assertEquals(node.getId(),nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_stream.correlate(event);
        Thread.sleep(200);
        verify(m_engine_stream);
    }

    @Test
    @Transactional
    public void testIdentifyNodeByForeignIdWithKieSessionDaoStreamMode() throws InterruptedException {
        OnmsNode node = m_nodeDao.get(4);


        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByForeignId","true").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(node.getId().longValue());

        OnmsNode nodeFoundByDao = m_nodeDao.findByForeignId("import:",node.getLabel());
        assertEquals(node.getId(),nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_stream.correlate(event);
        Thread.sleep(200);
        verify(m_engine_stream);
    }

    @Test
    @Transactional
    public void testCreateNewOnmsNodeAndCorrelateEventWithItStreamMode() throws InterruptedException {
        OnmsNode node = new OnmsNode(new OnmsMonitoringLocation(),
                                     "unit-test-foreign-id-new-node-stream");
        node.setForeignSource("unit-test-foreign-source");
        node.setForeignId("unit-test-foreign-id-new-node-stream");
        NodeDao sessionNodeDao = (NodeDao)m_engine_stream.getKieSession().getGlobal("nodeDao");
        sessionNodeDao.saveOrUpdate(node);
        sessionNodeDao.flush();

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByForeignId","true").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(node.getId().longValue());

        OnmsNode nodeFoundByDao = m_nodeDao.findByForeignId("unit-test-foreign-source","unit-test-foreign-id-new-node" +
                "-stream");
        assertEquals(node.getId(),nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_stream.correlate(event);
        Thread.sleep(200);
        verify(m_engine_stream);
    }
}
