public class pickList {

    public pickList(){}

   @RemoteAction
    public static String getAllObjects(Boolean typeObj) {
        try {
            String[] objList = new String[] {};
            Map<String, Schema.SObjectType> GlobalMap = Schema.getGlobalDescribe();
                for (Schema.SObjectType Obj : GlobalMap.values()) {
                     Schema.DescribeSObjectResult ObjDesc = Obj.getDescribe();
                     system.debug('Object Name: ' + ObjDesc.getName());
                     system.debug(ObjDesc.isCustom());
                     if(ObjDesc.isCustom() == typeObj){
                        objList.add(ObjDesc.getName());                     
                     }
                        system.debug(objList);    
                 }
                                  return JSON.serialize(objList);
        
        } catch (Exception ex) {
           return JSON.serialize(ex);
        }

        
    }


    @RemoteAction
    public static String getAllFields(String nameObject){
        try{
            //String[] objFieldApiName = new String[] {};
            List<Object> objFieldApiName = new List<Object>();
            Map<String, Schema.SObjectType> schemaMap = Schema.getGlobalDescribe();
            Schema.SObjectType leadSchema = schemaMap.get(nameObject);
            Map<String, Schema.SObjectField> fieldMap = leadSchema.getDescribe().fields.getMap();
            for (String fieldName: fieldMap.keySet()) { 
                System.debug(fieldName); 
                objFieldApiName.add(fieldName);
                //It provides to get the object fields label.
                //String fieldLabel = fieldMap.get(fieldName).getDescribe().getLabel();
        }
        String fields = String.join(objFieldApiName,',');
          List<SObject> fieldRec = new List<SObject>();
          String que = 'SELECT '+fields+' FROM ' + nameObject;
          System.debug(que);
         fieldRec = Database.query(que);
          System.debug(fieldRec);
         return JSON.serialize(fieldRec);
        }catch (Exception ex) {
           return JSON.serialize(ex);
        }
    }

    
    @RemoteAction
    public static String getAllRecordInField(String name, String field){
        //try{
           List<Object> m = (List<Object>)JSON.deserializeUntyped(field);
           System.debug(m);
           System.debug(m.size());
           System.debug(name);
           String fields = String.join(m,',');
           System.debug(fields);
            List<SObject> fieldRec = new List<SObject>();
        ////  String[] recordField = new String[] {};
            String que ='SELECT '+ fields +' FROM ' + name;
           fieldRec = Database.query(que);
        ////    System.debug(fieldRec);
            return JSON.serialize(fieldRec);
            
        //    }catch(Exception ex) {
        //            return JSON.serialize(ex);
        //    }
        //return JSON.serialize('lol');
    }    

}