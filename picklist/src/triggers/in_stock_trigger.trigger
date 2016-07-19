trigger in_stock_trigger on Product__c (before insert) {
 List<Product__c> productsList = new  List<Product__c>();
    productsList = [SELECT Quantity__c,in_stock__c FROM Product__c ]  ;
    for(Product__c item : Trigger.new ){
        if(item.Quantity__c>0)
            item.in_stock__c = true;
    }
}