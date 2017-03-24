package io.fundrequest.harvesth;

import org.ethereum.core.Block;
import org.ethereum.core.BlockSummary;
import org.ethereum.core.PendingState;
import org.ethereum.core.Transaction;
import org.ethereum.core.TransactionExecutionSummary;
import org.ethereum.core.TransactionReceipt;
import org.ethereum.listener.EthereumListener;
import org.ethereum.net.eth.message.StatusMessage;
import org.ethereum.net.message.Message;
import org.ethereum.net.p2p.HelloMessage;
import org.ethereum.net.rlpx.Node;
import org.ethereum.net.server.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface HarvesthEthereumListener extends EthereumListener {

    Logger logger = LoggerFactory.getLogger(HarvesthEthereumListener.class);

    @Override
    default void trace(String output) {
        logger.debug("trace");
    }

    @Override
    default void onNodeDiscovered(Node node) {
        logger.debug("onNodeDiscovered");
    }

    @Override
    default void onHandShakePeer(Channel channel, HelloMessage helloMessage) {
        logger.debug("onHandShakePeer");
    }

    @Override
    default void onEthStatusUpdated(Channel channel, StatusMessage status) {
        logger.debug("onEthStatusUpdated");
    }

    @Override
    default void onRecvMessage(Channel channel, Message message) {
        logger.debug("onRecvMessage");
    }

    @Override
    default void onSendMessage(Channel channel, Message message) {
        logger.debug("onSendMessage");
    }

    @Override
    default void onBlock(BlockSummary blockSummary) {
        logger.debug("onBlock");
    }

    @Override
    default void onPeerDisconnect(String host, long port) {
        logger.debug("onPeerDisconnect");
    }

    @Override
    default void onPendingTransactionsReceived(List<Transaction> transactions) {
        logger.debug("onPendingTransactionsReceived");
    }

    @Override
    default void onPendingStateChanged(PendingState pendingState) {
        logger.debug("onPendingStateChanged");
    }

    @Override
    default void onPendingTransactionUpdate(TransactionReceipt txReceipt, PendingTransactionState state, Block block) {
        logger.debug("onPendingTransactionUpdate");
    }

    @Override
    default void onSyncDone(SyncState state) {
        logger.debug("onSyncDone");
    }

    @Override
    default void onNoConnections() {
        logger.debug("onNoConnections");
    }

    @Override
    default void onVMTraceCreated(String transactionHash, String trace) {
        logger.debug("onVMTraceCreated");
    }

    @Override
    default void onTransactionExecuted(TransactionExecutionSummary summary) {
        logger.debug("onTransactionExecuted");
    }

    @Override
    default void onPeerAddedToSyncPool(Channel peer) {
        logger.debug("onPeerAddedToSyncPool");
    }
}
