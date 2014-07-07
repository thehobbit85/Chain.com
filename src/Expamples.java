import org.json.JSONException;


public class Expamples {

	public static void main(String[] args) throws JSONException {
		
		Chain chain = Chain.getChain(); // Getting an API wrapper default API key
	//	chain.useTestNet3(); // Use the Testnet3 Blockchain
	//	chain.useMainNet(); // Use the real Bitcoin Blockcahin (defualt status)
		
		//Check Addresses balance (in Satoshis)
		System.out.println(chain.getAddress("16LaExj9SLXpYqAwZuiQ9XWRKLpZDdPC4E").get("balance")); 
		//Get the latest transaction amount (in Satoshis)
		System.out.println(chain.getAddressTransactions("1PozS2mm5wUyxLj8E9DMkj35zM6jz52WHk").getJSONObject(0).get("amount"));  
		//Faster way to get the latest transaction amount (in Satoshis)
		System.out.println(chain.getAddressTransactions("16LaExj9SLXpYqAwZuiQ9XWRKLpZDdPC4E",1).getJSONObject(0).get("amount"));
		//Get the latest unspent transaction value (in Satoshis)
		System.out.println(chain.getAddressUnspents("16LaExj9SLXpYqAwZuiQ9XWRKLpZDdPC4E").getJSONObject(0).get("value"));
		//Get the decoded text from a transaction's OP_RETURN
		System.out.println(chain.getTransactionOpRerturn("c607d67d01724771f849a6117554324d7cd6bbb1c0c42bf669d66c6286fd30d0").get("text"));
		//Get a blocks hash
		System.out.println(chain.getBlockByHeight(0).get("hash"));
		//Get a blocks height
		System.out.println(chain.getBlockByHash("00000000f102043c1c58e47b6e91e6804f7ae356fed25c456c6f8359e737da6d").get("height"));
		//Get the latest blocks count
		System.out.println(chain.getLatestBlock().get("height"));		
		
		// TODO Auto-generated method stub

	}

}
