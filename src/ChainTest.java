import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChainTest extends TestCase {

    private Chain chain;

    public void setUp() throws Exception {
        super.setUp();
        chain = Chain.getChain("4f1e1945321c493e5e53fdb1236a2849");
    }

    public void tearDown() throws Exception {

    }
    
    public void testGetAddressUnspents() throws Exception {
 	   String addr = "1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp";
        JSONArray addressTransactions = chain.getAddressTransactions(addr);
        assertNotNull(addressTransactions);
        addr = "1dice8EMZmqKvrGE4Qc9bUfdsFf9PX3xaYDp";
        addressTransactions = chain.getAddressTransactions(addr);
        assertNull(addressTransactions);
 }


    public void testGetAddress() throws Exception {
        String addr = "1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp";
        String wrongAddr = "1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDpWRONG";
        JSONObject jsonAnswr = chain.getAddress(addr);
        assertNotNull(jsonAnswr);
        assertEquals(jsonAnswr.get("hash"), addr);
        jsonAnswr = chain.getAddress(wrongAddr);
        assertNull(jsonAnswr);
    }

    public void testGetAddressTransactions() throws Exception {
        String addr = "1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp";
        JSONArray addressTransactions = chain.getAddressTransactions(addr);
        assertNotNull(addressTransactions);
        JSONObject json = (JSONObject) addressTransactions.get(0);
        assertTrue((Integer) json.get("amount") >= 0);
        addr = "1dice8EMZmqKvrGE4Qc9bUfdsFf9PX3xaYDp";
        addressTransactions = chain.getAddressTransactions(addr);
        assertNull(addressTransactions);
    }

    public void testGetAddressTransactions1() throws Exception {
        String addr = "1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp";
        JSONArray addressTransactions = chain.getAddressTransactions(addr, 500);
        assertNotNull(addressTransactions);
        assertTrue(addressTransactions.length() == 500);
        addr = "1dice8EMZmqKvrGE4Qc9bUfdsFf9PX3xaYDp";
        addressTransactions = chain.getAddressTransactions(addr,500);
        assertNull(addressTransactions);
    }

  
    public void testGetTransaction() throws Exception {
        String hash = "4cc6ff6ab765158ed179938542ce15801155754cafbb3d6be773cab80edb8ced";
        JSONObject transaction = chain.getTransaction(hash);
        assertNotNull(transaction);
        assertEquals(transaction.get("hash"), hash);
        hash = "1dice8EMZmqKvrGE4Qc9bUfdsFf9PX3xaYDp";
        transaction = chain.getTransaction(hash);
        assertNull(transaction);
    }

    public void testGetTransactionOpRerturn() throws Exception {
    	 
         String hash = "1dice8EMZmqKvrGE4Qc9bUfdsFf9PX3xaYDp";
         JSONObject transaction = chain.getTransactionOpRerturn(hash);
         assertNull(transaction);
    	//hash = "A transaction hash with an OP_return";
        //JSONObject transaction = chain.getTransactionOpRerturn(hash);
        //assertNotNull(transaction);   
    }
    
    public void testSendTransaction() throws Exception {
    	 String hex = "1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp";
         String addressTransactions = chain.sendTransaction(hex);
         assertNull(addressTransactions);         
         //hex = "Put a Real unused Hex";
         //addressTransactions = chain.sendTransaction(hex);
         //assertNotNull(addressTransactions);       
         //hex = "Put Fake Hex";
         //addressTransactions = chain.sendTransaction(hex);
         //assertNull(addressTransactions); 
         //hex = "Put Hex already used";
         //addressTransactions = chain.sendTransaction(hex);
         //assertNull(addressTransactions);   
    }

    public void testGetBlockByHash() throws Exception {
    	  String hash = chain.getLatestBlock().get("hash").toString();
          JSONObject block = chain.getBlockByHash(hash);
          assertNotNull(block);
          assertEquals(block.get("hash"), hash);
    }

    public void testGetBlockByHeight() throws Exception {
    	  int height = 3;
          JSONObject block = chain.getBlockByHeight(height);
          assertNotNull(block);
          assertNotNull(block.get("hash"));
          height = 300000;
          block = chain.getBlockByHeight(height);
          assertNotNull(block);
          assertNotNull(block.get("hash"));
          height = 0;
          block = chain.getBlockByHeight(height);
          assertNotNull(block);
          assertNotNull(block.get("hash"));
          height = -1;
          block = chain.getBlockByHeight(height);
          assertNull(block);
          height = 100000000;
          block = chain.getBlockByHeight(height);
          assertNull(block);
    }

    public void testGetLatestBlock() throws Exception {
          JSONObject block = chain.getLatestBlock();
          assertNotNull(block);
          assertNotNull(block.get("hash"));
    }
    
    public void testCheckCertificate() {
    	assertTrue(CheckSecurity.checkCertificate());
    }
  

}