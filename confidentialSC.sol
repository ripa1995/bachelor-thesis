pragma solidity ^0.4.0;
pragma experimental ABIEncoderV2;

contract Contract is usingTinyOracle {

    uint totalPrice;
    uint totalPriceSupplier;
    uint totalWeight;
    uint creditCardqueryPayment;
    uint creditCardqueryPayment;
    uint _weightqueryDeliveryHomomorphic1;
    uint _weightqueryDeliveryHomomorphic0;
    uint _pricequeryPaymentHomomorphic0;
    uint deliveryAddressqueryDelivery;
    uint deliveryAddressqueryDelivery;
    uint quantityqueryDelivery;
    uint quantityquerySupplier;
    uint quantityqueryDelivery;
    uint quantityqueryDeliveryHomomorphic1;
    uint quantityqueryDeliveryHomomorphic0;
    uint quantityqueryPaymentHomomorphic0;
    uint quantityqueryManufacturer;
    uint itemIDquerySupplier;
    uint itemIDqueryManufacturer;
    uint _supplierPricequeryPaymentHomomorphic0;


    event queryBuyerGUIEvent();
    event queryManufacturerEvent(uint _quantity, uint _itemID);
    event queryPaymentEvent(uint _totalPrice, uint _creditCard);
    event queryDeliveryEvent(uint _deliveryAddress, uint _quantity, uint _totalWeight);
    event querySupplierEvent(uint _quantity, uint _itemID);

    function main(){
        string header = "_quantity, queryManufacturer, queryPaymentHomomorphic, queryDeliveryHomomorphic, queryDelivery, querySupplier, _itemID, queryManufacturer, querySupplier, _creditCard, queryPayment, _deliveryAddress, queryDelivery";
        queryBuyerGUI(header);
    }

    function queryBuyerGUI(string header){
        //do things
    }

    function queryManufacturer(uint _quantity, uint _itemID, string header){
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

    function querySupplier(uint _quantity, uint _itemID, string header){
        //chiamata esterna all'oracle
        bytes tmp = _quantity;
        queryTinyOracle(tmp);
    }

    function _callbackBuyerGUI(uint _quantity, uint _itemID, uint _creditCard, uint _deliveryAddress) onlyFromTinyOracle external {
        string memory creditCardqueryPaymentstring = toString(_creditCard);
        string memory creditCardqueryPaymentsubstring = substring(creditCardqueryPaymentstring, 77, 0);
        creditCardqueryPayment = parseInt(creditCardqueryPaymentsubstring);

        string memory quantityquerySupplierstring = toString(_quantity);
        string memory quantityquerySuppliersubstring = substring(quantityquerySupplierstring, 77, 308);
        quantityquerySupplier = parseInt(quantityquerySuppliersubstring);
        string memory quantityqueryDeliverystring = toString(_quantity);
        string memory quantityqueryDeliverysubstring = substring(quantityqueryDeliverystring, 77, 231);
        quantityqueryDelivery = parseInt(quantityqueryDeliverysubstring);
        string memory quantityqueryDeliveryHomomorphic1string = toString(_quantity);
        string memory quantityqueryDeliveryHomomorphic1substring = substring(quantityqueryDeliveryHomomorphic1string, 77, 154);
        quantityqueryDeliveryHomomorphic1 = parseInt(quantityqueryDeliveryHomomorphic1substring);
        string memory quantityqueryDeliveryHomomorphic0string = toString(_quantity);
        string memory quantityqueryDeliveryHomomorphic0substring = substring(quantityqueryDeliveryHomomorphic0string, 77, 154);
        quantityqueryDeliveryHomomorphic0 = parseInt(quantityqueryDeliveryHomomorphic0substring);
        string memory quantityqueryPaymentHomomorphic0string = toString(_quantity);
        string memory quantityqueryPaymentHomomorphic0substring = substring(quantityqueryPaymentHomomorphic0string, 77, 77);
        quantityqueryPaymentHomomorphic0 = parseInt(quantityqueryPaymentHomomorphic0substring);
        string memory quantityqueryManufacturerstring = toString(_quantity);
        string memory quantityqueryManufacturersubstring = substring(quantityqueryManufacturerstring, 77, 0);
        quantityqueryManufacturer = parseInt(quantityqueryManufacturersubstring);

        string memory deliveryAddressqueryDeliverystring = toString(_deliveryAddress);
        string memory deliveryAddressqueryDeliverysubstring = substring(deliveryAddressqueryDeliverystring, 77, 0);
        deliveryAddressqueryDelivery = parseInt(deliveryAddressqueryDeliverysubstring);

        string memory itemIDquerySupplierstring = toString(_itemID);
        string memory itemIDquerySuppliersubstring = substring(itemIDquerySupplierstring, 77, 77);
        itemIDquerySupplier = parseInt(itemIDquerySuppliersubstring);
        string memory itemIDqueryManufacturerstring = toString(_itemID);
        string memory itemIDqueryManufacturersubstring = substring(itemIDqueryManufacturerstring, 77, 0);
        itemIDqueryManufacturer = parseInt(itemIDqueryManufacturersubstring);

        string header = "_availability, _price, queryPaymentHomomorphic, _weight, queryDeliveryHomomorphic";
        queryManufacturer(quantityqueryManufacturer, itemIDqueryManufacturer, header);
    }

    function _callbackManufacturer(uint _availability, uint _price, uint _weight) onlyFromTinyOracle external {
        string memory _pricequeryPaymentHomomorphic0string = toString(_price);
        string memory _pricequeryPaymentHomomorphic0substring = substring(_pricequeryPaymentHomomorphic0string, 77, 0);
        _pricequeryPaymentHomomorphic0 = parseInt(_pricequeryPaymentHomomorphic0substring);
        totalPrice = _pricequeryPaymentHomomorphic0 * quantityqueryPaymentHomomorphic0;
        if (_availability == 1) {
            string memory _weightqueryDeliveryHomomorphic1string = toString(_weight);
            string memory _weightqueryDeliveryHomomorphic1substring = substring(_weightqueryDeliveryHomomorphic1string, 77, 0);
            _weightqueryDeliveryHomomorphic1 = parseInt(_weightqueryDeliveryHomomorphic1substring);
            string memory _weightqueryDeliveryHomomorphic0string = toString(_weight);
            string memory _weightqueryDeliveryHomomorphic0substring = substring(_weightqueryDeliveryHomomorphic0string, 77, 0);
            _weightqueryDeliveryHomomorphic0 = parseInt(_weightqueryDeliveryHomomorphic0substring);
            totalWeight = quantityqueryDeliveryHomomorphic0 * _weightqueryDeliveryHomomorphic0;
            uint totalWeight2 = quantityqueryDeliveryHomomorphic1 * _weightqueryDeliveryHomomorphic1;
            queryPayment(totalPrice, creditCardqueryPayment);
            queryDelivery(deliveryAddressqueryDelivery, quantityqueryDelivery, totalWeight);
        } else {
            string header = "_supplierPrice, queryPaymentHomomorphic";
            querySupplier(quantityquerySupplier, itemIDquerySupplier, header);
        }
    }

    function _callbackSupplier(uint _supplierPrice) onlyFromTinyOracle external {
        string memory _supplierPricequeryPaymentHomomorphic0string = toString(_supplierPrice);
        string memory _supplierPricequeryPaymentHomomorphic0substring = substring(_supplierPricequeryPaymentHomomorphic0string, 77, 0);
        _supplierPricequeryPaymentHomomorphic0 = parseInt(_supplierPricequeryPaymentHomomorphic0substring);
        totalPriceSupplier = totalPrice + _supplierPricequeryPaymentHomomorphic0;
        queryPayment(totalPriceSupplier, creditCardqueryPayment);
        queryDelivery(deliveryAddressqueryDelivery, quantityqueryDelivery, totalWeight2);
    }

    function parseInt(string _value) public returns (uint _ret) {
        bytes memory _bytesValue = bytes(_value);
        uint j = 1;
        for (uint i = _bytesValue.length - 1; i >= 0 && i < _bytesValue.length; i--) {
            assert(_bytesValue[i] >= 48 && _bytesValue[i] <= 57);
            _ret += (uint(_bytesValue[i]) - 48) * j;
            j *= 10;
        }
    }

    function substring(string _base, uint _length, uint _offset) internal returns (string) {
        bytes memory _baseBytes = bytes(_base);
        assert(uint(_offset + _length) <= _baseBytes.length);
        string memory _tmp = new string(uint(_length));
        bytes memory _tmpBytes = bytes(_tmp);
        uint j = 0;
        for (uint i = uint(_offset); i < uint(_offset + _length); i++) {
            _tmpBytes[j++] = _baseBytes[i];
        }
        return string(_tmpBytes);
    }

    function toString(uint _base) internal returns (string) {
        bytes memory _tmp = new bytes(32);
        uint i;
        for (i = 0; _base > 0; i++) {
            _tmp[i] = byte((_base % 10) + 48);
            _base /= 10;
        }
        bytes memory _real = new bytes(i--);
        for (uint j = 0; j < _real.length; j++) {
            _real[j] = _tmp[i--];
        }
        return string(_real);
    }
}