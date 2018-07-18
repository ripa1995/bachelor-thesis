pragma solidity ^0.4.0;
pragma experimental ABIEncoderV2;

contract Contract {

    uint quantity;
    uint itemID;
    uint creditCard;
    uint deliveryAddress;
    uint totalPrice;
    uint totalPriceSupplier;
    uint totalWeight;


    event queryBuyerGUI();
    event queryManufacturer(uint _quantity, uint _itemID);
    event queryPayment(uint _totalPrice, uint _creditCard);
    event queryDelivery(uint _deliveryAddress, uint _quantity, uint _totalWeight);
    event querySupplier(uint _quantity, uint _itemID);

    function main(){
        queryBuyerGUI();
    }

    function _callbackBuyerGUI(uint _quantity, uint _itemID, uint _creditCard, uint _deliveryAddress) {
        creditCard = _creditCard;
        quantity = _quantity;
        deliveryAddress = _deliveryAddress;
        itemID = _itemID;
        queryManufacturer(quantity, itemID);
    }

    function _callbackManufacturer(uint _availability, uint _price, uint _weight) {
        totalPrice = _price * quantity;
        if (_availability==1){
            totalWeight = quantity * _weight;
            queryPayment(totalPrice, creditCard);
        } else {
            querySupplier(quantity, itemID);
        }
    }

    function _callbackSupplier(uint _supplierPrice) onlyFromTinyOracle external {
        totalPriceSupplier = totalPrice + _supplierPrice;
        queryPayment(totalPriceSupplier, creditCard);
    }

    function _callbackPayment() onlyFromTinyOracle external {
        queryDelivery(deliveryAddress, quantity, totalWeight);
    }

}
