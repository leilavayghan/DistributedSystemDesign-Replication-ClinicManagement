package Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import interfaces.IGroupable;
import interfaces.IRecord;
import shared.ExceptionStrings;
/**
 * Groupedepository deals with all the functionalities required by Client by invoking method on Server. It takes generic record as type and implement functionality on it. 
 * @param <GroupableType>
 */
public class GroupedRepository<GroupableType extends IGroupable & IRecord> 
{
	
	
	private HashMap<String, ArrayList<GroupableType>> _records = new HashMap<String, ArrayList<GroupableType>>();
	/**
	 *  Add creates a record depending upon the type of staffrecord it is, a doctor record or nurse record
	 * @param record type of record either nurse or doctor
	 */
	public synchronized void Add(GroupableType record)
	{
		synchronized(_records){
			String key = record.getGroupKey();
		
			if(_records.containsKey(key)){
				ArrayList<GroupableType> existingGroup = _records.get(key);
				existingGroup.add(record);
			}
		
			else{
				ArrayList<GroupableType> newGroup = new ArrayList<GroupableType>();
				newGroup.add(record);
				_records.put(key, newGroup);
			}	
		}
	}
	/**
	 *  returns the complete record of staff member
	 * @param recordID unique id of record
	 * @return Instance of staffrecord
	 */
	public synchronized GroupableType GetRecord(String recordID)
	{
		synchronized(_records){
		for(List<GroupableType> group : _records.values())
		{
			for(GroupableType record : group)
			{
				if(record.getRecordID().equals(recordID))
					return record;
			}
		}
		
		return null;
		}
	}
	/**
	 * Function implemented to get an array of all the record ids
	 * @return array of record-ids.
	 */
	public String[] getRecordIDs()
	{
		synchronized(_records){
		ArrayList<String> recordIDs = new ArrayList<String>();
		
		for(List<GroupableType> group : _records.values())
		{
			for(GroupableType record : group)
			{
				recordIDs.add(record.getRecordID());
			}
		}
		
		return recordIDs.toArray(new String[0]);
		}
	}
	/**
	 *  Function used to edit the field of record 
	 * @param recordID unique id of record
	 * @param FieldName attribute to edit
	 * @param FieldValue he value by which field is to edit
	 * @throws Exception 
	 */
	public void Edit(String recordID, String FieldName, String FieldValue) throws Exception
	{
		synchronized(_records){
		GroupableType record = GetRecord(recordID);
		
		Field[] recordFields = record.getClass().getFields();

		for(Field field : recordFields)
		{	
			if(field.getName().equals(FieldName))
			{
					field.set(record, FieldValue);
					break;
				}
				else
				{
					throw new Exception(ExceptionStrings.InvalidaFieldValue); 
				}
			}
		}
	}
	
	
	/**
	 * Function to return recordIDs of all the records entered
	 * @return string of recordIDs
	 */
	public synchronized String getRecord()
	{
		synchronized(_records){
		ArrayList<String> recordIDs = new ArrayList<String>();
		
		for(List<GroupableType> group : _records.values())
		{
			for(GroupableType record : group)
			{
				recordIDs.add(record.getRecordID());
			}
		}
		
		return recordIDs.toString();
		}
	}
	/**
	 * Function used to delete the record from the hashmap
	 * @param recordID
	 */
	public synchronized void deleteRecord( String recordID)
	{
		synchronized(_records){
		String key = GetRecord(recordID).getGroupKey();
		_records.get(key).remove(GetRecord(recordID));
		}
		
	}
	/**
	 * Function used to find whether the records is present or not 
	 * @param recordID
	 * @return
	 */
	public synchronized boolean findRecord(String recordID)
	{
		synchronized(_records){
		boolean isFound = false;
		for(List<GroupableType> group : _records.values())
	
			{
				for(GroupableType record : group)
				{
					if(record.getRecordID().equals(recordID))
					{
						isFound = true;
					}
				}
			}
		return isFound;
		}
	}

	/**
	 * this function is used to get the hashmap of the server at the time of updating the state
	 * @return hashmap containing the records
	 */
	public HashMap<String, ArrayList<GroupableType>> getHashMap(){
		synchronized(_records){
		return _records;
		}
	}
	
	/**
	 * this function receives an object and casts it back to a hashmap type and updates the hashmap of the server.
	 * @param o this object is received from an input stream.
	 */
	@SuppressWarnings("unchecked")
	public void setHashMap(Object o){
		synchronized(_records){
		_records = (HashMap<String, ArrayList<GroupableType>>) o;
		}
	}
}