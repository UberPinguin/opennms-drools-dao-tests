package org.opennms.drools.dao.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.easymock.EasyMock;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.model.NetworkBuilder;
import org.opennms.netmgt.model.OnmsNode;
import org.springframework.beans.factory.annotation.Autowired;

public class NodeDaoMockDataPopulator {

    @Autowired
    private NodeDao m_nodeDao;

    private OnmsNode m_node1;
    private OnmsNode m_node2;
    private OnmsNode m_node3;
    private OnmsNode m_node4;
    private OnmsNode m_node5;
    private OnmsNode m_node6;
    private OnmsNode m_node7;
    private OnmsNode m_node8;
    private OnmsNode m_nodeCustomCloud;
    private OnmsNode m_nodeCustomStream;

    private List<OnmsNode> m_nodes;

    private void populateNodes() {
        m_nodes = new ArrayList<>();

        final NetworkBuilder builder = new NetworkBuilder();

        builder.addNode("node1").setForeignSource("imported:").setForeignId("1").setType(OnmsNode.NodeType.ACTIVE);
        final OnmsNode node1 = builder.getCurrentNode();
        setNode1(node1);

        builder.addNode("node2").setForeignSource("imported:").setForeignId("2").setType(OnmsNode.NodeType.ACTIVE);
        final OnmsNode node2 = builder.getCurrentNode();
        setNode2(node2);

        builder.addNode("node3").setForeignSource("imported:").setForeignId("3").setType(OnmsNode.NodeType.ACTIVE);
        final OnmsNode node3 = builder.getCurrentNode();
        setNode3(node3);

        builder.addNode("node4").setForeignSource("imported:").setForeignId("4").setType(OnmsNode.NodeType.ACTIVE);
        final OnmsNode node4 = builder.getCurrentNode();
        setNode4(node4);

        builder.addNode("node5").setForeignSource("imported:").setForeignId("5").setType(OnmsNode.NodeType.ACTIVE);
        final OnmsNode node5 = builder.getCurrentNode();
        setNode5(node5);

        builder.addNode("node6").setForeignSource("imported:").setForeignId("6").setType(OnmsNode.NodeType.ACTIVE);
        final OnmsNode node6 = builder.getCurrentNode();
        setNode6(node6);

        builder.addNode("node7").setForeignSource("imported:").setForeignId("7").setType(OnmsNode.NodeType.ACTIVE);
        final OnmsNode node7 = builder.getCurrentNode();
        setNode7(node7);

        builder.addNode("node8").setForeignSource("imported:").setForeignId("8").setType(OnmsNode.NodeType.ACTIVE);
        final OnmsNode node8 = builder.getCurrentNode();
        setNode8(node8);

        builder.addNode("unit-test-foreign-id-new-node-cloud").setForeignSource("unit-test-foreign-source")
                .setForeignId("unit-test-foreign-id-new-node-cloud").setType(OnmsNode.NodeType.ACTIVE);
        final OnmsNode nodeCustomCloud = builder.getCurrentNode();
        setNodeCustomCloud(nodeCustomCloud);

        builder.addNode("unit-test-foreign-id-new-node-stream").setForeignSource("unit-test-foreign-source")
                .setForeignId("unit-test-foreign-id-new-node-stream").setType(OnmsNode.NodeType.ACTIVE);
        final OnmsNode nodeCustomStream = builder.getCurrentNode();
        setNodeCustomStream(nodeCustomStream);

    }

    public NodeDao getNodeDao() {
        return m_nodeDao;
    }

