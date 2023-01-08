(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
    };
    
    var setBinding = function() {
    };

    var namespace = win.auction;
    var __COMPONENT_NAME = "SBID_POP"; 
    namespace[__COMPONENT_NAME] = (function () {
        var init = function(){
            setLayout();
            setBinding();
        };
        return {
            init: init
        }
    })();
    $(function () {
        namespace[__COMPONENT_NAME].init();
    });
})(jQuery, window, document);
