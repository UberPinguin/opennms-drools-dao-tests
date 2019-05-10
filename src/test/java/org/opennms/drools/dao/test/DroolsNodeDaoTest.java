package org.opennms.drools.dao.test;

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
import org.opennms.netmgt.xml.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@JUnitTemporaryDatabase(dirtiesContext=true, tempDbClass=MockDatabase.class)
public class DroolsNodeDaoTest extends CorrelationRulesTestCase {

    @Autowired
    private ServiceRegistry m_serviceRegistry;

    @Autowired
    private NodeDao m_nodeDao;

    @Autowired
    private NodeDaoMockDataPopulator m_nodeDaoPopulator;

    private DroolsCorrelationEngine m_engine_cloud;
    private DroolsCorrelationEngine m_engine_stream;

    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    protected ServiceRegistry getServiceRegistry() {
        return m_serviceRegistry;
    }

    @Before
    @SuppressWarnings("Duplicates")
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

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost("unit-test-foreign-id-new-node-cloud").addParam("findByForeignId","true")
                .addParam("foreignSource","unit-test-foreign-source").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(9L);

        OnmsNode nodeFoundByDao = m_nodeDao.findByForeignId("unit-test-foreign-source","unit-test-foreign-id-new-node-cloud");
        assertEquals(9L,(long)nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_cloud.correlate(event);
        verify(m_engine_cloud);
    }

    @Test
    @Transactional
    public void testIdentifyNodeByForeignIdWithDatabasePopulatorDaoCloudMode() {
        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost("unit-test-foreign-id-new-node-cloud").addParam("findByForeignId","true")
                .addParam("foreignSource","unit-test-foreign-source").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(9L);

        OnmsNode nodeFoundByDao = m_nodeDao.findByForeignId("unit-test-foreign-source","unit-test-foreign-id-new-node-cloud");
        assertEquals(9L,(long)nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_cloud.correlate(event);
        verify(m_engine_cloud);
    }

    @Test
    @Transactional
    public void testIdentifyNodeByForeignIdWithKieSessionDaoCloudMode() {

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost("unit-test-foreign-id-new-node-cloud").addParam("findByForeignId","true")
                .addParam("foreignSource","unit-test-foreign-source").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(9L);

        OnmsNode nodeFoundByDao = m_nodeDao.findByForeignId("unit-test-foreign-source","unit-test-foreign-id-new-node-cloud");
        assertEquals(9L,(long)nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_cloud.correlate(event);
        verify(m_engine_cloud);
    }

    @Test
    @Transactional
    public void testCreateNewOnmsNodeAndCorrelateEventWithItCloudMode() {

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost("unit-test-foreign-id-new-node-cloud").addParam("findByForeignId","true")
                .addParam("foreignSource","unit-test-foreign-source").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(9L);
        NodeDao sessionNodeDao = (NodeDao)m_engine_cloud.getKieSession().getGlobal("nodeDao");

        OnmsNode nodeFoundByDao = sessionNodeDao
                .findByForeignId("unit-test-foreign-source","unit-test-foreign-id-new-node-cloud");
        assertEquals(9,(long)nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_cloud.correlate(event);
        verify(m_engine_cloud);
    }

    @Test
    @Transactional
    public void testIdentifyNodeByLabelStreamMode() throws InterruptedException {
        OnmsNode node = m_nodeDao.get(1);
        System.err.println(node.toString());

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

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByForeignId","true")
                .addParam("foreignSource","import:").getEvent();
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
    public void testIdentifyNodeByForeignIdWithDatabasePopulatorDaoStreamMode() throws InterruptedException {
        OnmsNode node = m_nodeDao.get(3);

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByForeignId","true")
                .addParam("foreignSource","import:").getEvent();
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
    public void testIdentifyNodeByForeignIdWithKieSessionDaoStreamMode() throws InterruptedException {
        OnmsNode node = m_nodeDao.get(4);

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost(node.getLabel()).addParam("findByForeignId","true")
                .addParam("foreignSource","import:").getEvent();
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

        Event event = new EventBuilder("uei.opennms.org/test/droolsDaoTestEvent","tests")
                .setNodeid(0L).setHost("unit-test-foreign-id-new-node-stream").addParam("findByForeignId","true")
                .addParam("foreignSource","unit-test-foreign-source").getEvent();
        Event anticipated = JaxbUtils.unmarshal(Event.class,JaxbUtils.marshal(event));
        anticipated.setNodeid(10L);

        OnmsNode nodeFoundByDao = m_nodeDao.findByForeignId("unit-test-foreign-source","unit-test-foreign-id-new-node-stream");
        assertEquals(10L,(long)nodeFoundByDao.getId());

        m_anticipatedMemorySize = 0;
        anticipate(anticipated);
        m_engine_stream.correlate(event);
        Thread.sleep(200);
        verify(m_engine_stream);
    }
}
