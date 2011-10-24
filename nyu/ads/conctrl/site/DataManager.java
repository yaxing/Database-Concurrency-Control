package nyu.ads.conctrl.site;


import java.util.*;
/**
 * DataManager class
 * 
 * hold and manage all data on this site
 * 
 * @author Yaxing Chen(N16929794)
 *
 */
public class DataManager {

	HashMap<Integer, Integer> db;// actual db on this server, "source index"=>"latest timestamp"
	
	ArrayList<String[]> transactionLog; // transaction log: String[5] : 
										// [0]: Transactin No
										// [1]: op (W/R)
										// [2]: source index
										// [3]: operation value
										// [4]: operation result(successful or not): 1/0
	
	ArrayList<String[]> commitLog; // commit log: String[]:
								   // 	
	
	DataManager() {
		this.db = new HashMap<Integer, Integer>();
	}
	
}
