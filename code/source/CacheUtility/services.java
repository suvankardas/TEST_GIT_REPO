package CacheUtility;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-08-15 21:57:05 IST
// -----( ON-HOST: 192.168.2.5

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.softwareag.util.IDataMap;
import com.lxk.ehcache.utility.CacheEntryFactoryImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.MemoryUnit;
import net.sf.ehcache.config.SearchAttribute;
import net.sf.ehcache.config.Searchable;
import net.sf.ehcache.config.TerracottaClientConfiguration;
import net.sf.ehcache.config.TerracottaConfiguration;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;
import net.sf.ehcache.search.query.QueryManager;
import net.sf.ehcache.search.query.QueryManagerBuilder;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
// --- <<IS-END-IMPORTS>> ---

public final class services

{
	// ---( internal utility methods )---

	final static services _instance = new services();

	static services _newInstance() { return new services(); }

	static services _cast(Object o) { return (services)o; }

	// ---( server methods )---




	public static final void createCache (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(createCache)>> ---
		// @sigtype java 3.5
		Configuration cmConfig = new Configuration();
		cmConfig.name("AssetCacheManager").terracotta(
				new TerracottaClientConfiguration().url("localhost:9510"));
		CacheManager cm = CacheManager.create(cmConfig);
		
		CacheConfiguration cacheConfig = new CacheConfiguration();
		cacheConfig
				.name("Asset")
				.maxBytesLocalHeap(64, MemoryUnit.MEGABYTES)
				.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU)
				.eternal(false)
				.timeToLiveSeconds(120)
				.timeoutMillis(2)
			//	.cacheWriter(writerConfig)
				.terracotta(new TerracottaConfiguration());
				
		
		Searchable searchable = new Searchable();
		cacheConfig.addSearchable(searchable);
		
		searchable.addSearchAttribute(new SearchAttribute().name("empName")
				.expression("value.empName"));
		searchable.addSearchAttribute(new SearchAttribute().name("empID")
				.expression("value.empID"));
		searchable.addSearchAttribute(new SearchAttribute().name("orgName")
				.expression("value.orgName"));
		searchable.addSearchAttribute(new SearchAttribute().name("location")
				.expression("value.location"));
		
		Cache cache = new Cache(cacheConfig);
		
		
		cm.addCache(cache);
		
