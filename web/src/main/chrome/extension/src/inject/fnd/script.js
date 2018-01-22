(function() {
    /***
     * This script will
     * Receive message from bg/script.js
     */
    chrome.runtime.onMessage.addListener(
        function(message, sender, sendResponse) {
            // if app is loaded, let it know that it's loaded via chrome plugin
            document.addEventListener('fnd.loaded', function(e) {
                document.dispatchEvent(new CustomEvent('chrome.from.extension.fnd.opened'));
            });

            // event send from app when tab is closed after funding
            document.addEventListener('chrome.to.extension.fnd.FUND_SUCCESS', function(event) {
                sweetAlert('Funded!', 'Funds have been committed, once the transaction is confirmed the are transferred to the request. Click OK to go back to Github.', 'success').then(() => {
                    sendResponse({id: message.id, success: true, message: event.detail.body})
                });
            });
            // event send from app when tab is closed after funding
            document.addEventListener('chrome.to.extension.fnd.FUND_FAILED', function(event) {
                sendResponse({id: message.id, success: false, message: event.data.body});
            });
            // // check if page is loaded
            return true;
        }
    );
})();