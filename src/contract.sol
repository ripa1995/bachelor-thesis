pragma solidity ^0.4.0;
import "api.sol";

contract Contract is usingTinyOracle {

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
        bytes tmp = _quantity;
        queryTinyOracle(tmp);
    }

    function queryPayment(uint _totalPrice, uint _creditCard){
        //chiamata esterna all'oracle
        bytes tmp = _totalPrice;
        queryTinyOracle(tmp);
    }

    function queryDelivery(uint _deliveryAddress, uint _quantity, uint _totalWeight){
        //chiamata esterna all'oracle
        bytes tmp = _quantity;
        queryTinyOracle(tmp);
    }

    function querySupplier(uint _quantity, uint _itemID){
        //chiamata esterna all'oracle
        bytes tmp = _quantity;
        queryTinyOracle(tmp);
    }

    function _callbackBuyerGUI(uint _quantity, uint _itemID, uint _creditCard, uint _deliveryAddress) onlyFromTinyOracle external {
        creditCard = _creditCard;
        quantity = _quantity;
        deliveryAddress = _deliveryAddress;
        itemID = _itemID;
        queryManufacturer(quantity, itemID);
    }

    function _callbackManufacturer(uint _availability, uint _price, uint _weight) onlyFromTinyOracle external {
        totalPrice = _price * quantity;
        if (_availability==1){
            totalWeight = quantity * _weight;
            queryPayment(totalPrice, creditCard);
            queryDelivery(deliveryAddress, quantity, totalWeight);
        } else {
            querySupplier(quantity, itemID);
        }
    }

    function _callbackSupplier(uint _supplierPrice) onlyFromTinyOracle external {
        totalPrice = totalPrice + _supplierPrice;
        queryPayment(totalPrice, creditCard);
        queryDelivery(deliveryAddress, quantity, totalWeight);
    }

}