		//Element element = new Element(key, value);
		//cache.put(element);
		// --- <<IS-END>> ---

                
	}



	public static final void get (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(get)>> ---
		// @sigtype java 3.5
		IDataCursor idc = pipeline.getCursor();
		CacheManager cacheManager = (CacheManager) IDataUtil.get(idc,"cacheManager");
		Ehcache cache = (Ehcache) IDataUtil.get(idc,"cache");
		String key = IDataUtil.getString(idc,"key");
		idc.destroy();
		
		Element elem = cache.get(key);
		com.wm.util.Debug.log(4, "Element:"+elem);
		// --- <<IS-END>> ---

                
	}



	public static final void getSelfPopulatingCache (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getSelfPopulatingCache)>> ---
		// @sigtype java 3.5
		// [i] field:0:required cacheManagerName
		// [i] field:0:required cacheName
		// [o] object:0:required selfPopulatingCache
		// [o] object:0:required cacheManager
		IDataCursor idc = pipeline.getCursor();
		String cacheManagerName = IDataUtil.getString(idc,"cacheManagerName");
		String cacheName = IDataUtil.getString(idc,"cacheName");
		idc.destroy(); 
		
		CacheManager cm = CacheManager.getCacheManager(cacheManagerName);
		Ehcache cache = cm.getEhcache(cacheName);
		 
		CacheEntryFactory cef = new CacheEntryFactoryImpl();	
		SelfPopulatingCache spc = new SelfPopulatingCache(cache, cef);
		
		IDataCursor idc1 = pipeline.getCursor();
		IDataUtil.put(idc, "selfPopulatingCache", spc);
		IDataUtil.put(idc, "cacheManager", cm);
		idc1.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void mergeDocList (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(mergeDocList)>> ---
		// @sigtype java 3.5
		IDataCursor idc = pipeline.getCursor();
		
		IData [] doclist1 = IDataUtil.getIDataArray(idc, "docList1");
		IData [] doclist2 = IDataUtil.getIDataArray(idc, "docList2");
		
		
		for(int i=0; i< doclist1.length; i++){
			for(int j=0; j<doclist2.length; j++){
				
			}
		}
		
		
		IDataCursor docList1Cur = doclist1[0].getCursor();
		docList1Cur.next();
		
		IDataMap idm = new IDataMap(doclist1[0]);
		IDataMap idm2 = new IDataMap(doclist2[0]);
		
		idm.putAll(idm2);
		
		
		com.wm.util.Debug.log(4, "doclist1 0: "+idm);
		com.wm.util.Debug.log(4, "doclist2 0: "+doclist2[0]);
		// --- <<IS-END>> ---

                
	}



	public static final void put (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(put)>> ---
		// @sigtype java 3.5
		IDataCursor idc = pipeline.getCursor();
		IData assetDoc = IDataUtil.getIData(idc,"Asset");
		CacheManager cacheManager = (CacheManager) IDataUtil.get(idc,"cacheManager");
		Ehcache cache = (Ehcache) IDataUtil.get(idc,"cache");
		String key = IDataUtil.getString(idc,"key");
		idc.destroy();
		
		Element elem = new Element(key,assetDoc);
		
		cache.put(elem);
		
		IDataCursor idc1 = pipeline.getCursor();
		IDataUtil.put(idc, "cache", cache);
		idc1.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void search (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(search)>> ---
		// @sigtype java 3.5
		IDataCursor idc = pipeline.getCursor();
		String cmName = IDataUtil.getString(idc,"cacheManager");
		String cacheName = IDataUtil.getString(idc,"cache");
		String queryString = IDataUtil.getString(idc,"queryString");
		idc.destroy();
		
		String tempQueryString = queryString.toLowerCase();
		boolean isKeyReq = false;
		// extract starting position of keywords from query string
		int selectPos = tempQueryString.indexOf("select");
		int fromPos = tempQueryString.indexOf("from");
		int wherePos = tempQueryString.indexOf("where");
		int groupByPos = tempQueryString.indexOf("group by");
		int orderByPos = tempQueryString.indexOf("order by");
		
		// throw error is select and from is not present in the query
		if((selectPos == -1)&& (fromPos == -1)){ 
			throw new ServiceException("Either Select or From or both are not present in queryString");
		}
		
		// extract fields from queryString;
		String fieldsAsString = queryString.substring(selectPos+6,fromPos -1);
		//		com.wm.util.Debug.log(4, "fields "+fieldsAsString);
		String [] fields = fieldsAsString.split(","); 
		
		// get CacheManager and Cache instances
		CacheManager manager = CacheManager.getCacheManager(cmName);
		Cache cache = manager.getCache(cacheName);
		
		// create Attributes
		Set<Attribute> attributeList = new HashSet();
		ArrayList<String> aggFuncColNameList = new ArrayList<String>();
		for(int i=0; i < fields.length; i++){
			String fieldName = fields[i].trim();
		//	com.wm.util.Debug.log(4, "fieldName: "+fieldName);
			if(fieldName.equals("*")){ // e.g. select * from <cachename> 
				attributeList = cache.getSearchAttributes(); 
			}else if((fieldName.toLowerCase().matches("count.*?\\(.*?\\)"))|| // e.g select count(*) from <cachename>/ select count(<field name>) from <cachename>
					(fieldName.toLowerCase().matches("max.*?\\(.*?\\)"))||
					(fieldName.toLowerCase().matches("min.*?\\(.*?\\)"))||
					(fieldName.toLowerCase().matches("avg.*?\\(.*?\\)"))||
					(fieldName.toLowerCase().matches("average.*?\\(.*?\\)"))||
					(fieldName.toLowerCase().matches("sum.*?\\(.*?\\)"))){
				aggFuncColNameList.add(fieldName); //storing the field name for the aggregator function for priving in output later
		//		com.wm.util.Debug.log(4,"aggregate column within loop: "+aggFuncColNameList);
			}else if(fieldName.equalsIgnoreCase("key")){
				isKeyReq = true;
			}else{
				// getting searchable attributes using the field name
				Attribute field = cache.getSearchAttribute(fieldName);
				if(field == null){
					throw new ServiceException("Invalid field name: "+fieldName);				
				}
				attributeList.add(field);
		//		com.wm.util.Debug.log(4, "attributes within loop: "+attributeList.toString());
			}
		}
		//	com.wm.util.Debug.log(4,"aggregate column: "+aggFuncColNameList);
		//	com.wm.util.Debug.log(4, "attributes: "+attributeList.toString());
		//create query
		QueryManager queryManager = QueryManagerBuilder.newQueryManagerBuilder().addCache(cache).build();
		Query qry = queryManager.createQuery(queryString);
		
		// includes attributes in query
		Iterator it = attributeList.iterator();
		while (it.hasNext()){
			qry = qry.includeAttribute((Attribute)it.next());
		}
		if (isKeyReq){
			qry = qry.includeKeys();
		}
		// execute query
		Results results = qry.end().execute();
		
		// process results
		List<Result> resultList = results.all();
		//		com.wm.util.Debug.log(4, "size of result: "+results.size());
		//		com.wm.util.Debug.log(4, "results: "+results.all().get(0));
		//		com.wm.util.Debug.log(4, "results: "+results);
		
			
		// extracts data from query output object and prepares Values object for putting into pipeline
		
		IData result = null;
		IDataCursor resultCursor = null;
		
		IData qryOutput = null;
		IDataCursor qryOutputCursor = null;
		
		IData [] resultIData = new IData[results.size()];
		
		for(int i=0; i<resultList.size();i++){
			Values hm = new Values();
			Values hmResult = new Values();
			
			qryOutput = IDataFactory.create();
			qryOutputCursor = qryOutput.getCursor();
			
			result = IDataFactory.create();
			resultCursor = result.getCursor();
			
			if(results.hasAttributes()){ //in case of attributes/columns
				it = attributeList.iterator();
		
				while(it.hasNext()){
					
					Attribute field = (Attribute)it.next();
					if(!field.getAttributeName().equals("value")&&!field.getAttributeName().equals("key")){
						//hm.put(field.getAttributeName(),resultList.get(i).getAttribute(field));
						IDataUtil.put(resultCursor, field.getAttributeName(), resultList.get(i).getAttribute(field));
						resultCursor.next();
					}// end if
		
				}// end while
			}//end if hasAttributes
			if(results.hasAggregators()){ // in case of aggregator functions
				
				List aggFuncList = resultList.get(i).getAggregatorResults();
				
		//		com.wm.util.Debug.log(4, "record: "+aggFuncList.toString());
				
				Iterator itFuncColName = aggFuncColNameList.iterator();
				Iterator itFunc = aggFuncList.iterator();
				while(itFunc.hasNext()){
					//hm.put(itFuncColName.next().toString(),itFunc.next());
					IDataUtil.put(resultCursor, itFuncColName.next().toString(), itFunc.next());
					resultCursor.next();
				}
			}// end if hasAggregators
		//	com.wm.util.Debug.log(4, "record: "+hm.toString());
			
			if(results.hasKeys()){
				//hmResult.put("result",(IData)hm);
			//	IDataUtil.put(qryOutputCursor, "result", result);
			//	resultCursor.destroy();
			//	result = null;
				
			//	qryOutputCursor.next();
				//hmResult.put("KEY",resultList.get(i).getKey());
			//	IDataUtil.put(qryOutputCursor, "key", resultList.get(i).getKey());
				
				IDataUtil.put(resultCursor,"key", resultList.get(i).getKey());
			
				
			}else{
				//hmResult.put("result",(IData)hm);
		//		com.wm.util.Debug.log(4, "result:"+result);
			//	IDataUtil.put(qryOutputCursor, "result", result);
			//	resultCursor.destroy();
			//	result = null;
			}
			//com.wm.util.Debug.log(4, "qryOutput:"+qryOutput);
			//resultIData[i]=qryOutput;
			resultIData[i]=result;
			
		//			com.wm.util.Debug.log(4, "resultIData:"+resultIData.toString());
		
		//	qryOutputCursor.destroy();
		//	qryOutput = null;
			
			resultCursor.destroy();
			result = null;
			
		}// end for
		
		//put output into pipeline
		IDataCursor idc1 = pipeline.getCursor();
		IDataUtil.put(idc1, "qryOutput", resultIData);
		//IDataUtil.put(idc1, "qryOutput", qryOutput);
		idc1.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void searchCache (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(searchCache)>> ---
		// @sigtype java 3.5
		//CacheManager cm = CacheManager.getCacheManager("LBSAssetCacheManager");
				//Ehcache cache = cm.getEhcache("LBSAsset");
				
				IDataCursor idc = pipeline.getCursor();
				CacheManager cacheManager = (CacheManager) IDataUtil.get(idc,"cacheManager");
				Ehcache cache = (Ehcache) IDataUtil.get(idc,"cache");
				
				Attribute<String> Alert = cache.getSearchAttribute("Alert");
				Attribute<String> AccountCode = cache.getSearchAttribute("AccountCode");
				
						
				Query query = cache.createQuery()
						.includeAttribute(Alert)
						.includeAttribute(AccountCode)
						.includeAggregator(Alert.count())
						.addCriteria(Alert.eq("Toner Low"))
						.end();
				Results results = query.execute();	
				
				List<Result> resultList = results.all();
				
				for(int i=0; i< resultList.size(); i++){
					
					com.wm.util.Debug.log(4, "----result:--"+resultList.get(i));
					com.wm.util.Debug.log(4, "----AccountCode:--"+resultList.get(i).getAttribute(AccountCode));
				//	com.wm.util.Debug.log(4, "----AccountCode count:--"+resultList.get(i).getAggregatorResults().get(0));
					com.wm.util.Debug.log(4, "----Alert:--"+resultList.get(i).getAttribute(Alert));
				//	com.wm.util.Debug.log(4, "----Alert count:--"+resultList.get(i).getAggregatorResults().get(0));
					
					List aggList = resultList.get(i).getAggregatorResults();
					for(int j=0; j <aggList.size();j++ ){
						com.wm.util.Debug.log(4, "----count:--"+resultList.get(i).getAggregatorResults().get(j));
					}
				}
		// --- <<IS-END>> ---

                
	}



	public static final void setNull (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(setNull)>> ---
		// @sigtype java 3.5
		IDataCursor idc = pipeline.getCursor();
		IDataUtil.put(idc, "output", null);
		idc.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void test (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(test)>> ---
		// @sigtype java 3.5
		com.wm.util.Debug.log(4, "------pipeline----"+pipeline.toString());
		IDataCursor idc = pipeline.getCursor();
		//idc.next();
		String s = "input";
		Object obj = IDataUtil.get(idc,s );
		com.wm.util.Debug.log(4, "------input value----"+obj);
		// --- <<IS-END>> ---

                
	}



	public static final void testIDM (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(testIDM)>> ---
		// @sigtype java 3.5
		IDataCursor idc = pipeline.getCursor();
		IData assetDoc = IDataUtil.getIData(idc,"Asset");
		IData inputDoc = IDataUtil.getIData(idc,"input");
		com.wm.util.Debug.log(4, "assetDoc: "+assetDoc);
		com.wm.util.Debug.log(4, "inputDoc: "+inputDoc);
		idc.destroy();
		
		IDataMap idm = new IDataMap();
		
		IDataCursor assetCursor = assetDoc.getCursor();
		IDataCursor inputCursor = inputDoc.getCursor();
		
		while(assetCursor.next()){
			while(inputCursor.next()){
				com.wm.util.Debug.log(4, "inputCursor: "+inputCursor.getKey());
				if(assetCursor.getKey().equals(inputCursor.getKey())){
					idm.put(assetCursor.getKey(), assetCursor.getValue());
				}
			}
			inputCursor.first();
		}
		
		assetCursor.destroy();
		
		IDataCursor idc1 = pipeline.getCursor();
		IDataUtil.put(idc1, "output", idm.getIData());
		
		com.wm.util.Debug.log(4, "idm: "+idm.getIData());
		// --- <<IS-END>> ---

                
	}
}