    public void setNodeDao(final NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

    public void tearDown() {
        EasyMock.reset(m_nodeDao);
    }

    public List<OnmsNode> getOnmsNodes() {
        return m_nodes;
    }

    void setUpMock() {
        populateNodes();
        EasyMock.expect(m_nodeDao.save(m_node1)).andReturn(1).atLeastOnce();
        EasyMock.expect(m_nodeDao.save(m_node2)).andReturn(2).atLeastOnce();
        EasyMock.expect(m_nodeDao.save(m_node3)).andReturn(3).atLeastOnce();
        EasyMock.expect(m_nodeDao.save(m_node4)).andReturn(4).atLeastOnce();
        EasyMock.expect(m_nodeDao.save(m_node5)).andReturn(5).atLeastOnce();
        EasyMock.expect(m_nodeDao.save(m_node6)).andReturn(6).atLeastOnce();
        EasyMock.expect(m_nodeDao.save(m_node7)).andReturn(7).atLeastOnce();
        EasyMock.expect(m_nodeDao.save(m_node8)).andReturn(8).atLeastOnce();
        EasyMock.expect(m_nodeDao.save(m_nodeCustomCloud)).andReturn(9).atLeastOnce();
        EasyMock.expect(m_nodeDao.get(1)).andReturn(m_node1).anyTimes();
        EasyMock.expect(m_nodeDao.get(2)).andReturn(m_node2).anyTimes();
        EasyMock.expect(m_nodeDao.get(3)).andReturn(m_node3).anyTimes();
        EasyMock.expect(m_nodeDao.get(4)).andReturn(m_node4).anyTimes();
        EasyMock.expect(m_nodeDao.get(5)).andReturn(m_node5).anyTimes();
        EasyMock.expect(m_nodeDao.get(6)).andReturn(m_node6).anyTimes();
        EasyMock.expect(m_nodeDao.get(7)).andReturn(m_node7).anyTimes();
        EasyMock.expect(m_nodeDao.get(8)).andReturn(m_node8).anyTimes();
        EasyMock.expect(m_nodeDao.get(9)).andReturn(m_nodeCustomCloud).anyTimes();
        EasyMock.expect(m_nodeDao.get(10)).andReturn(m_nodeCustomStream).anyTimes();
        EasyMock.expect(m_nodeDao.findAll()).andReturn(m_nodes).anyTimes();
        EasyMock.expect(m_nodeDao.findByLabel("node1")).andReturn(Collections.singletonList(m_node1)).anyTimes();
        EasyMock.expect(m_nodeDao.findByLabel("node2")).andReturn(Collections.singletonList(m_node2)).anyTimes();
        EasyMock.expect(m_nodeDao.findByLabel("node3")).andReturn(Collections.singletonList(m_node3)).anyTimes();
        EasyMock.expect(m_nodeDao.findByLabel("node4")).andReturn(Collections.singletonList(m_node4)).anyTimes();
        EasyMock.expect(m_nodeDao.findByLabel("node5")).andReturn(Collections.singletonList(m_node5)).anyTimes();
        EasyMock.expect(m_nodeDao.findByLabel("node6")).andReturn(Collections.singletonList(m_node6)).anyTimes();
        EasyMock.expect(m_nodeDao.findByLabel("node7")).andReturn(Collections.singletonList(m_node7)).anyTimes();
        EasyMock.expect(m_nodeDao.findByLabel("node8")).andReturn(Collections.singletonList(m_node8)).anyTimes();
        EasyMock.expect(m_nodeDao.findByLabel("unit-test-foreign-id-new-node-cloud")).andReturn(Collections.singletonList(m_nodeCustomCloud)).anyTimes();
        EasyMock.expect(m_nodeDao.findByLabel("unit-test-foreign-id-new-node-stream")).andReturn(Collections.singletonList(m_nodeCustomStream)).anyTimes();
        EasyMock.expect(m_nodeDao.findByForeignId("import:","node1")).andReturn(m_node1).anyTimes();
        EasyMock.expect(m_nodeDao.findByForeignId("import:","node2")).andReturn(m_node2).anyTimes();
        EasyMock.expect(m_nodeDao.findByForeignId("import:","node3")).andReturn(m_node3).anyTimes();
        EasyMock.expect(m_nodeDao.findByForeignId("import:","node4")).andReturn(m_node4).anyTimes();
        EasyMock.expect(m_nodeDao.findByForeignId("import:","node5")).andReturn(m_node5).anyTimes();
        EasyMock.expect(m_nodeDao.findByForeignId("import:","node6")).andReturn(m_node6).anyTimes();
        EasyMock.expect(m_nodeDao.findByForeignId("import:","node7")).andReturn(m_node7).anyTimes();
        EasyMock.expect(m_nodeDao.findByForeignId("import:","node8")).andReturn(m_node8).anyTimes();
        EasyMock.expect(m_nodeDao.findByForeignId("unit-test-foreign-source","unit-test-foreign-id-new-node-cloud")).andReturn(m_nodeCustomCloud).anyTimes();
        EasyMock.expect(m_nodeDao.findByForeignId("unit-test-foreign-source","unit-test-foreign-id-new-node-stream")).andReturn(m_nodeCustomStream).anyTimes();
        EasyMock.replay(m_nodeDao);
    }

    private OnmsNode getOnmsNode(Integer id) {
        OnmsNode node = null;
        switch(id) {
            case 1: node = m_node1;
                break;
            case 2: node = m_node2;
                break;
            case 3: node = m_node3;
                break;
            case 4: node = m_node4;
                break;
            case 5: node = m_node5;
                break;
            case 6: node = m_node6;
                break;
            case 7: node = m_node7;
                break;
            case 8: node = m_node8;
                break;
            case 9: node = m_nodeCustomCloud;
                break;
        }
        return node;
    }

    private void setNode1(final OnmsNode node1) {
        node1.setId(1);
        m_node1 = node1;
        m_nodes.add(m_node1);
    }

    private void setNode2(final OnmsNode node2) {
        node2.setId(2);
        m_node2 = node2;
        m_nodes.add(m_node2);
    }

    private void setNode3(final OnmsNode node3) {
        node3.setId(3);
        m_node3 = node3;
        m_nodes.add(m_node3);
    }

    private void setNode4(final OnmsNode node4) {
        node4.setId(4);
        m_node4 = node4;
        m_nodes.add(m_node4);
    }

    private void setNode5(final OnmsNode node5) {
        node5.setId(5);
        m_node5 = node5;
        m_nodes.add(m_node5);
    }

    private void setNode6(final OnmsNode node6) {
        node6.setId(6);
        m_node6 = node6;
        m_nodes.add(m_node6);
    }

    private void setNode7(final OnmsNode node7) {
        node7.setId(7);
        m_node7 = node7;
        m_nodes.add(m_node7);
    }

    private void setNode8(final OnmsNode node8) {
        node8.setId(8);
        m_node8 = node8;
        m_nodes.add(m_node8);
    }

    private void setNodeCustomCloud(final OnmsNode nodeCustomCloud) {
       nodeCustomCloud.setId(9);
       m_nodeCustomCloud = nodeCustomCloud;
       m_nodes.add(m_nodeCustomCloud);
    }

    private void setNodeCustomStream(final OnmsNode nodeCustomStream) {
       nodeCustomStream.setId(10);
       m_nodeCustomStream = nodeCustomStream;
       m_nodes.add(m_nodeCustomStream);
    }
}
