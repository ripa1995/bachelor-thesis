pragma solidity ^0.4.0;
import "github.com/oraclize/ethereum-api/oraclizeAPI.sol";

contract Contract is usingOraclize {

    uint quantity;
    uint itemID;
    uint creditCard;
    uint deliveryAddress;
    uint totalPrice;
    uint totalWeight;

    event queryBuyerGUIEvent();
    event queryManufacturerEvent(uint _quantity, uint _itemID);
    event queryPaymentEvent(uint _totalPrice, uint _creditCard);
    event queryDeliveryEvent(uint _deliveryAddress, uint _quantity, uint _totalWeight);
    event querySupplierEvent(uint _quantity, uint _itemID);

    function main(){
        queryBuyerGUI();
    }

    function queryBuyerGUI(){
        //do things
    }

    function queryManufacturer(uint _quantity, uint _itemID){
        //chiamata esterna all'oracle
        oraclize_query("URL", "https://xyz.io/queryManufacturer", '{"quantity": _quantity, "itemID": _itemID}');
    }

    function queryPayment(uint _totalPrice, uint _creditCard){
        //chiamata esterna all'oracle
        oraclize_query("URL", "https://xyz.io/queryPayment", '{"totalPrice": _totalPrice, "creditCard": _creditCard}');
    }

    function queryDelivery(uint _deliveryAddress, uint _quantity, uint _totalWeight){
        //chiamata esterna all'oracle
        oraclize_query("URL", "https://xyz.io/queryDelivery", '{"deliveryAddress": _deliveryAddress, "quantity": _quantity, "totalWeight": _totalWeight}');
    }

    function querySupplier(uint _quantity, uint _itemID){
        //chiamata esterna all'oracle
        oraclize_query("URL", "https://xyz.io/querySupplier", '{"quantity": _quantity, "itemID": _itemID}');
    }

    function _callbackBuyerGUI(uint _quantity, uint _itemID, uint _creditCard, uint _deliveryAddress){
        creditCard = _creditCard;
        quantity = _quantity;
        deliveryAddress = _deliveryAddress;
        itemID = _itemID;
        queryManufacturer(quantity, itemID);
    }

    function _callbackManufacturer(uint _availability, uint _price, uint _weight) external {
        totalPrice = _price * quantity;
        if (_availability>1){
            totalWeight = quantity * _weight;
            queryPayment(totalPrice, creditCard);
            queryDelivery(deliveryAddress, quantity, totalWeight);
        } else {
            querySupplier(quantity, itemID);
        }
    }

    function _callbackSupplier(uint _supplierPrice){
        totalPrice = totalPrice + _supplierPrice;
        queryPayment(totalPrice, creditCard);
        queryDelivery(deliveryAddress, quantity, totalWeight);
    }

}
