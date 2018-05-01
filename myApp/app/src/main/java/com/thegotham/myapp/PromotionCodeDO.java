package com.thegotham.myapp;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "parkinggarage-mobilehub-1238327462-PromotionCode")

public class PromotionCodeDO {
    private String _codeName;
    private String _dateCreated;

    @DynamoDBHashKey(attributeName = "CodeName")
    @DynamoDBAttribute(attributeName = "CodeName")
    public String getCodeName() {
        return _codeName;
    }

    public void setCodeName(final String _codeName) {
        this._codeName = _codeName;
    }
    @DynamoDBRangeKey(attributeName = "DateCreated")
    @DynamoDBAttribute(attributeName = "DateCreated")
    public String getDateCreated() {
        return _dateCreated;
    }

    public void setDateCreated(final String _dateCreated) {
        this._dateCreated = _dateCreated;
    }

}
