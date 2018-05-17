pragma solidity ^0.4.0;
import "github.com/oraclize/ethereum-api/oraclizeAPI.sol";

contract Contract is usingOraclize {

    uint quantity;
    uint itemID;
    uint creditCard;
    uint deliveryAddress;
    uint availability;
    uint price; // questo non ti serve 
    uint totalPrice;
    uint totalWeight;
    uint supplierPrice;

    event queryBuyerGUIEvent(uint _quantity, uint _itemID, uint _creditCard, uint _deliveryAddress);
    event queryManufacturerEvent(uint _quantity, uint _itemID);
    event queryPaymentEvent(uint _totalPrice, uint _creditCard);
    event queryDeliveryEvent(uint _deliveryAddress, uint _quantity, uint _totalWeight);
    event querySupplierEvent(uint _quantity, uint _itemID);

    function queryBuyerGUI(uint _quantity, uint _itemID, uint _creditCard, uint _deliveryAddress){
        creditCard = _creditCard;
        quantity = _quantity;
        deliveryAddress = _deliveryAddress;
        itemID = _itemID;

        queryManufacturer(quantity, itemID);
    }

    function queryManufacturer(uint _quantity, uint _itemID){
        //chiamata esterna all'oracle
        oraclize_query("URL", "https://xyz.io/queryManufacturer", '{"quantity": _quantity, "itemID": _itemID}');
    }

    function _callbackManufacturer(uint availability, uint _price, uint _weight) external {
        totalPrice = _price * quantity;
        if (availability = 1){
            totalWeight = quantity * _weight;
            queryPayment(totalPrice, totalWeight);
        } else {
            querySupplier(quantity, itemID);
        }
    }

    function queryPayment(uint _totalPrice, uint _creditCard){
        //chiamata esterna all'oracle
        oraclize_query("URL", "https://xyz.io/queryPayment", '{"totalPrice": _totalPrice, "creditCard": _creditCard}');
    }

    function queryDelivery(uint _deliveryAddress, uint _quantity, uint _totalWeight){
        //chiamata esterna all'oracle
        oraclize_query("URL", "https://xyz.io/queryDelivery", '{"deliveryAddress": _deliveryAddress, "quantity": _quantity, "totalWeight": _totalWeight}');
    }

    function querySupplier(uint _quantity, uint _itemID){ // ti manca  totalPrice = _price * quantity; riga 18 dell'esempio

        //chiamata esterna all'oracle
        oraclize_query("URL", "https://xyz.io/querySupplier", '{"quantity": _quantity, "itemID": _itemID}');
    }

}
